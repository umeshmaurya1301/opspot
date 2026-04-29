package com.opspot.repository;

import com.opspot.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    boolean existsByRegistrationLink(String registrationLink);

    Optional<Event> findByRegistrationLink(String registrationLink);
}
