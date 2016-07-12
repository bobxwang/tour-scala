package com.bob.javatour;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Nothing {

    public static void main(String[] args) {

        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        Class<?> lclass = l.getClass();
        System.out.println(lclass);
        Type type = lclass.getGenericSuperclass();
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        try {
            /**
             * 因为类型擦除，其实java中很难获取具体的T，不同于c#
             */
            Class<?> ltemp = (Class) params[0];
            Object obj = ltemp.newInstance();
            System.out.println(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> ls = new ArrayList<>();
        ls.add("fuck");
        ls.add("51");
        Class<?> lsclass = ls.getClass();
        System.out.println(lsclass);

    }
}