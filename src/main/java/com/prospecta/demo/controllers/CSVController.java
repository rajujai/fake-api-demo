package com.prospecta.demo.controllers;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.prospecta.demo.services.CSVService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/csv")
public class CSVController {
    private final CSVService service;

    public CSVController(CSVService service) {
        this.service = service;
    }

    @PostMapping
    public void updateCSV(@RequestParam final MultipartFile file, final HttpServletResponse response) throws IOException, CsvException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=updated_" + file.getResource().getFilename());
        final CSVWriter writer = new CSVWriter(response.getWriter());
        service.updateCSV(file, writer);
    }
}
