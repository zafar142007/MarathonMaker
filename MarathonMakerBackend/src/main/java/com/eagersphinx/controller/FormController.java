package com.eagersphinx.controller;

import com.eagersphinx.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("marathon-maker/v1")
public class FormController {

    @Autowired
    private FormService formService;

    @PostMapping("/marathon/{marathon}/form")
    public ResponseEntity ingestData(@PathVariable("marathon") String marathon) throws Exception {
        formService.ingest(marathon);
        return ResponseEntity.ok().build();
    }

}
