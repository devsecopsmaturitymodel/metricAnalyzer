package com.analyzer;

import java.util.List;

public interface Component {
    String getName();

    void setName(String newName);

    // The list can hold elements of any type.
    List<?> getValues();
}
