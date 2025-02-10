package com.owiseman.dataapi.aop.error;

public class ResponseError {
    private String errorMessage;

        public ResponseError(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        // Getter and Setter
        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
}
