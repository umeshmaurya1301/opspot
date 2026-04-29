package com.opspot.specification;

import com.opspot.dto.ai.AIToolOfferFilterParams;
import com.opspot.entity.AIToolOffer;
import com.opspot.enums.ApplicationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AIToolOfferSpecification {

    public static Specification<AIToolOffer> withFilters(AIToolOfferFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getToolName() != null && !params.getToolName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("toolName")),
                        "%" + params.getToolName().toLowerCase() + "%"));
            }
            if (params.getIsFree() != null) {
                predicates.add(cb.equal(root.get("isFree"), params.getIsFree()));
            }
            if (params.getForProfessionals() != null) {
                predicates.add(cb.equal(root.get("forProfessionals"), params.getForProfessionals()));
            }
            if (!params.isShowRejected()) {
                predicates.add(cb.notEqual(root.get("status"), ApplicationStatus.REJECTED));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private AIToolOfferSpecification() {}
}
