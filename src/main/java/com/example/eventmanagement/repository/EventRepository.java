package com.example.eventmanagement.repository;

import com.example.eventmanagement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTitleContaining(String title);

}