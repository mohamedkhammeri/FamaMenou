package com.project.famaMenouApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestAlertException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String entityName;
    private final String errorKey;
    private final String detailMessage;

    /**
     * Constructor for simple error messages (as used in your controller)
     */
    public BadRequestAlertException(String detailMessage) {
        this(detailMessage, "product", "badRequest");
    }

    /**
     * Full constructor with all parameters
     */
    public BadRequestAlertException(String detailMessage, String entityName, String errorKey) {
        super(detailMessage);
        this.detailMessage = detailMessage;
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    @Override
    public String getMessage() {
        return detailMessage;
    }
}