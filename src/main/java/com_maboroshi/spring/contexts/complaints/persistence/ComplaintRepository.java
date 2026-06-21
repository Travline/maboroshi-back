package com_maboroshi.spring.contexts.complaints.persistence;

import com_maboroshi.spring.contexts.complaints.models.Complaint;

import java.util.Optional;

public interface ComplaintRepository {

    Optional<Complaint> saveComplaint(
            Complaint complaint
    );
}