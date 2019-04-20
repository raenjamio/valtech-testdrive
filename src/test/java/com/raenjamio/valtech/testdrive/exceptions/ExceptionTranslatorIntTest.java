package com.raenjamio.valtech.testdrive.exceptions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zalando.problem.Status;

import com.raenjamio.valtech.testdrive.TestDriveApplication;
import com.raenjamio.valtech.testdrive.error.ErrorConstants;



/**
 * Test class for the ExceptionTranslator controller advice.
 *
 * @see ExceptionTranslator
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDriveApplication.class)
public class ExceptionTranslatorIntTest {

    @Autowired
    private ExceptionTranslatorTestController controller;

    @Autowired
    private CustomRestExceptionHandler exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }
    
    @Test
    public void testConstraint() throws Exception {
         mockMvc.perform(get("/test/constraint").content("{}").contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isBadRequest())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
             .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()))
             .andExpect(jsonPath("$.message").value("test"));
    }

    @Test
    public void testMethodArgumentNotValid() throws Exception {
         mockMvc.perform(post("/test/method-argument").content("{}").contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isBadRequest())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
             .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()))
             .andExpect(jsonPath("$.errors.[0]").value("test: Este campo no puede ser nulo"));
    }
    
    
    @Test
    public void testMethodTypeMismacth() throws Exception {
         mockMvc.perform(get("/test/type-mismatch").content("{}").contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isBadRequest())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
             .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()));
    }
    
    @Test
    public void testBind() throws Exception {
         mockMvc.perform(get("/test/type-bind").content("{}").contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isBadRequest())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
             .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()));
    }
    /*
    @Test
    public void methodArgumentTypeMismatchException() throws Exception {
         mockMvc.perform(get("/test/methodArgumentTypeMismatchException/aa").content("{}").contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isBadRequest())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
             .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()))
             .andExpect(jsonPath("$.errors.[0]").value("param parameter is missing"));
    }
	*/

    @Test
    public void testMissingServletRequestPartException() throws Exception {
        mockMvc.perform(get("/test/missing-servlet-request-part"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()))
            .andExpect(jsonPath("$.errors.[0]").value("test part is missing"));
    }
    
    @Test
    public void handleNoHandlerFoundException() throws Exception {
        mockMvc.perform(get("/test/handleNoHandlerFoundException"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.NOT_FOUND.toString()))
            .andExpect(jsonPath("$.message").value("No handler found for test /test/handleNoHandlerFoundException"))
            .andExpect(jsonPath("$.errors.[0]").value("No handler found for test /test/handleNoHandlerFoundException"));
    }
    
    
    @Test
    public void httpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(get("/test/httpRequestMethodNotSupportedException"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.METHOD_NOT_ALLOWED.toString()))
            .andExpect(jsonPath("$.message").value("Request method 'test' not supported"))
            .andExpect(jsonPath("$.errors.[0]").value("test method is not supported for this request. Supported methods are GET "));
    }
    
    @Test
    public void handleHttpMediaTypeNotSupported() throws Exception {
        mockMvc.perform(get("/test/handleHttpMediaTypeNotSupported"))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.UNSUPPORTED_MEDIA_TYPE.toString()));
    }
    /*
    @Test
    public void transactionSystemException() throws Exception {
        mockMvc.perform(get("/test/transactionSystemException"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()))
            .andExpect(jsonPath("$.errors.[0]").value("param parameter is missing"));
    }
    */
    @Test
    public void badRequestAlertException() throws Exception {
        mockMvc.perform(get("/test/badRequestAlertException"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()))
            .andExpect(jsonPath("$.errors.[0]").value("BAD_REQUEST"));
    }

    @Test
    public void testMissingServletRequestParameterException() throws Exception {
        mockMvc.perform(get("/test/missing-servlet-request-parameter"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.BAD_REQUEST.toString()))
            .andExpect(jsonPath("$.errors.[0]").value("param parameter is missing"));
    }
    
    @Test
    public void exception() throws Exception {
        mockMvc.perform(get("/test/exception"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value(Status.INTERNAL_SERVER_ERROR.toString()))
            .andExpect(jsonPath("$.errors.[0]").value("error occurred"));
    }



}
