package com.app.model;

/**
 * Enumeration representing the status of a process or request.
 */
public enum Status {
    /**
     * The process or request has been rejected.
     */
    REJECTED,

    /**
     * The process or request has been accepted.
     */
    ACCEPTED,

    /**
     * The process or request is currently being processed.
     */
    PROCESSING,
}
