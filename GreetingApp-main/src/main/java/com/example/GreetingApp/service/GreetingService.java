package com.example.GreetingApp.service;

import com.example.GreetingApp.dto.GreetingDTO;
import com.example.GreetingApp.dto.UserDTO;
import com.example.GreetingApp.interfaces.IGreetingService;
import com.example.GreetingApp.model.Greeting;
import com.example.GreetingApp.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GreetingService implements IGreetingService {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    GreetingRepository greetingRepository;

    @Override
    public Greeting addGreeting(UserDTO user) {
        String message = String.format(template, (user.getFirstName().isEmpty() && user.getLastName().isEmpty()) ? "World" : user.getFirstName() + " " + user.getLastName());
        return greetingRepository.save(new Greeting(counter.incrementAndGet(), message));
    }

    @Override
    public GreetingDTO getGreetingById(long id) {
        Greeting greeting = greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting not found with id: " + id));
        return new GreetingDTO(greeting.getId(), greeting.getMessage());
    }

    @Override
    public List<GreetingDTO> getAllGreetings() {
        List<Greeting> allGreetings = greetingRepository.findAll();
        return allGreetings.stream()
                .map(greeting -> new GreetingDTO(greeting.getId(), greeting.getMessage()))
                .toList();
    }

    @Override
    public Greeting editGreeting(long id, UserDTO user) {
        Greeting greeting = greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting not found with id: " + id));
        greeting.setMessage(String.format(template, (user.getFirstName().isEmpty() && user.getLastName().isEmpty()) ? "World" : user.getFirstName() + " " + user.getLastName()));
        return greetingRepository.save(greeting);
    }

    @Override
    public void deleteGreeting(long id) {
        greetingRepository.deleteById(id);
    }
}