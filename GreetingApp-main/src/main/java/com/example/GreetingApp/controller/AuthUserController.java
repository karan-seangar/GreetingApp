package com.example.GreetingApp.controller;

import com.example.GreetingApp.Exception.UserException;
import com.example.GreetingApp.dto.*;
import com.example.GreetingApp.interfaces.IAuthenticationService;
import com.example.GreetingApp.model.AuthUser;
import com.example.GreetingApp.service.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    IAuthenticationService authenticationService;

    @Autowired
    EmailSenderService emailSenderService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody AuthUserDTO userDTO) throws Exception {

        AuthUser user = authenticationService.register(userDTO);
        ResponseDTO responseUserDTO = new ResponseDTO("User details is submitted!", user);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) throws UserException {
        String result = authenticationService.login(loginDTO);
        ResponseDTO responseUserDTO = new ResponseDTO("Login successfully!!", result);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
    }

    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<ResponseDTO> forgotPassword(@PathVariable String email, @Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) throws UserException {
        String result = authenticationService.forgotPassword(email, forgotPasswordDTO);
        ResponseDTO responseDTO = new ResponseDTO("Reset password link sent to your email", result);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/reset-password/{email}")
    public ResponseEntity<ResponseDTO> resetPassword(@PathVariable String email, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws UserException {
        String result = authenticationService.resetPassword(email, resetPasswordDTO);
        ResponseDTO responseDTO = new ResponseDTO("Password reset successfully", result);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}