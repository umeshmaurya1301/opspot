package com.opspot.specification;

import com.opspot.dto.job.JobFilterParams;
import com.opspot.entity.Job;
import com.opspot.enums.ApplicationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    public static Specification<Job> withFilters(JobFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getLocation() != null && !params.getLocation().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("location")),
                        "%" + params.getLocation().toLowerCase() + "%"));
            }
            if (params.getWorkMode() != null) {
                predicates.add(cb.equal(root.get("workMode"), params.getWorkMode()));
            }
            // any-match: skills param is a single skill keyword checked against CSV field
            if (params.getSkills() != null && !params.getSkills().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("skills")),
                        "%" + params.getSkills().toLowerCase() + "%"));
            }
            if (params.getExperienceMin() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("experienceMin"), params.getExperienceMin()));
            }
            if (params.getExperienceMax() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("experienceMax"), params.getExperienceMax()));
            }
            if (!params.isShowRejected()) {
                predicates.add(cb.notEqual(root.get("status"), ApplicationStatus.REJECTED));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private JobSpecification() {}
}
