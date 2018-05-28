package org.xlbean.xljson.jsontoclass.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonField {
    private String name;
    private JsonClass type;
    private List<String> sampleValues = new ArrayList<>();

    public JsonField(JsonClass type, String name) {
        this.name = name;
        this.type = type;
    }

    public void addSampleValue(String sampleValue) {
        sampleValues.add(sampleValue);
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public JsonClass getType() {
        return type;
    }

    public String getTypeName() {
        return type.getName();
    }

    @Override
    public String toString() {
        return "Field [name=" + name + ", type=" + type + ", sampleValues=" + sampleValues + "]";
    }

    public List<String> getSampleValues() {
        return sampleValues;
    }

}
