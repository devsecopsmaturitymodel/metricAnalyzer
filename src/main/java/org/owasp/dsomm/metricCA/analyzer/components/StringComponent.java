package org.owasp.dsomm.metricCA.analyzer.components;

import jakarta.persistence.Entity;
import org.owasp.dsomm.metricCA.analyzer.Component;

@Entity
public class StringComponent implements Component<String> {
    private String name;
    private String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        String s = (String) value;
        this.value = s;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  // Performs a shallow copy
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + ", value=" + value + "]";
    }
}
