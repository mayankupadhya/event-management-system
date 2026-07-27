package com.example.eventmanagement.repository;

import com.example.eventmanagement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Registration findByUser_IdAndEvent_Id(Long userId, Long eventId);

}