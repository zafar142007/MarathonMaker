package com.eagersphinx.repo;

import com.eagersphinx.domain.entity.Event;
import com.eagersphinx.domain.entity.UsersBib;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersBibRepo extends CrudRepository<UsersBib, Integer> {
    Optional<UsersBib> findByBibAndEventId(int bib, int eventId);

    List<UsersBib> findByEventIdIn(List<Integer> events);

    Optional<UsersBib> findByBib(Integer bib);
}
