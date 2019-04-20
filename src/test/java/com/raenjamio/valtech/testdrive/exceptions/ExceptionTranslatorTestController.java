package com.raenjamio.valtech.testdrive.exceptions;

import java.sql.SQLException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
public class ExceptionTranslatorTestController {

    @GetMapping("/test/concurrency-failure")
    public void concurrencyFailure() {
        throw new ConcurrencyFailureException("test concurrency failure");
    }

    @PostMapping("/test/method-argument")
    public void methodArgument(@Valid @RequestBody TestDTO testDTO) {
    }

    
    @GetMapping("/test/type-mismatch")
    public void typeMismatchException(@RequestParam String param) {
    	throw new TypeMismatchException("value", String.class);
    }
    
    @GetMapping("/test/type-bind")
    public void bindException(@RequestParam String param) throws org.springframework.validation.BindException {
    	throw new org.springframework.validation.BindException(String.class, "test");
    }
    
    @GetMapping("/test/missing-servlet-request-part")
    public void missingServletRequestPartException(@RequestParam String param) throws MissingServletRequestPartException {
    	throw new MissingServletRequestPartException("test");
    }
    
    @GetMapping("/test/constraint")
    public void constraintViolationException(@RequestParam String param) {
    	throw new ConstraintViolationException("Error", new SQLException(), "constraint");
    }
    
    
    @GetMapping("/test/handleNoHandlerFoundException")
    public void handleNoHandlerFoundException(@RequestParam String param) throws NoHandlerFoundException {
    	throw new NoHandlerFoundException("test", "/test/handleNoHandlerFoundException", new HttpHeaders());
    }
    
    
    @GetMapping("/test/httpRequestMethodNotSupportedException")
    public void httpRequestMethodNotSupportedException(@RequestParam String param) throws HttpRequestMethodNotSupportedException {
    	throw new HttpRequestMethodNotSupportedException("test");
    }
    
    
    @GetMapping("/test/handleHttpMediaTypeNotSupported")
    public void handleHttpMediaTypeNotSupported(@RequestParam String param) throws HttpMediaTypeNotSupportedException {
    	throw new HttpMediaTypeNotSupportedException("error");
    }
    
    
    @GetMapping("/test/transactionSystemException")
    public void transactionSystemException(@RequestParam String param) {
    	throw new TransactionSystemException("error");
    }
    
    @GetMapping("/test/badRequestAlertException")
    public void adRequestAlertException(@RequestParam String param) {
    	throw new BadRequestAlertException("error", "test", "test");
    }
    

    @GetMapping("/test/missing-servlet-request-parameter")
    public void missingServletRequestParameterException(@RequestParam String param) {
    }

    @GetMapping("/test/access-denied")
    public void accessdenied() {
        throw new AccessDeniedException("test access denied!");
    }

    @GetMapping("/test/unauthorized")
    public void unauthorized() {
        throw new BadCredentialsException("test authentication failed!");
    }

    @GetMapping("/test/response-status")
    public void exceptionWithReponseStatus() {
        throw new TestResponseStatusException();
    }

    @GetMapping("/test/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }
    
    
    @GetMapping("/test/exception")
    public void exception() throws Exception {
        throw new Exception();
    }

    public static class TestDTO {

        @NotNull
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "test response status")
    @SuppressWarnings("serial")
    public static class TestResponseStatusException extends RuntimeException {
    }

}
