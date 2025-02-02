package com.pt.productinventory.error;

import com.pt.productinventory.error.exceptions.IllegalParameterException;
import com.pt.productinventory.error.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class, IllegalParameterException.class})
    public ResponseEntity<HttpResponse> handleBadRequestExceptions(Exception e, HttpServletRequest request) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<HttpResponse> handleNotFoundException(ObjectNotFoundException e, HttpServletRequest request) {
        return createHttpResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message, String path) {

        return new ResponseEntity<>(new HttpResponse(
                path, message, httpStatus, httpStatus.value(), LocalDateTime.now()), httpStatus);
    }

    public record HttpResponse(String path, String message, HttpStatus error, Integer status, LocalDateTime timestamp) {
    }
}
