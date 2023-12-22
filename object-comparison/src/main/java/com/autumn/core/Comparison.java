package com.autumn.core;

import java.lang.reflect.Field;

/**
 * @author niann
 * @description
 * @date 2023/12/22 18:21
 */
public class Comparison {


    public static <T> boolean typeEqual(T t1, T t2) {
        Class<?> clazz1 = t1.getClass();
        Class<?> clazz2 = t2.getClass();
        return clazz1.getName().equals(clazz2.getName());
    }


    public static <T> boolean supperTypeEqual(T obj1, T obj2) {
        Class<?> t1 = obj1.getClass();
        Class<?> t2 = obj2.getClass();
        Class<?> superclass1 = t1.getSuperclass();
        Class<?> superclass2 = t2.getSuperclass();
        return superclass1.getName().equals(superclass2.getName());
    }

    /**
     * 比较两个对象属性是否相同返回 boolean
     *
     * @param obj1
     * @param obj2
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> boolean objectInfoEqual(T obj1, T obj2) throws Exception {
        T t = objectInfoEqualEntity(obj1, obj2);
        return t == null;
    }

    /**
     * 比较两个对象是否相同
     * 不相同返回不相同的字段否则返回null
     *
     * @param obj1
     * @param obj2
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T objectInfoEqualEntity(T obj1, T obj2) throws Exception {
        if (!typeEqual(obj1, obj2)) {
            throw new RuntimeException("对象类型不一致");
        }
        T val = build(obj1, obj2);
        return val;
    }

    private static <T> T build(T obj1, T obj2) throws Exception {
        Class<?> t1 = obj1.getClass();
        Class<?> t2 = obj2.getClass();
        Field[] fields = t1.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object v1 = field.get(obj1);
            String fieldName = field.getName();
            Field fd2 = t2.getDeclaredField(fieldName);
            fd2.setAccessible(true);
            Object v2 = fd2.get(obj2);
            Field t1Field = t1.getDeclaredField(field.getName());
            t1Field.setAccessible(true);
            if (v1.equals(v2)) {
                String typeName = field.getType().getTypeName();
                if (typeName.equals("int") || typeName.equals("short") || typeName.equals("double") || typeName.equals("float") || typeName.equals("long")) {
                    t1Field.set(obj1, 0);
                } else {
                    t1Field.set(obj1, null);
                }
            } else {
                t1Field.set(obj1, v1);
            }
        }
        Class<?> objClass = obj1.getClass();
        Field[] objectFields = objClass.getDeclaredFields();
        for (Field field : objectFields) {
            field.setAccessible(true);
            Object val = field.get(obj1);
            String typeName = field.getType().getTypeName();
            if (typeName.equals("int") || typeName.equals("long")) {
                if (!(Integer.parseInt(val.toString()) == 0)) {
                    return obj1;
                }
            } else if (typeName.equals("short") || typeName.equals("double") || typeName.equals("float")) {
                if (!(Double.parseDouble(val.toString()) == 0.0)) {
                    return obj1;
                }
            } else {
                if (val != null) {
                    return obj1;
                }
            }
        }
        return null;
    }


}
