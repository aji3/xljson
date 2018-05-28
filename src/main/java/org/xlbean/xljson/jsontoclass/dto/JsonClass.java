package org.xlbean.xljson.jsontoclass.dto;

import java.util.ArrayList;
import java.util.List;

import org.xlbean.xljson.jsontoclass.ClassRepository;

public class JsonClass {

    private String name;
    private List<JsonField> fields;

    public JsonClass(String className) {
        this.name = className;
    }

    public void setField(String type, String fieldName, String sampleValue) {
        JsonField field = getField(fieldName);
        if (field == null) {
            field = new JsonField(ClassRepository.getInstance().getClass(type), fieldName);
            if (fields == null) {
                fields = new ArrayList<>();
            }
            fields.add(field);
        }
        if (sampleValue != null) {
            field.addSampleValue(sampleValue);
        }
    }

    private JsonField getField(String fieldName) {
        if (fields == null) {
            return null;
        }
        return fields.stream().filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "Class [name=" + name + ", fields=" + fields + "]";
    }

    public String getName() {
        return name;
    }

    public List<JsonField> getFields() {
        return fields;
    }
}
