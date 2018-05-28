package org.xlbean.util;

import java.util.regex.Pattern;

import org.junit.Test;
import org.xlbean.XlBean;
import org.xlbean.xljson.builder.FlexXlBean;

public class FieldAccessHelperTest {

    private static final Pattern INDEX = Pattern.compile("\\[([0-9]+)\\]");

    @Test
    public void test() {

        XlBean target = new FlexXlBean();
        FieldAccessHelper.setValue("aaa", "aaa", target);
        FieldAccessHelper.setValue("bbb.b1", "b1", target);
        FieldAccessHelper.setValue("bbb.b2", "b2", target);
        FieldAccessHelper.setValue("ccc.list[2]", "c2", target);
        FieldAccessHelper.setValue("ccc.list[4]", "c4", target);
        FieldAccessHelper.setValue("list[1]", "l2", target);
        FieldAccessHelper.setValue("list[0]", "l1", target);
        FieldAccessHelper.setValue("listinlist[1][2]", "1-2", target);
        FieldAccessHelper.setValue("listinlist[1][4]", "1-4", target);
        FieldAccessHelper.setValue("listinlist[2][0]", "2-0", target);
        FieldAccessHelper.setValue("listinlist[2][2]", "2-2", target);

        System.out.println(target);
    }
}
