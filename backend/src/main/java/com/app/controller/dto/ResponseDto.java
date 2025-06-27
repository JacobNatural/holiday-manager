package com.app.controller.dto;

/**
 * Generic response wrapper DTO to encapsulate either the successful data
 * or an error message.
 *
 * @param <T> the type of the data returned in the response
 * @param data the successful response data, or null if there is an error
 * @param error the error message, or null if the request was successful
 */
public record ResponseDto<T>(T data, String error) {

    /**
     * Constructs a successful response containing data.
     *
     * @param data the response data
     */
    public ResponseDto(T data) {
        this(data, null);
    }

    /**
     * Constructs an error response containing an error message.
     *
     * @param error the error message
     */
    public ResponseDto(String error) {
        this(null, error);
    }
}
