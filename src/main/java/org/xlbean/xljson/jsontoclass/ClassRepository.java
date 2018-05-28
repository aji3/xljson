package org.xlbean.xljson.jsontoclass;

import java.util.HashMap;
import java.util.Map;

import org.xlbean.xljson.jsontoclass.dto.JsonClass;

public class ClassRepository {

    private static ClassRepository instance = new ClassRepository();

    private ClassRepository() {}

    public static ClassRepository getInstance() {
        return instance;
    }

    @SuppressWarnings("serial")
    private Map<String, JsonClass> classes = new HashMap<String, JsonClass>() {
        {
            put("UNKNOWN", new JsonClass("UNKNOWN"));
            put("Double", new JsonClass("Double"));
            put("Integer", new JsonClass("Integer"));
            put("String", new JsonClass("String"));
            put("Boolean", new JsonClass("Boolean"));
        }
    };

    public void addClass(JsonClass jsonClass) {
        classes.put(jsonClass.getName(), jsonClass);
    }

    public JsonClass getClass(String className) {
        JsonClass classObject = classes.get(className);
        if (classObject == null) {
            classObject = new JsonClass(className);
            classes.put(className, classObject);
        }
        return classObject;
    }

    @Override
    public String toString() {
        return "Context [classes=" + classes + "]";
    }

    public Map<String, JsonClass> getClasses() {
        return classes;
    }
}
