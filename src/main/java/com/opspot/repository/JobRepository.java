package com.opspot.repository;

import com.opspot.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    boolean existsByJobLink(String jobLink);

    Optional<Job> findByJobLink(String jobLink);
}
