package com_maboroshi.spring.contexts.complaints.services;

import com_maboroshi.spring.contexts.complaints.errors.CannotCreateComplaint;
import com_maboroshi.spring.contexts.complaints.models.Complaint;
import com_maboroshi.spring.contexts.complaints.persistence.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateComplaint {

    private final ComplaintRepository repository;

    public Complaint execute(Complaint complaint) {

        return repository
                .saveComplaint(complaint)
                .orElseThrow(() ->
                        new CannotCreateComplaint(
                                "Cannot create complaint"
                        )
                );
    }
}