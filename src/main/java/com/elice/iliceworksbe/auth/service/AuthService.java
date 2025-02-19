package com.elice.iliceworksbe.auth.service;

import com.elice.iliceworksbe.auth.dto.request.CheckDuplicateAccountIdRequestDto;
import com.elice.iliceworksbe.auth.dto.request.ConfirmEmailRequestDto;
import com.elice.iliceworksbe.auth.dto.request.SignUpRequestDto;
import com.elice.iliceworksbe.auth.dto.request.VerifyEmailRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService  extends UserDetailsService {

    UserDetails loadUserByUsername(String accountId);
    void verifyEmail(VerifyEmailRequestDto verifyEmailRequestDto);
    void confirmVerificationCode(ConfirmEmailRequestDto confirmEmailRequestDto);
    Boolean checkDuplicateAccountId(CheckDuplicateAccountIdRequestDto checkDuplicateAccountIdRequestDTO);
    void signUp(SignUpRequestDto signUpRequestDto);
}
