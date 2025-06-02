package com.ec.user.service;


import com.ec.user.dto.auth.UserRegistrationForm;
import com.ec.user.dto.profile.ProfileCreateForm;
import com.ec.user.entity.Account;
import com.ec.user.entity.Profile;
import com.ec.user.integration.redis.RedisService;
import com.ec.user.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ProfileService profileService;

//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private OTPService otpService;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//    @Autowired
//    private EmailService emailService;
//
	@Autowired
	private RedisService redisService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Account with username " + username + " not found"));
	}
	
	@Override
	public Account getAccountByUsername(String username) {
		return accountRepository.findByUsername(username).orElse(null);
//                .orElseThrow(() -> new UsernameNotFoundException("Account with username " + username + " not found"));
	}


//    @Override
//    public Account getAccountById(String id) {
//        return accountRepository.findById(id)
//                .orElseThrow(() -> new UsernameNotFoundException("Account with id " + id + " not found"));
//    }
//
//    @Override
//    public Page<Account> getAllAccounts(Pageable pageable, AccountFilterForm filterForm) {
//        Specification<Account> specification = AccountSpecification.buildWhere(filterForm);
//        return accountRepository.findAll(specification, pageable);
//    }

	@Override
	@Transactional
	public Account createAccount(UserRegistrationForm accountCreateForm) {
		
		ProfileCreateForm profileCreateForm = new ProfileCreateForm();
		profileCreateForm.setEmail(accountCreateForm.getEmail());
		Profile profile = profileService.createProfile(profileCreateForm, null);
		
		Account account = new Account();
		
		account.setUsername(accountCreateForm.getEmail());
		account.setPassword(passwordEncoder.encode(accountCreateForm.getPassword()));
		account.setProfile(profile);
		
		account = accountRepository.save(account);

//        OTP otp = otpService.createOTP(account, OTP.Category.REGISTER, 25);
//        emailService.sendRegistrationUserConfirm(account.getEmail(), otp);
		return account;
	}
//
//    @Override
//    public Account updateStatusOfAccount(String accountId, Account.Status status) {
//        Account account = getAccountById(accountId);
//        account.setStatus(status);
//        return accountRepository.save(account);
//    }
//
//    @Override
//    public Account updateRoleOfAccount(String accountId, Account.Role role) {
//        Account account = getAccountById(accountId);
//        account.setRole(role);
//        return accountRepository.save(account);
//    }
//
//    @Override
//    public Account resetPasswordOfAccount(OTP otp, AccountUpdateFormForResetPassword form) {
//        	Account account = getAccountById(otp.getAccountId());
//            String newPassword = passwordEncoder.encode(form.getNewPassword());
//            account.setPassword(newPassword);
//            return accountRepository.save(account);
//
//    }
}

