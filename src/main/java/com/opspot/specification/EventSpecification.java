package com.opspot.specification;

import com.opspot.dto.event.EventFilterParams;
import com.opspot.entity.Event;
import com.opspot.enums.ApplicationStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> withFilters(EventFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getCity() != null && !params.getCity().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("city")),
                        "%" + params.getCity().toLowerCase() + "%"));
            }
            if (params.getEventType() != null) {
                predicates.add(cb.equal(root.get("eventType"), params.getEventType()));
            }
            if (params.getWorkMode() != null) {
                predicates.add(cb.equal(root.get("workMode"), params.getWorkMode()));
            }
            if (params.getTheme() != null && !params.getTheme().isBlank()) {
                query.distinct(true);
                ListJoin<Event, String> themesJoin = root.joinList("themes", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(themesJoin), "%" + params.getTheme().toLowerCase() + "%"));
            }
            if (params.getStartDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), params.getStartDateFrom()));
            }
            if (params.getStartDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), params.getStartDateTo()));
            }
            if (params.getProfessionalAllowed() != null) {
                predicates.add(cb.equal(root.get("professionalAllowed"), params.getProfessionalAllowed()));
            }
            if (!params.isShowRejected()) {
                predicates.add(cb.notEqual(root.get("status"), ApplicationStatus.REJECTED));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private EventSpecification() {}
}
