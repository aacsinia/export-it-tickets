package com.nudgeit.rest.error;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

/**
 * Handle rest controllers exceptions
 */
@ControllerAdvice(basePackages = "com.nudgeit.rest")
class RestExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors notFoundExceptionHandler(NotFoundException e) {
        return new VndErrors("error", e.getMessage());
    }
}
