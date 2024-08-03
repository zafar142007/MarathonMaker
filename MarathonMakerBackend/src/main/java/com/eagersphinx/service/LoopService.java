package com.eagersphinx.service;

import com.eagersphinx.domain.dto.BibResponse;
import com.eagersphinx.domain.dto.LapResponse;
import com.eagersphinx.domain.dto.LoopDto;
import com.eagersphinx.domain.dto.MarathonResponse;
import com.eagersphinx.domain.entity.Loop;
import com.eagersphinx.domain.entity.Marathon;
import com.eagersphinx.domain.entity.UsersBib;
import com.eagersphinx.domain.exception.ErrorCode;
import com.eagersphinx.domain.exception.MarathonException;
import com.eagersphinx.repo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LoopService {

    @Autowired
    private LoopRepo loopRepo;

    @Autowired
    private UsersBibRepo usersBibRepo;

    @Autowired
    private MarathonRepo marathonRepo;
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UsersRepo usersRepo;

    public LapResponse incrementLoop(LoopDto loopDto) {
        Optional<UsersBib> usersBib = usersBibRepo.findByBib(loopDto.getBib());
        if (usersBib.isEmpty()) {
            throw new MarathonException(ErrorCode.M1);
        }
        log.info("Processing loop for request {}", loopDto);
        Optional<Loop> loop = loopRepo.findFirst1ByBibOrderByLoopDesc(loopDto.getBib());
        if (loop.isEmpty()) {
            Loop l = loopRepo.save(Loop.builder()
                    .bib(loopDto.getBib())
                    .lat(loopDto.getLat())
                    .longitude(loopDto.getLongitude())
                    .checkpoint(loopDto.getCheckpoint())
                    .loop(1)
                    .build());
            return LapResponse.builder()
                    .finishedLap(1)
                    .timeOfLap(l.getCreatedAt().getTime())
                    .build();
        } else {
            Loop l = loop.get();
            l = loopRepo.save(Loop.builder()
                    .bib(loopDto.getBib())
                    .lat(loopDto.getLat())
                    .loop(l.getLoop() + 1)
                    .longitude(loopDto.getLongitude())
                    .checkpoint(loopDto.getCheckpoint())
                    .build());
            return LapResponse.builder()
                    .finishedLap(l.getLoop())
                    .timeOfLap(l.getUpdatedAt().getTime())
                    .build();
        }

    }

    public BibResponse getStatus(LoopDto loopDto) {
        log.info("Processing bibs for request {}", loopDto);
        Optional<Marathon> m = marathonRepo.findById(loopDto.getMarathonId());
        if (m.isPresent()) {
            List<MarathonResponse> marathonResponses = loopRepo.findStatus();
            Map<Integer, BibResponse.LoopResp> response = new HashMap<>();
            marathonResponses.forEach(resp -> {
                if (response.containsKey(resp.getBib())) {
                    BibResponse.LoopResp r = response.get(resp.getBib());
                    Loop l = Loop.builder()
                            .loop(resp.getLoop())
                            .createdAt(resp.getCreated_at())
                            .bib(resp.getBib())
                            .lat(resp.getLat())
                            .longitude(resp.getLongitude())
                            .checkpoint(resp.getCheckpoint())
                            .build();
                    r.getLoops().add(l);
                } else {
                    List<Loop> list = null;
                    if (resp.getLoop() != null) {
                        Loop l = Loop.builder()
                                .loop(resp.getLoop())
                                .createdAt(resp.getCreated_at())
                                .bib(resp.getBib())
                                .lat(resp.getLat())
                                .longitude(resp.getLongitude())
                                .checkpoint(resp.getCheckpoint())
                                .build();
                        list = new ArrayList<>();
                        list.add(l);
                    }
                    if (checkEvent(loopDto.getEvent(), resp.getEname())) {
                        response.put(resp.getBib(), new BibResponse.LoopResp(resp.getUname(), resp.getEname(), resp.getBib(), resp.getAwarded(), list));
                    }
                }

            });
            return BibResponse.builder().loops(response).build();
        } else throw new MarathonException(ErrorCode.M2);

    }

    private boolean checkEvent(String event, String ename) {
        return ((event != null && event.equalsIgnoreCase(ename)) || (event!=null && event.equalsIgnoreCase("All events")));
    }
  /*
    public BibResponse getBibs(LoopDto loopDto) {

        log.info("Processing bibs for request {}", loopDto);
        Optional<Marathon> m = marathonRepo.findById(loopDto.getMarathonId());
        if (m.isPresent()) {
            if (loopDto.getBib() != null) {
                List<Loop> loops = loopRepo.findByBib(loopDto.getBib());
                Optional<UsersBib> bib = usersBibRepo.findByBib(loopDto.getBib());
                if (bib.isPresent()) {
                    Optional<Users> u = usersRepo.findById(bib.get().getUserId());
                    if (u.isPresent()) {
                        return BibResponse.builder()
                                .loops(Map.of(bib.get().getBib(), new BibResponse.LoopResp(u.get().getName(), loops)))
                                .build();
                    } else throw new MarathonException(ErrorCode.M1);
                } else throw new MarathonException(ErrorCode.M1);
            }
            List<Event> events = eventRepo.findByMarathonId(m.get().getId());
            List<UsersBib> users = usersBibRepo.findByEventIdIn(events.stream().map(e-> e.getId()).toList());
            List<Users> us = usersRepo.findByIdIn(users.stream().map(u -> u.getUserId()).toList());
            Map<Integer, String> userNameMap = us.stream().map(u -> new AbstractMap.SimpleEntry<>(u.getId(), u.getName()))
                    .collect(Collectors.toMap(m1 -> m1.getKey(), m1 -> m1.getValue()));

            Map<Integer, String> bibToUsersMap = users.stream()
                    .map(u -> new AbstractMap.SimpleEntry<>(u.getBib(), userNameMap.get(u.getUserId())))
                    .collect(Collectors.toMap(m1 -> m1.getKey(), m1 -> m1.getValue()));

            List<Loop> loops = loopRepo.findByBibIn(users.stream().map(u -> ((Integer) u.getBib()))
                    .collect(Collectors.toList()));
            Map<Integer, List<Loop>> bibToLoopMap = new HashMap<>();
            loops.stream().forEach(loop -> {
                if(bibToLoopMap.containsKey(loop.getBib())) {
                    bibToLoopMap.get(loop.getBib()).add(loop);
                } else {
                    List<Loop> list = new ArrayList<>();
                    list.add(loop);
                    bibToLoopMap.put(loop.getBib(), list);
                }
            });

            Map<Integer, BibResponse.LoopResp> response = users.stream()
                    .map(e->  new AbstractMap.SimpleEntry<>(e.getBib(), new BibResponse.LoopResp(bibToUsersMap.get(e.getBib()), bibToLoopMap.get(e.getBib()))))
                    .collect(Collectors.toMap(m1-> m1.getKey(), m1-> m1.getValue()));

            return BibResponse.builder().loops(response).build();
        } else throw new MarathonException(ErrorCode.M2);
    }*/
}
