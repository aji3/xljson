package org.xlbean.xljson.builder;

import org.xlbean.XlBean;
import org.xlbean.util.XlBeanFactory;

public class FlexXlBeanFactory extends XlBeanFactory {

    @Override
    public XlBean createBean() {
        return new FlexXlBean();
    }

}
