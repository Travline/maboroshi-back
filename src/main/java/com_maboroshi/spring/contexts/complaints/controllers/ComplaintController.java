package com_maboroshi.spring.contexts.complaints.controllers;

import com_maboroshi.spring.contexts.complaints.models.Complaint;
import com_maboroshi.spring.contexts.complaints.services.CreateComplaint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final CreateComplaint createComplaint;

    @PostMapping
    public ResponseEntity<Complaint> createComplaint(
            @RequestBody Complaint complaint
    ) {

        Complaint response =
                createComplaint.execute(complaint);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}