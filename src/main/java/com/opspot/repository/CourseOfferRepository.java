package com.opspot.repository;

import com.opspot.entity.CourseOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CourseOfferRepository extends JpaRepository<CourseOffer, Long>, JpaSpecificationExecutor<CourseOffer> {

    boolean existsByOfferLink(String offerLink);

    Optional<CourseOffer> findByOfferLink(String offerLink);
}
