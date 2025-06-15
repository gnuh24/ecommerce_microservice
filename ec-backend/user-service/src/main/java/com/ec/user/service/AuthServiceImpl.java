package com.ec.user.service;

import com.ec.user.dto.account.AccountCreateForm;
import com.ec.user.dto.account.AccountRedisDTO;
import com.ec.user.dto.auth.*;
import com.ec.user.dto.profile.ProfileCreateForm;
import com.ec.user.entity.Account;
import com.ec.user.entity.Profile;
import com.ec.user.integration.redis.RedisConstants;
import com.ec.user.integration.redis.RedisService;
import com.ec.user.security.JwtTokenProvider;
import com.ec.user.utils.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProfileService profileService;
//
//	@Autowired
//	private AuthExceptionHandler authExceptionHandler;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RedisService redisService;
	
	@Override
	@Transactional
	public Account activeAccount(String otp) {
		
		AccountRedisDTO account = (AccountRedisDTO) redisService.get(RedisConstants.OTP_VERIFY_ACCOUNT + ":" + otp);
		
		if (account == null) {
			throw new RuntimeException("OTP không tồn tại hoặc đã hết hạn sử dụng !");
		}
		
		ProfileCreateForm profileCreateForm = new ProfileCreateForm();
		profileCreateForm.setEmail(account.getUsername());
		Profile profile = profileService.createProfile(profileCreateForm);

		
		AccountCreateForm accountCreateForm = new AccountCreateForm();
		accountCreateForm.setId(account.getId());
		accountCreateForm.setUsername(account.getUsername());
		accountCreateForm.setPassword(account.getPassword());
		accountService.createAccount(accountCreateForm, profile);
		
		return accountService.activeAccount(account.getId());
		
	}
	
	@Override
	public boolean isUsernameExists(String username) {
		return redisService.exists(RedisConstants.USERNAME_EXIST + ":" + username);
	}
	
	@Override
	public AuthResponseDTO login(LoginRequestForm request) {
		Account user = accountService.getAccountByUsername(request.getUsername());
		
		if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Email hoặc mật khẩu không đúng!");
		}
		
		if (user.getRole() != Account.Role.USER) {
			throw new BadCredentialsException("Email hoặc mật khẩu không đúng!");
		}
		
		if (user.getStatus().toString().equals("INACTIVE")) {
			throw new DisabledException("Tài khoản của bạn chưa được kích hoạt, hãy kiểm tra email " + request.getUsername());
		}
		
		if (user.getStatus().toString().equals("BANNED")) {
			throw new LockedException("Tài khoản của bạn đã bị khóa! Nếu có vấn đề, vui lòng liên hệ Admin.");
		}
		
		// Tạo và trả về AuthResponseDTO
		return buildAuthResponse(user);
	}
	
	@Override
	public AuthResponseDTO staffLogin(LoginRequestForm request) {
		Account user = accountService.getAccountByUsername(request.getUsername());
		
		if (user == null || user.getRole().equals(Account.Role.USER) || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Email hoặc mật khẩu không đúng!");
		}
		
		if (user.getStatus().toString().equals("INACTIVE")) {
			throw new DisabledException("Tài khoản của bạn chưa được kích hoạt, hãy kiểm tra email " + request.getUsername());
		}
		
		if (user.getStatus().toString().equals("BANNED")) {
			throw new LockedException("Tài khoản của bạn đã bị khóa! Nếu có vấn đề, vui lòng liên hệ Admin.");
		}
		
		// Tạo và trả về AuthResponseDTO
		return buildAuthResponse(user);
	}
	
	
	@Override
	@Transactional
	public AccountRedisDTO register(UserRegistrationForm userRegistrationForm) {
//		if (accountService.isEmailExists(userRegistrationForm.getEmail())) {
//			throw new RuntimeException("Email :" + userRegistrationForm.getEmail() + " đã tồn tại trong hệ thống !");
//		}
		
		
		String accountId = IdGenerator.generateId();
		redisService.set(RedisConstants.USERNAME_EXIST + ":" + userRegistrationForm.getUsername(), "true", 5, TimeUnit.MINUTES);
		
		
		AccountRedisDTO account = new AccountRedisDTO();
		account.setId(accountId);
		account.setUsername(userRegistrationForm.getUsername());
		account.setPassword(
		    passwordEncoder.encode(
			userRegistrationForm.getPassword()
		    )
		);
		
		String otp = IdGenerator.generateOTP();
		redisService.setObjectWithTTL(RedisConstants.OTP_VERIFY_ACCOUNT + ":" + otp, account, 5, TimeUnit.MINUTES);
		
		emailService.sendRegistrationUserConfirm(userRegistrationForm.getUsername(),  otp);
		return account;
	}
	
	@Override
	public void sendOtpResetPassword(String username) {
		redisService.delete(RedisConstants.OTP_FORGOT_PASSWORD + ":" + username);
		String otp = IdGenerator.generateOTP();
		redisService.set(RedisConstants.OTP_FORGOT_PASSWORD + ":" + username, otp,3, TimeUnit.MINUTES);
		emailService.sendResetPasswordUserConfirm(username, otp);
	}
	
	@Override
	public Account resetPassword(String username, ResetPasswordForm form) {
		String otpRedis = redisService.get(RedisConstants.OTP_FORGOT_PASSWORD + ":" + username).toString();
		
		if (!otpRedis.equals(form.getOtp())){
			throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn!");
		}
		
		redisService.delete(RedisConstants.OTP_FORGOT_PASSWORD + ":" + username);
		return accountService.updatePassword(username, form.getNewPassword());
	}
	
	@Override
	public Account updatePassword(UpdatePasswordForm form) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Account account = (Account) authentication.getPrincipal();
		
		if (!passwordEncoder.matches(form.getOldPassword(), account.getPassword())) {
			throw new RuntimeException("Mật khẩu cũ không đúng !!");
		}
		
		String newPassword = passwordEncoder.encode(form.getNewPassword());
		return accountService.updatePassword(account, newPassword);
		
	}

