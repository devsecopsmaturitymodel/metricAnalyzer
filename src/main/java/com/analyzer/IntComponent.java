package com.analyzer;

import java.util.ArrayList;
import java.util.List;

public class IntComponent implements Component{
    private String name;
    private List<Integer> values;

    // Default constructor initializes the values list
    public IntComponent() {
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
    public List<Integer> getValues() {
        return this.values;
    }

    public void setValue(int value) {
        this.values.add(value);
    }
}
