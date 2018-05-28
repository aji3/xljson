package org.xlbean.xljson.builder;

import org.xlbean.XlBean;

@SuppressWarnings("serial")
public class FlexXlBean extends XlBean {

    @Override
    protected boolean canPut(Object value) {
        return true;
    }

}
