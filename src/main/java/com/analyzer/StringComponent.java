package com.analyzer;

import java.util.ArrayList;
import java.util.List;

public class StringComponent implements Component<String>{
    private String name;
    private List<String> values;

    // Default constructor initializes the values list
    public StringComponent() {
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
    public List<String> getValues() {
        return this.values;
    }

    @Override
    public void setValue(String value) {
        this.values.add(value);
    }
    
}
