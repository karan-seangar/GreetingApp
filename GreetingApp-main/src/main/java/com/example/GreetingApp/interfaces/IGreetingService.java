package com.example.GreetingApp.interfaces;

import com.example.GreetingApp.dto.GreetingDTO;
import com.example.GreetingApp.dto.UserDTO;
import com.example.GreetingApp.model.Greeting;

import java.util.List;

public interface IGreetingService {
    Greeting addGreeting(UserDTO user);
    GreetingDTO getGreetingById(long id);
    List<GreetingDTO> getAllGreetings();
    Greeting editGreeting(long id, UserDTO user);
    void deleteGreeting(long id);
}
