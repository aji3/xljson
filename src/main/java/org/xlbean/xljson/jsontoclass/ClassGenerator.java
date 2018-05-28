package org.xlbean.xljson.jsontoclass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.xlbean.xljson.jsontoclass.dto.JsonClass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
public class ClassGenerator {

    public static void main(String[] args) {
        new ClassGenerator().run();
    }

    private static Map<String, String> JSON_TYPE_MAPPER = new HashMap<String, String>() {
        {
            put("VALUE_NULL", "UNKNOWN");
            put("VALUE_NUMBER_FLOAT", "Double");
            put("VALUE_NUMBER_INT", "Integer");
            put("VALUE_STRING", "String");
            put("VALUE_TRUE", "Boolean");
            put("VALUE_FALSE", "Boolean");
        }
    };

    private String rootClassName = "Root";

    public void run() {
        try {
            Files.lines(Paths.get("fields.txt")).forEach(this::parseLine);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeToJson(ClassRepository.getInstance().getClasses().values());
    }

    private void writeToJson(Object result) {
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            System.out.println(mapper.writeValueAsString(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseLine(String lineStr) {

        Line line = new Line(lineStr);

        String[] splittedFields = line.getNestedField().split("\\.");
        for (int i = 0; i < splittedFields.length; i++) {
            String className = getClassName(splittedFields, i);
            String fieldName = splittedFields[i];
            String fieldType = getFieldType(splittedFields, i, fieldName, line.getFieldType());
            String sampleValue = getSampleValue(splittedFields, i, line.getSampleValue());

            JsonClass jsonClass = createJsonClass(className, fieldName, fieldType, sampleValue);
            ClassRepository.getInstance().addClass(jsonClass);
        }
    }

    private String getClassName(String[] splittedFields, int i) {
        if (i == 0) {
            return rootClassName;
        } else {
            return splittedFields[i - 1];
        }
    }

    private String getFieldType(String[] splittedFields, int i, String fieldName, String valueType) {
        if (i < splittedFields.length - 1) {
            return fieldName;
        } else {
            return JSON_TYPE_MAPPER.get(valueType);
        }
    }

    private String getSampleValue(String[] splittedFields, int i, String sampleValue) {
        if (i < splittedFields.length - 1) {
            return null;
        } else {
            return sampleValue;
        }
    }

    private JsonClass createJsonClass(String className, String fieldName, String fieldType, String sampleValue) {
        boolean isFieldList = false;
        String tmpClassName = className.substring(0, 1).toUpperCase() + className.substring(1);
        if (tmpClassName.contains("[")) {
            tmpClassName = tmpClassName.substring(0, tmpClassName.indexOf('['));
            // singularize

        }
        String tmpFieldName = fieldName;
        if (tmpFieldName.contains("[")) {
            tmpFieldName = tmpFieldName.substring(0, tmpFieldName.indexOf('['));
            isFieldList = true;
        }
        String tmpFieldType = fieldType.substring(0, 1).toUpperCase() + fieldType.substring(1);
        if (tmpFieldType.contains("[") || isFieldList) {
            if (tmpFieldType.contains("[")) {
                tmpFieldType = tmpFieldType.substring(0, tmpFieldType.indexOf("["));
            }
            tmpFieldType = String.format("List<%s>", tmpFieldType);
            // singularize
        }
        JsonClass classObject = ClassRepository.getInstance().getClass(tmpClassName);
        classObject.setField(tmpFieldType, tmpFieldName, sampleValue);
        return classObject;
    }

    private class Line {
        private String nestedField;
        private String fieldType;
        private String sampleValue;

        public Line(String line) {
            String[] lineAry = line.split("\t");
            nestedField = lineAry[0];
            fieldType = lineAry[1];
            sampleValue = lineAry[2];
        }

        public String getNestedField() {
            return nestedField;
        }

        public String getFieldType() {
            return fieldType;
        }

        public String getSampleValue() {
            return sampleValue;
        }
    }

}
