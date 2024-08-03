package com.eagersphinx.repo;

import com.eagersphinx.domain.entity.Event;
import com.eagersphinx.domain.entity.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepo extends CrudRepository<Users, Integer> {
    List<Users> findByIdIn(List<Integer> toList);
}
