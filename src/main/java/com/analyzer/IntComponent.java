package com.analyzer;

import java.util.ArrayList;
import java.util.List;

public class IntComponent implements Component<Integer> {
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

    @Override
    public void setValue(Integer value) {
        this.values.add(value);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  // Performs a shallow copy
    }
}
