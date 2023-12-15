package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DatePeriodComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.IntComponent;


public interface Component<T> extends Cloneable {

  Object clone() throws CloneNotSupportedException;

  String getName();

  void setName(String newName);

  // The list can hold elements of any type.
  T getValue();

  void setValue(Object value);
}