//	@Override
//	@Transactional
//	public boolean verifiOTP(String email, String otp) {
//		if (!redisService.exists(RedisContants.OTP_CODE + otp)) {
//			return false;
//		}
//		UserRegistrationForm userRegistrationForm = (UserRegistrationForm) redisService.get(RedisContants.OTP_CODE + otp);
//
//		if (!Objects.equals(email, userRegistrationForm.getEmail())) {
//			return false;
//		}
//		ProfileCreateForm form = ProfileCreateForm.builder()
//		    .phone(userRegistrationForm.getPhone())
////				.email(userRegistrationForm.getEmail())
//		    .fullname(userRegistrationForm.getFullname())
//		    .build();
//
	
	/// /		Profile profile=profileService.createProfile(form, );
//		Account account = accountService.createAccount(userRegistrationForm);
//
//		return true;
//	}
//

//
//	@Override
//	public AuthResponseDTO refreshToken(String oldToken, String refreshToken) {
//
//		AuthResponseDTO response = new AuthResponseDTO();
//
//		try {
//
//			String emailFromAccessToken = jwtTokenProvider.getUsernameWithoutExpired(oldToken);
//			String emailFromRefreshToken = jwtTokenProvider.getUsername(refreshToken);
//
//			if (!emailFromAccessToken.equals(emailFromRefreshToken)) {
//				throw new MismatchedTokenAccountException("AccessToken và RefreshToken không khớp với cùng một tài khoản.");
//			}
//			//Tìm tài khoản dựa trên Email
//			Account account = accountService.getAccountByEmail(emailFromAccessToken);
//
//			response.setId(account.getId());
//			response.setEmail(emailFromAccessToken);
//			response.setRole(account.getRole().toString());
//
//			// Tạo Token
//			String jwt = jwtTokenProvider.generateToken(account);
//			response.setToken(jwt);
//			response.setTokenExpirationTime("30 phút");
//
//			// Tạo Refresh Token
//			response.setRefreshToken(refreshToken);
//			response.setRefreshTokenExpirationTime("7 ngày");
//
//		} catch (ExpiredJwtException e1) {
//			throw new TokenExpiredException("Refresh Token đã hết hạn sử dụng.");
//		} catch (SignatureException e2) {
//			throw new InvalidJWTSignatureException("Refresh Token chứa signature không hợp lệ.");
//		} catch (UsernameNotFoundException e3) {
//			throw new UsernameNotFound("Refresh Token chứa thông tin không tồn tại trong hệ thống.");
//		}
//
//		return response;
//	}
	private AuthResponseDTO buildAuthResponse(Account user) {
		AuthResponseDTO response = new AuthResponseDTO();
		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setFullName(user.getProfile().getFullName());
		response.setRole(user.getRole().toString());
		
		// Tạo Token
		String jwt = jwtTokenProvider.generateToken(user);
		response.setToken(jwt);
		response.setTokenExpirationTime("30 phút");

//		redisService.set(RedisContants.TOKEN + jwt, true);
		
		
		// Tạo Refresh Token
		String refreshToken = jwtTokenProvider.generateRefreshToken(new HashMap<>(), user);
		response.setRefreshToken(refreshToken);
		response.setRefreshTokenExpirationTime("7 ngày");
		
		return response;
	}

//    @Override
//    public AuthResponseDTO refreshToken(String oldToken, String refreshToken){
//
//	AuthResponseDTO response = new AuthResponseDTO();
//
//	try{
//
//	    String emailFromAccessToken = jwtTokenProvider.getUsernameWithoutExpired(oldToken);
//	    String emailFromRefreshToken = jwtTokenProvider.getUsername(refreshToken);
//
//	    if (!emailFromAccessToken.equals(emailFromRefreshToken)){
//		throw new MismatchedTokenAccountException("AccessToken và RefreshToken không khớp với cùng một tài khoản.");
//	    }
//	    //Tìm tài khoản dựa trên Email
//	    Account account = accountService.getAccountByEmail(emailFromAccessToken);
//
//	    response.setId(account.getId());
//	    response.setEmail(emailFromAccessToken);
//	    response.setRole(account.getRole().toString());
//
//	    // Tạo Token
//	    String jwt = jwtTokenProvider.generateToken(account);
//	    response.setToken(jwt);
//	    response.setTokenExpirationTime("30 phút");
//
//	    // Tạo Refresh Token
//	    response.setRefreshToken(refreshToken);
//	    response.setRefreshTokenExpirationTime("7 ngày");
//
//	} catch (ExpiredJwtException e1) {
//	    throw new TokenExpiredException("Refresh Token đã hết hạn sử dụng.");
//	} catch (SignatureException e2) {
//	    throw new InvalidJWTSignatureException("Refresh Token chứa signature không hợp lệ.");
//	} catch (UsernameNotFoundException e3) {
//	    throw new UsernameNotFound("Refresh Token chứa thông tin không tồn tại trong hệ thống.");
//	}
//
//	return response;
//    }
}
