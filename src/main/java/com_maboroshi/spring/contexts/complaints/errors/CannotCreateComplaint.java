package com_maboroshi.spring.contexts.complaints.errors;

public class CannotCreateComplaint extends RuntimeException {
    public CannotCreateComplaint(String message) {
        super(message);
    }
}