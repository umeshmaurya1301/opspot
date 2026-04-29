package com.opspot.repository;

import com.opspot.entity.AIToolOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AIToolOfferRepository extends JpaRepository<AIToolOffer, Long>, JpaSpecificationExecutor<AIToolOffer> {

    boolean existsByOfferLink(String offerLink);

    Optional<AIToolOffer> findByOfferLink(String offerLink);
}
