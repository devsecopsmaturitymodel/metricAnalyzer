package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

public interface Component<T> extends Cloneable {

  Object clone() throws CloneNotSupportedException;

  String getName();

  void setName(String newName);

  // The list can hold elements of any type.
  T getValue();

  void setValue(Object value);
}
