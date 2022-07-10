package com.piperplatform.pes.commons;

import com.piperplatform.pes.commons.entity.MsQueryCondition;
import com.piperplatform.pes.commons.entity.RdbmsDynamicFieldQueryRequest;
import com.piperplatform.pes.commons.wrapper.MsQueryWrapper;
import com.piperplatform.pes.commons.wrapper.MsUpdateWrapper;
import com.piperplatform.pes.commons.wrapper.entity.WrapperBean;
import com.piperplatform.pes.commons.wrapper.entity.WrapperUpdateBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author shiyoufeng
 * @date 2022/7/10
 */
public class WrapperTest {

    void test() throws InstantiationException, IllegalAccessException {
        WrapperTest wrapperTest = new WrapperTest();
        wrapperTest.query();
        wrapperTest.update();
    }

    void update() throws IllegalAccessException {
        ArrayList<WrapperUpdateBean> list = new ArrayList<>();
        WrapperUpdateBean bean1 = new WrapperUpdateBean();
        bean1.setId("12345");
        bean1.setName("wangwu");
        bean1.setAge("12");
        WrapperUpdateBean bean2 = new WrapperUpdateBean();
        bean2.setId("12344");
        bean2.setName("wang2");
        bean2.setAge("13");
        list.add(bean1);
        list.add(bean2);
        MsUpdateWrapper<WrapperUpdateBean> wrapper = new MsUpdateWrapper<>();
        System.out.println(wrapper.warp(list));

    }

    void query() throws InstantiationException, IllegalAccessException {
        MsQueryWrapper<WrapperBean> wrapper = new MsQueryWrapper<>(WrapperBean.class);
        wrapper.eq("name", "zhangsan")
                .like("age", "12");
        RdbmsDynamicFieldQueryRequest warp = wrapper.warp();
        ArrayList<HashMap<String, Object>> contents = new ArrayList<>();
        HashMap<String, Object> content1 = new HashMap<String, Object>(4) {
            private static final long serialVersionUID = 5159226184792146658L;

            {
                put("id", "12345");
                put("name", "zhangsan");
                put("age", "12");
            }
        };
        HashMap<String, Object> content2 = new HashMap<String, Object>(4) {
            private static final long serialVersionUID = -8385749193473913080L;

            {
                put("id", "12344");
                put("name", "lisi");
                put("age", "15");
            }
        };
        contents.add(content1);
        contents.add(content2);
        for (MsQueryCondition condition : warp.getConditions()) {
            System.out.println(condition);
        }
        System.out.println(warp.getLabels());
        List<WrapperBean> wrapperBeans = wrapper.unWarp(contents);
        for (WrapperBean wrapperBean : wrapperBeans) {
            System.out.println(wrapperBean);
        }
    }
}
