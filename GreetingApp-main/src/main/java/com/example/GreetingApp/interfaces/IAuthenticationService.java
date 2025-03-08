package com.example.GreetingApp.interfaces;

import com.example.GreetingApp.Exception.UserException;
import com.example.GreetingApp.dto.AuthUserDTO;
import com.example.GreetingApp.dto.ForgotPasswordDTO;
import com.example.GreetingApp.dto.LoginDTO;
import com.example.GreetingApp.dto.ResetPasswordDTO;
import com.example.GreetingApp.model.AuthUser;
import jakarta.validation.Valid;

public interface IAuthenticationService {
    AuthUser register(AuthUserDTO userDTO) throws Exception;
    String login(LoginDTO loginDTO) throws UserException;
    String forgotPassword(String email, @Valid ForgotPasswordDTO forgotPasswordDTO) throws UserException;
    String resetPassword(String email, @Valid ResetPasswordDTO resetPasswordDTO) throws UserException;
}
