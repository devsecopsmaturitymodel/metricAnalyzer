package com.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DateComponent implements Component<LocalDate> {
    
    private String name;
    private List<LocalDate> values;

    // Default constructor initializes the values list
    public DateComponent() {
        this.values = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public List<LocalDate> getValues() {
        return this.values;
    }

    public void setValue(LocalDate value) {
        this.values.add(value);
    }
    
}
