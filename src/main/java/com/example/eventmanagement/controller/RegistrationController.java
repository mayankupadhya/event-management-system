package com.example.eventmanagement.controller;

import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.entity.Registration;
import com.example.eventmanagement.entity.User;
import com.example.eventmanagement.exception.ResourceNotFoundException;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.repository.RegistrationRepository;
import com.example.eventmanagement.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/register/{eventId}")
    public ResponseEntity<String> registerForEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Registration existing =
                registrationRepository.findByUser_IdAndEvent_Id(user.getId(), eventId);

        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already registered for this event");
        }

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);

        registrationRepository.save(registration);

        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("/my-events")
    public ResponseEntity<List<Map<String, Object>>> getMyEvents(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();

        List<Registration> registrations = registrationRepository.findAll()
                .stream()
                .filter(r -> r.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        List<Map<String, Object>> result = registrations.stream()
                .map(r -> Map.<String, Object>of(
                        "eventId", r.getEvent().getId(),
                        "title", r.getEvent().getTitle(),
                        "date", r.getEvent().getDate(),
                        "venue", r.getEvent().getVenue()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}