package de.htw.ai.wad.findmefinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class App {
    public static void main (String[] args)
    {
        Class aClass = className.class;
        AnnotationChecker checker = new AnnotationChecker(aClass);

        System.out.println("Fields:");
        for(Field field : checker.getFindmeFields()) {
            printProperty(field);
        }

        System.out.println("\nMethods:");
        for(Method method : checker.getFindmeMethods()) {
            printProperty(method);
        }
    }

    public static void printProperty(Method method) {
        System.out.println(Modifier.toString(method.getModifiers()) + " " +  method.getReturnType().getCanonicalName() + " " + method.getName());
    }

    public static void printProperty(Field field) {
        System.out.println(Modifier.toString(field.getModifiers()) + " " +  field.getType().getCanonicalName() + " " + field.getName());
    }
}