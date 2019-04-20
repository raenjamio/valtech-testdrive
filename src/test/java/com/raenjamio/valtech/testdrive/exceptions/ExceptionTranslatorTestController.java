package com.raenjamio.valtech.testdrive.exceptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public void typeMismatchException() {
    	throw new TypeMismatchException("value", String.class);
    }
    
    @GetMapping("/test/type-bind")
    public void bindException() throws org.springframework.validation.BindException {
    	throw new org.springframework.validation.BindException (String.class, "test");
    }
    
    @GetMapping("/test/missing-servlet-request-part")
    public void missingServletRequestPartException() throws MissingServletRequestPartException {
    	throw new MissingServletRequestPartException("test");
    }
    
    @GetMapping("/test/methodArgumentTypeMismatchException")
    public void methodArgumentTypeMismatchException(@RequestParam Long param) {
    }
    
    @GetMapping("/test/constraint")
    public void constraintViolationException() {
    	Set<ConstraintViolation> contranint = new HashSet();
    	throw new javax.validation.ConstraintViolationException("test", new HashSet());
    }
    
    
    @GetMapping("/test/handleNoHandlerFoundException")
    public void handleNoHandlerFoundException() throws NoHandlerFoundException {
    	throw new NoHandlerFoundException("test", "/test/handleNoHandlerFoundException", new HttpHeaders());
    }
    
    
    @GetMapping("/test/httpRequestMethodNotSupportedException")
    public void httpRequestMethodNotSupportedException() throws HttpRequestMethodNotSupportedException {
    	Set<String> methods = new HashSet<>();
    	methods.add(HttpMethod.GET.toString());
		throw new HttpRequestMethodNotSupportedException("test", methods);
    }
    
    
    @GetMapping("/test/handleHttpMediaTypeNotSupported")
    public void handleHttpMediaTypeNotSupported() throws HttpMediaTypeNotSupportedException {
    	List<MediaType> mediaTypes = new ArrayList();
    	mediaTypes.add(MediaType.APPLICATION_JSON);
		MediaType contenType = MediaType.APPLICATION_FORM_URLENCODED;
		throw new HttpMediaTypeNotSupportedException(contenType, mediaTypes);
    }
    
    
    @GetMapping("/test/transactionSystemException")
    public void transactionSystemException() {
    	Throwable cause = new Throwable();
		throw new TransactionSystemException("test", cause);
    }
    
    @GetMapping("/test/badRequestAlertException")
    public void adRequestAlertException() {
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
