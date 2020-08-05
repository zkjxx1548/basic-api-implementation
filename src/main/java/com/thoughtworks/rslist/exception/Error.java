package com.thoughtworks.rslist.exception;

import lombok.Data;

public class Error {
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
