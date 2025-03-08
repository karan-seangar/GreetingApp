package com.example.GreetingApp.controller;
import com.example.GreetingApp.Exception.ResourceNotFoundException;
import com.example.GreetingApp.dto.GreetingDTO;
import com.example.GreetingApp.dto.UserDTO;
import com.example.GreetingApp.interfaces.IGreetingService;
import com.example.GreetingApp.model.Greeting;
import com.example.GreetingApp.repository.GreetingRepository;
import com.example.GreetingApp.service.GreetingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/greetings")
public class GreetingController {
    @Autowired
    IGreetingService greetingService;

    @PostMapping("")
    public GreetingDTO getGreeting(@RequestParam(value = "firstName", defaultValue = "", required = false) String firstName, @RequestParam(value = "lastName", defaultValue = "", required = false) String lastName) {
        UserDTO user = new UserDTO();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return new GreetingDTO(greetingService.addGreeting(user));
    }

    @GetMapping("/{id}")
    public GreetingDTO getGreetingById(@PathVariable(value = "id") long id) {
        return greetingService.getGreetingById(id);
    }

    @GetMapping("/all")
    public Iterable<GreetingDTO> getAllGreetings() {
        return greetingService.getAllGreetings();
    }

    @PutMapping("/edit/{id}")
    public GreetingDTO editGreeting(@PathVariable(value = "id") long id, @RequestParam(value = "firstName", defaultValue = "", required = false) String firstName, @RequestParam(value = "lastName", defaultValue = "", required = false) String lastName) {
        UserDTO user = new UserDTO();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return new GreetingDTO(greetingService.editGreeting(id, user));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGreeting(@PathVariable(value = "id") long id) {
        greetingService.deleteGreeting(id);
    }
}