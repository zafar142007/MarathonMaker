package com.eagersphinx.controller;

import com.eagersphinx.domain.dto.LoopDto;
import com.eagersphinx.domain.dto.Response;
import com.eagersphinx.service.AwardService;
import com.eagersphinx.service.LoopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("marathon-maker/v1")
@Slf4j
public class AwardController {

    @Autowired
    private AwardService awardService;

    @PostMapping("/bib/{bib}/award")
    public ResponseEntity<Response> recordLap(@PathVariable("bib") Integer bib) {
        awardService.markDone(bib);
        return ResponseEntity.ok().body(Response.builder().build());
    }
}
