package org.owasp.dsomm.metricCA.analyzer;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectFlattener {

    public static Map<String, Object> flattenObject(Object obj) throws Exception {
        Map<String, Object> flatMap = new HashMap<>();
        flattenHelper(obj, "", flatMap);
        return flatMap;
    }

    private static void flattenHelper(Object obj, String path, Map<String, Object> flatMap) throws Exception {
        for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
            Method getter = pd.getReadMethod();
            if (getter != null && !getter.getName().equals("getClass")) {
                Object value = getter.invoke(obj);
                String currentPath = path.isEmpty() ? pd.getName() : path + "." + pd.getName();

                if (value instanceof Object && !(value instanceof String) && !(value instanceof Number) && !(value instanceof Boolean)) {
                    flattenHelper(value, currentPath, flatMap);
                } else {
                    flatMap.put(currentPath, value);
                }
            }
        }
    }
}
