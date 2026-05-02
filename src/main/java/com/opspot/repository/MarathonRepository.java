package com.opspot.repository;

import com.opspot.entity.Marathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MarathonRepository extends JpaRepository<Marathon, Long>, JpaSpecificationExecutor<Marathon> {

    boolean existsByRegistrationLink(String registrationLink);
}
