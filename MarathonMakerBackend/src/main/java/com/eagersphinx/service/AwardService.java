package com.eagersphinx.service;

import com.eagersphinx.domain.entity.Users;
import com.eagersphinx.domain.entity.UsersBib;
import com.eagersphinx.domain.exception.ErrorCode;
import com.eagersphinx.domain.exception.MarathonException;
import com.eagersphinx.repo.UsersBibRepo;
import com.eagersphinx.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AwardService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private UsersBibRepo usersBibRepo;
    public void markDone(Integer bib) {
        Optional<UsersBib> b =  usersBibRepo.findByBib(bib);
        if (b.isPresent()) {
            Optional<Users> u = usersRepo.findById(b.get().getUserId());
            if (u.isPresent()) {
                Users us = u.get();
                us.setIsAwarded(true);
                usersRepo.save(us);
            }
        } else throw new MarathonException(ErrorCode.M1);
    }
}
