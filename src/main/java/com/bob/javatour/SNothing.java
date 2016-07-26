package com.bob.javatour;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SNothing {

    public static void main(String[] args) {

        int x = 4;
        java.util.Date date = (x > 4) ? new A() : new B();


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

    static class A extends java.util.Date {

        public void test () {
            short s1 = 1;
            // s1 = s1 + 1; it required int but found short
            s1 += 1; // this is ok
            System.out.println(s1);
        }
    }

    static class B extends java.util.Date {
    }
}