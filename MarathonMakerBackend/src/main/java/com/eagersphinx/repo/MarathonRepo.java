package com.eagersphinx.repo;

import com.eagersphinx.domain.entity.Marathon;
import org.springframework.data.repository.CrudRepository;

public interface MarathonRepo extends CrudRepository<Marathon, Integer> {
}
