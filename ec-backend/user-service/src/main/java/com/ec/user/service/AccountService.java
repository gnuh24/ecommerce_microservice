package com.ec.user.service;

//import com.sgu.backend.dto.request.account.*;
//import com.sgu.backend.dto.request.auth.UserRegistrationForm;
//import com.sgu.backend.entities.Account;
//import com.sgu.backend.entities.OTP;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
import com.ec.user.dto.auth.UserRegistrationForm;
import com.ec.user.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
	
	Account getAccountByUsername(String username);
	Account createAccount(UserRegistrationForm accountCreateForm);
//    Page<Account> getAllAccounts(Pageable pageable, AccountFilterForm filterForm);
//
//    Account getAccountByEmail(String username);
//
//    Account getAccountById(String id);
//
//    boolean isEmailExists(String email);
//
//    Account createAccount(UserRegistrationForm userRegistrationForm);
//
//    Account updateStatusOfAccount(String accountId, Account.Status status);
//
//    Account updateRoleOfAccount(String accountId, Account.Role role);
//
//		Account resetPasswordOfAccount(OTP otp, AccountUpdateFormForResetPassword form);
}