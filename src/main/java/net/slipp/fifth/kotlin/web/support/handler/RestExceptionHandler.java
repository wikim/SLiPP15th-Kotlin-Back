package net.slipp.fifth.kotlin.web.support.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ControllerAdvice(annotations = { RestController.class })
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<?> handleException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request);
        return ResponseEntity.status(status).body(new CustomErrorType(status, status.value(), ex.getMessage()));
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomErrorType {
        private HttpStatus status;
        private int statusValue;
        private String message;
    }
}
