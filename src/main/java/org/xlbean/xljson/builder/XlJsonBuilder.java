package org.xlbean.xljson.builder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xlbean.XlBean;
import org.xlbean.converter.ValueConverter;
import org.xlbean.converter.ValueConverterImpl;
import org.xlbean.reader.XlBeanReader;
import org.xlbean.util.FieldAccessHelper;
import org.xlbean.util.XlBeanFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class XlJsonBuilder {

    public void build() {

        XlBeanFactory.setInstance(new FlexXlBeanFactory());

        XlBean data = XlBeanFactory.getInstance().createBean();
        XlBean bean = new XlBeanReader().read(new File("xljson_out.xlsx"));

        ValueConverter converter = new ValueConverterImpl();

        bean.list("fields").forEach(f -> {
            switch (f.value("type")) {
            case "string":
                FieldAccessHelper.setValue(f.value("name"), converter.toObject(f.value("value"), String.class), data);
                break;
            case "array":
                FieldAccessHelper.setValue(f.value("name"), new ArrayList<Object>(), data);
                break;
            case "boolean":
                FieldAccessHelper.setValue(f.value("name"), converter.toObject(f.value("value"), Boolean.class), data);
                break;
            case "float":
                FieldAccessHelper.setValue(f.value("name"), converter.toObject(f.value("value"), Float.class), data);
                break;
            case "int":
                FieldAccessHelper.setValue(f.value("name"), converter.toObject(f.value("value"), Integer.class), data);
                break;
            case "null":
                break;
            case "object":
                FieldAccessHelper.setValue(f.value("name"), XlBeanFactory.getInstance().createBean(), data);
                break;
            }
        });

        StringWriter writer = new StringWriter();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writer, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(writer.toString());
    }
}
