package com.krukovska.paymentsystem.persistence;

import java.util.Objects;

public class Field {
    private final String name;
    private final Object value;

    public Field(String name, Object value) {
        Objects.requireNonNull(name, "Field name must be not null");
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Field " + name + '=' + value;
    }
}