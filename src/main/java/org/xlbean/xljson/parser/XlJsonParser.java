package org.xlbean.xljson.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xlbean.XlBean;
import org.xlbean.writer.XlBeanWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class XlJsonParser {

    public void run() {
        Context context = new Context();

        JsonFactory factory = new JsonFactory();
        try {
            JsonParser parser = factory.createParser(new File("test.json"));
            JsonToken token = parser.nextToken();
            while (token != null) {
                switch (token) {
                case START_OBJECT:
                    context.addField(context.getCurrentName(), token, "");
                    token = parseObject(parser, context);
                    break;
                case START_ARRAY:
                    context.addField(context.getCurrentName(), token, "");
                    token = parseArray(parser, context);
                    break;
                default:
                    token = parser.nextToken();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        context.finalize();

        context.getFields().forEach(System.out::println);
        XlBean xlbean = new XlBean();
        xlbean.set("fields", context.getFields());

        XlBeanWriter writer = new XlBeanWriter();
        writer.write(new File("xljson.xlsx"), xlbean, new File("xljson_out.xlsx"));
    }

    private JsonToken parseArray(JsonParser parser, Context context) throws IOException {
        JsonToken token = null;
        int index = 0;
        while ((token = parser.nextToken()) != null) {
            context.append("[" + index + "]");
            switch (token) {
            case START_OBJECT:
                context.addField(context.getCurrentName(), token, "");
                token = parseObject(parser, context);
                context.remove("\\[" + index + "\\]");
                break;
            case START_ARRAY:
                context.addField(context.getCurrentName(), token, "");
                token = parseArray(parser, context);
                context.remove("\\[" + index + "\\]");
                break;
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
            case VALUE_NULL:
            case VALUE_TRUE:
            case VALUE_FALSE:
                context.addField(context.getCurrentName(), token, parser.getText());
                context.remove("\\[" + index + "\\]");
                break;
            case END_ARRAY:
                return token;
            default:
                return token;
            }
            index++;
        }
        return token;

    }

    private JsonToken parseObject(JsonParser parser, Context context) throws IOException {
        JsonToken token = null;

        while ((token = parser.nextToken()) != null) {
            if (JsonToken.FIELD_NAME.equals(token)) {
                context.push(parser.getCurrentName());
            } else {
                return token;
            }
            token = parser.nextToken();
            switch (token) {
            case START_OBJECT:
                context.addField(context.getCurrentName(), token, "");
                token = parseObject(parser, context);
                context.pop();
                break;
            case START_ARRAY:
                context.addField(context.getCurrentName(), token, "");
                token = parseArray(parser, context);
                context.pop();
                break;
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
            case VALUE_NULL:
            case VALUE_TRUE:
            case VALUE_FALSE:
                context.addField(context.getCurrentName(), token, parser.getText());
                context.pop();
                break;
            case END_OBJECT:
                context.pop();
                break;
            default:
                return token;
            }

        }
        return token;
    }

    public static class Context {
        private Stack<String> stack = new Stack<>();

        private List<Field> fields = new ArrayList<>();

        public String push(String name) {
            return stack.push(name);
        }

        public String append(String append) {
            return stack.set(stack.size() - 1, stack.peek() + append);
        }

        public String remove(String remove) {
            return stack.set(stack.size() - 1, stack.peek().replaceAll(remove, ""));
        }

        public String pop() {
            return stack.pop();
        }

        public String getCurrentName() {
            return String.join(".", stack);
        }

        public void finalize() {
            fields.remove(0);
        }

        public void addField(String name, JsonToken type, String value) {
            Field field = new Field();
            field.setName(name);
            String typeString = null;
            switch (type) {
            case START_OBJECT:
                typeString = "object";
                break;
            case START_ARRAY:
                typeString = "array";
                break;
            case VALUE_STRING:
                typeString = "string";
                break;
            case VALUE_NUMBER_INT:
                typeString = "int";
                break;
            case VALUE_NUMBER_FLOAT:
                typeString = "float";
                break;
            case VALUE_NULL:
                typeString = "null";
                break;
            case VALUE_TRUE:
            case VALUE_FALSE:
                typeString = "boolean";
                break;
            default:
                // no action
            }
            field.setType(typeString);
            field.setValue(value);
            fields.add(field);
        }

        public List<Field> getFields() {
            return fields;
        }

        @Override
        public String toString() {
            return getCurrentName();
        }
    }

    public static class Field {
        private String name;
        private String type;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String toString() {
            return name + "\t" + type + "\t" + value;
        }
    }

}
