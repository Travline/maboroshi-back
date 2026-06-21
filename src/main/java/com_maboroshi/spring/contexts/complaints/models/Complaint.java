package com_maboroshi.spring.contexts.complaints.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    private UUID id;

    private String fullName;
    private String dni;
    private String email;
    private String phone;

    private String complaintType;

    private String detail;
    private String expectedSolution;
}