package com.example.eventmanagement.service;

import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.entity.User;
import com.example.eventmanagement.exception.ResourceNotFoundException;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public String addEvent(Long adminId, Event event) {

        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getRole().equals("ADMIN")) {
            return "Only admin can create events";
        }

        eventRepository.save(event);
        return "Event created successfully";
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public String updateEvent(Long id, Event updatedEvent) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setDate(updatedEvent.getDate());
        event.setVenue(updatedEvent.getVenue());

        eventRepository.save(event);

        return "Event updated successfully";
    }

    public String deleteEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        eventRepository.delete(event);

        return "Event deleted successfully";
    }

    public List<Event> searchEvents(String title) {
        return eventRepository.findByTitleContaining(title);
    }
}