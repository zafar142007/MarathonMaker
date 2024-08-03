package com.eagersphinx.controller;

import com.eagersphinx.domain.dto.LoopDto;
import com.eagersphinx.domain.dto.Response;
import com.eagersphinx.domain.exception.MarathonException;
import com.eagersphinx.service.LoopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("marathon-maker/v1")
@Slf4j
public class LoopController {

    @Autowired
    private LoopService loopService;

    @PostMapping("/bib/lap")
    public ResponseEntity<Response> recordLap(@RequestBody final LoopDto loopDto) {
        try {
            return ResponseEntity.ok().body(loopService.incrementLoop(loopDto));
        } catch (MarathonException e) {
            log.error("error", e);
            return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(Response.builder()
                    .errorMessage(e.getErrorCode().getMsg())
                    .build());
        } catch (Exception e) {
            log.error("error", e);
            return ResponseEntity.status(500).body(Response.builder()
                    .errorMessage("Something unexpected happened").build());
        }
    }

    @PostMapping("/bib")
    public ResponseEntity<Response> getBibs(@RequestBody final LoopDto loopDto) {
        try {
            return ResponseEntity.ok().body(loopService.getStatus(loopDto));
        } catch (MarathonException e) {
            log.error("error", e);
            return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(Response.builder()
                    .errorMessage(e.getErrorCode().getMsg())
                    .build());
        } catch (Exception e) {
            log.error("error", e);
            return ResponseEntity.status(500).body(Response.builder()
                    .errorMessage("Something unexpected happened").build());
        }
    }
}
