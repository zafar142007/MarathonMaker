package com.eagersphinx.service;

import com.eagersphinx.domain.Util;
import com.eagersphinx.domain.dto.Response;
import com.eagersphinx.domain.entity.Event;
import com.eagersphinx.domain.entity.Marathon;
import com.eagersphinx.domain.entity.Users;
import com.eagersphinx.domain.entity.UsersBib;
import com.eagersphinx.repo.EventRepo;
import com.eagersphinx.repo.MarathonRepo;
import com.eagersphinx.repo.UsersBibRepo;
import com.eagersphinx.repo.UsersRepo;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.eagersphinx.domain.constants.Constants.*;

@Service
@Slf4j
public class FormService {

    @Autowired
    private UsersBibRepo usersBibRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private MarathonRepo marathonRepo;
    @Autowired
    private UsersRepo usersRepo;

    private Map<String, Integer> headingsMap = Map.of(
            NAME, 4,
            ADDRESS, 3,
            EVENT, 8,
            BIB, 7
    );

    public void ingest(String marathon) throws Exception {
        Path path = Paths.get(
                ClassLoader.getSystemResource("form_data.csv").toURI());
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();
        try (Reader reader = Files.newBufferedReader(path)) {
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build();

            List<String[]> data = csvReader.readAll();
            Marathon m = marathonRepo.save(Marathon.builder().name(marathon).build());

            Set<String> events = getAllDistinctValues(data.subList(1, data.size()), headingsMap.get(EVENT));
            Map<String, Integer> eventIdMap = new HashMap<>();
            Map<String, Integer> userIdMap = new HashMap<>();

            saveEvents(m, events, eventIdMap);
            saveUsers(data, userIdMap);
            saveBibs(data, eventIdMap, userIdMap);
        }
    }

    private void saveBibs(List<String[]> data, Map<String, Integer> eventIdMap, Map<String, Integer> userIdMap) {
        for (String[] row : data.subList(1, data.size())) {
            try {
                usersBibRepo.save(UsersBib.builder()
                        .bib(Integer.parseInt(row[headingsMap.get(BIB)]))
                        .eventId(eventIdMap.get(Util.getEvent(row[headingsMap.get(EVENT)])))
                        .userId(userIdMap.get(row[headingsMap.get(NAME)]))
                        .build());
            } catch (Exception e) {
                log.error("error while saving row {}", row, e);
            }
        }
    }

    private void saveUsers(List<String[]> data, Map<String, Integer> userIdMap) {
        for (String[] row : data.subList(1, data.size())) {

            Users u = usersRepo.save(Users.builder()
                    .address(row[headingsMap.get(ADDRESS)])
                    .name(row[headingsMap.get(NAME)])
                    .build());
            userIdMap.put(u.getName(), u.getId());
        }
    }

    private void saveEvents(Marathon m, Set<String> events, Map<String, Integer> eventIdMap) {
        for (String event : events) {
            event = Util.getEvent(event);
            Event e = eventRepo.save(Event.builder()
                    .marathonId(m.getId())
                    .name(event)
                    .build());
            eventIdMap.put(event, e.getId());
        }
    }

    private List<String> getAllValues(List<String[]> data, int column) {
        return data.stream().map(row -> row[column])
                .filter(value -> value != null && !value.isEmpty()).collect(Collectors.toList());
    }

    private Set<String> getAllDistinctValues(List<String[]> data, int column) {
        return data.stream().map(row -> row[column])
                .filter(value -> value != null && !value.isEmpty()).collect(Collectors.toSet());
    }

}
