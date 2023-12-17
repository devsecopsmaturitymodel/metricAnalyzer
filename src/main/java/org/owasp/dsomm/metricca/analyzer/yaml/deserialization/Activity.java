package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DateComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Activity {
  private static final Logger logger = LoggerFactory.getLogger(Activity.class);

  private String level;
  private String activityName;


  @JsonIgnore
  private final Map<String, Object> skeleton;
  private final ArrayList<Map<String, Object>> content;

  public Activity() {
    skeleton = new HashMap<>();
    content = new ArrayList<>();
  }

  public String getLevel() {
    return this.level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getActivityName() {
    return this.activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  public void addComponentToSkeleton(Component component, ArrayList<String> nester) {
    if (nester.isEmpty()) {
      skeleton.put(component.getName(), component);
    } 
    else 
    {
      for (String key : nester) // TODO: Should be fully recursive 
      {
        if (skeleton.get(nester.get(0)) != null) // The nester can have multiple components. E.g. links has title StringComponent and url StringComponent --> links={title=StringComponent[name=title, value=null], url=StringComponent[name=url, value=null]}
        { 
          Map<String, Object> temp = (HashMap) skeleton.get(key);
          temp.put(component.getName(), component);
        } 
        else 
        {
          Map<String, Object> temp = new HashMap<>(); // HashMap for nester does not exist
          temp.put(component.getName(), component);
          skeleton.put(key, temp);
        }
      }
    }
  }

  public Map<String, Object> getSkeletons() {
    return this.skeleton;
  }

  // Clones components from the 'skeleton' map and adds them to the 'content' ArrayList.
  // Components are added either directly or within nested HashMaps, preserving the original structure.
  public void cloneSkeletonAndAddToContent() {
    HashMap finalAcc = new HashMap<>();
    for (String componentKey : skeleton.keySet()) {
      if (skeleton.get(componentKey) instanceof Component) {
        try {
          Component component = (Component) skeleton.get(componentKey);
          finalAcc.put(componentKey, component.clone());
        } catch (CloneNotSupportedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else if (skeleton.get(componentKey) instanceof HashMap fin) {
        HashMap temp = new HashMap<>();
        for (Object k : fin.keySet()) {
          Component component = (Component) fin.get(k);
          try {
            temp.put(k, component.clone());
          } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        finalAcc.put(componentKey, temp);
      } else {
        // TODO Raise an exception
        System.out.println("This instance should not be here!");
      }
    }
    content.add(finalAcc);
  }

  public ArrayList<Map<String, Object>> getContent() {
    return this.content;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[name=" + activityName + ", level=" + level + "]";
  }

  // remember: including DatePeriodComponent
  public Collection<DateComponent> getDateComponents() {
    Collection<DateComponent> dateComponents = new ArrayList<>();
    for (Map<String, Object> componentMap : content) {
      for (Object component : componentMap.values()) {
        if (component instanceof DateComponent) {
          dateComponents.add((DateComponent) component);
        }
      }
    }
    Comparator<DateComponent> dateComparator = (date1, date2) -> {
      return date1.getValue().compareTo(date2.getValue());
    };
    Collections.sort((ArrayList<DateComponent>) dateComponents, dateComparator);
    return dateComponents;
  }

  public DateComponent getMatchingDatePeriodComponent(Date givenDate) {
    for (DateComponent dateComponent : this.getDateComponents()) {
      if (dateComponent.getValue().equals(givenDate)) {
        return dateComponent;
      }
    }
    return null;
  }

  public DateComponent getClosestBeforeDatePeriodComponent(Date date) {
    DateComponent dateComponentToReturn = null;
    for (DateComponent dateComponent : this.getDateComponents()) {
      if (dateComponent.getValue().before(date)) {
        dateComponentToReturn = dateComponent;
      }
    }
    return dateComponentToReturn;
  }
}
