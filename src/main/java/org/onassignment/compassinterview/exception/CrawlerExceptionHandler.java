package org.onassignment.compassinterview.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.onassignment.compassinterview.pojo.ResultOfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;
import java.io.IOException;

@ControllerAdvice
public class CrawlerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerExceptionHandler.class);

    @ExceptionHandler({IOException.class,
            MismatchedInputException.class,
            JsonParseException.class,
            JsonMappingException.class,
            PropertyBindingException.class,
            ServletException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultOfException handleClientException(Exception e) {
        ResultOfException errorStatus = new ResultOfException();

        logger.warn("Bad Request occurred, check param and json file url and format");

        if (e instanceof IOException) {
            errorStatus.setRetCode(-100);
            errorStatus.setRetMessage("IO Error, url wrong? " + e.toString());
        } else if (e instanceof ServletException) {

            errorStatus.setRetCode(-101);
            errorStatus.setRetMessage("parameter is wrong. " + e.toString());

        } else {
            errorStatus.setRetCode(-102);
            errorStatus.setRetMessage("Json file is not correct." + e.toString());

        }

        return errorStatus;

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultOfException handleServerException(Exception e) {
        ResultOfException errorStatus = new ResultOfException();

        logger.warn(" server error " + e.toString());

        if (e instanceof InterruptedException) {
            errorStatus.setRetCode(-200);
            errorStatus.setRetMessage("Thread been Interrupt." + e.toString());
        } else {
            errorStatus.setRetCode(-201);
            errorStatus.setRetMessage("General Error occurred " + e.toString());
        }

        return errorStatus;

    }
}
