package com.analyzer;

import java.util.List;

public interface Component<T> {
    String getName();

    void setName(String newName);

    // The list can hold elements of any type.
    List<?> getValues();

    void setValue(T value);
}
