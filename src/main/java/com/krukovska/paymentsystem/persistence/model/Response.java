package com.krukovska.paymentsystem.persistence.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response<T> {

    private T object;
    private List<String> errors = new ArrayList<>();

    public Response(T object) {
        this.object = object;
    }

    public Response() {

    }

    public Response(List<String> errors) {
        this.errors = errors;
    }

    public Response(T object, List<String> errors) {
        this.object = object;
        this.errors = errors;
    }

    public Response(T object, String error) {
        this.object = object;
        this.errors = Collections.singletonList(error);
    }

    public Response(String error) {
        this.errors = Collections.singletonList(error);
    }

    public boolean isOkay() {
        return errors.isEmpty();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }


    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
