package org.fwoxford.service.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuyu on 2017-04-04.
 */
public class EntityUtil {
    public static Object avoidFieldValueNull(Object bean) {
        if (bean == null) {
            return null;
        }
        Class<?> cls = bean.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(bean);
                if (value == null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                    if (field.getType().equals(String.class)){
                        field.set(bean, " ");
                    } else if (field.getType().equals(Integer.class)){
                        field.set(bean, 0);
                    }
                } else if ("".equals(value)){
                    field.set(bean, " ");
                }
            } catch (Exception e) {
                continue;
            }
        }
        return bean;
    }
}
