package com.example.eventmanagement.controller;

import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.entity.Registration;
import com.example.eventmanagement.entity.User;
import com.example.eventmanagement.exception.ResourceNotFoundException;
import com.example.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/add/{adminId}")
    public ResponseEntity<String> addEvent(@PathVariable Long adminId, @RequestBody Event event) {

        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event created successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Map<String, Object>>> getEventParticipants(@PathVariable Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        List<Map<String, Object>> participants = new ArrayList<>();

        for (Registration registration : event.getRegistrations()) {
            User user = registration.getUser();

            Map<String, Object> data = Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
            );

            participants.add(data);
        }

        return ResponseEntity.ok(participants);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setDate(updatedEvent.getDate());
        event.setVenue(updatedEvent.getVenue());

        eventRepository.save(event);

        return ResponseEntity.ok("Event updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        eventRepository.delete(event);

        return ResponseEntity.ok("Event deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String title) {
        return ResponseEntity.ok(eventRepository.findByTitleContaining(title));
    }
}