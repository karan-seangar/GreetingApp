package com.example.GreetingApp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.GreetingApp.dto.ForgotPasswordDTO;
import com.example.GreetingApp.dto.ResetPasswordDTO;
import com.example.GreetingApp.interfaces.IAuthenticationService;
import com.example.GreetingApp.util.JwtToken;
import com.example.GreetingApp.Exception.UserException;
import com.example.GreetingApp.dto.AuthUserDTO;
import com.example.GreetingApp.dto.LoginDTO;
import com.example.GreetingApp.model.AuthUser;
import com.example.GreetingApp.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    JwtToken tokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AuthUser register(AuthUserDTO userDTO) throws Exception {
        AuthUser user = new AuthUser(userDTO);
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        String token = tokenUtil.createToken(user.getUserId());

        user.setPassword(encodedPassword);
        user.setResetToken(token);
        System.out.println(user);

        authUserRepository.save(user);
        emailSenderService.sendEmail(user.getEmail(),"Welcome to MyHI App", "Hello "
                + user.getFirstName()
                + "\n I am MyHI, your Assistant.\nYou have been successfully registered to MyHI Platform!.\n"
                + "Feel free to ask anything.\n\n\n"
                + "Till then, Here is your Profile and Registration Details:\n\n User Id:  "
                + user.getUserId() + "\n First Name:  "
                + user.getFirstName() + "\n Last Name:  "
                + user.getLastName() + "\n Email:  "
                + user.getEmail() + "\n Address:  "
                + "\n Token:  " + token);
        return user;
    }

    public Optional<AuthUser> existsByEmail(String email) {
        Optional<AuthUser> user = Optional.ofNullable(authUserRepository.findByEmail(email));
        return user;
    }

    @Override
    public String login(LoginDTO loginDTO) throws UserException {
        Optional<AuthUser> user = existsByEmail(loginDTO.getEmail());
        if (user.isPresent() && passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
            emailSenderService.sendEmail(user.get().getEmail(),"Logged in Successfully!", "Hii...."+user.get().getFirstName()+"\n\n You have successfully logged in into Greeting App!");
            return "Congratulations!! You have logged in successfully!";
        } else if (!user.isPresent()) {
            throw new UserException("Sorry! User not Found!");
        } else if (!passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
            throw new UserException("Sorry! Password is incorrect!");
        } else {
            throw new UserException("Sorry! Email or Password is incorrect!");
        }
    }

    @Override
    public String forgotPassword(String email, ForgotPasswordDTO forgotPasswordDTO) throws UserException {
        AuthUser user = authUserRepository.findByEmail(email);
        if (user != null) {
            String newEncodedPassword = passwordEncoder.encode(forgotPasswordDTO.getPassword());
            user.setPassword(newEncodedPassword);
            String token = tokenUtil.createToken(user.getUserId());
            user.setResetToken(token);
            authUserRepository.save(user);
            return "Password has been changed successfully!";
        } else {
            throw new UserException("Sorry! User not Found!");
        }
    }

    @Override
    public String resetPassword(String email, ResetPasswordDTO resetPasswordDTO) throws UserException {
        AuthUser user = authUserRepository.findByEmail(email);
        if (user != null) {
            if (passwordEncoder.matches(resetPasswordDTO.getCurrentPassword(), user.getPassword())) {
                String resetEncodedPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());
                user.setPassword(resetEncodedPassword);
                authUserRepository.save(user);
                return "Password reset successfully!";
            } else {
                throw new UserException("Sorry! Current Password is incorrect!");
            }
        } else {
            throw new UserException("Sorry! User not Found with email: " + email);
        }
    }
}