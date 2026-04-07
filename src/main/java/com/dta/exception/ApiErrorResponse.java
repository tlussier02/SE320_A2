package com.dta.exception;

import java.time.Instant;
import java.util.List;

public class ApiErrorResponse {

    private ErrorBody error;

    public ApiErrorResponse(String code, String message, List<FieldDetail> details) {
        this.error = new ErrorBody(code, message, details, Instant.now().toString());
    }

    public ErrorBody getError() {
        return error;
    }

    public void setError(ErrorBody error) {
        this.error = error;
    }

    public static class ErrorBody {

        private String code;
        private String message;
        private List<FieldDetail> details;
        private String timestamp;

        public ErrorBody(
                String code,
                String message,
                List<FieldDetail> details,
                String timestamp) {
            this.code = code;
            this.message = message;
            this.details = details;
            this.timestamp = timestamp;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<FieldDetail> getDetails() {
            return details;
        }

        public void setDetails(List<FieldDetail> details) {
            this.details = details;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class FieldDetail {

        private String field;
        private String message;

        public FieldDetail(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
