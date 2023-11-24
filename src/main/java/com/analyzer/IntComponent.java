package com.analyzer;

public class IntComponent implements Component<Integer> {
    private String name;
    private int value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        int s = (Integer) value;
        this.value = s;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  // Performs a shallow copy
    }
}
