package com.vicious.viciouscore.common.util.reflect;

import java.lang.reflect.Field;

public class ShadowField<T> {
    private final Field reflectiveField;

    public ShadowField(Class<?> cls, String fieldName) {
        reflectiveField = Reflection.getField(cls, fieldName);
    }

    public T get(Object toAccess) {
        return (T) Reflection.accessField(toAccess, reflectiveField);
    }

    public void set(Object toAccess, T val) {
        Reflection.setField(toAccess, val, reflectiveField);
    }
}
