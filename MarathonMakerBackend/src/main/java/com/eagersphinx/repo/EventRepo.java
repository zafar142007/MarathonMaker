package com.eagersphinx.repo;

import com.eagersphinx.domain.entity.Event;
import com.eagersphinx.domain.entity.Loop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepo extends CrudRepository<Event, Integer> {
    List<Event> findByMarathonId(int id);
}
