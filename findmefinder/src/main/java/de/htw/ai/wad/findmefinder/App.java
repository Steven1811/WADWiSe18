package de.htw.ai.wad.findmefinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;

public class App {
    public static void main (String[] args)
    {
        //Error handling
        if(args==null || args.length == 0) {
            System.out.println("Please provide the first argument to a .class file.");
            System.exit(1);
        }

        String classPath = System.getProperty("user.dir") + File.separator + args[0];
        int extensionIndex = args[0].lastIndexOf('.');

        //In case no extension was provided
        if(extensionIndex == -1) {
            System.out.println("Please provide a file with an extension.");
            System.exit(0);
        }

        //Build class name
        String className = args[0].substring(0, extensionIndex);

        Class targetClass = null;
        try {
            targetClass = ClassLoader.loadClass(classPath, className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class " + args[0] + " not found.");
            System.exit(1);
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL encountered. Exiting...");
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        AnnotationChecker checker = new AnnotationChecker(targetClass);

        System.out.println("Fields:");
        for(Field field : checker.getFindmeFields()) {
            printProperty(field);
        }

        System.out.println("\nMethods:");
        for(Method method : checker.getFindmeMethods()) {
            printProperty(method);
        }

        System.exit(0);
    }

    public static void printProperty(Method method) {
        System.out.println(Modifier.toString(method.getModifiers()) + " " +  method.getReturnType().getCanonicalName() + " " + method.getName());
    }

    public static void printProperty(Field field) {
        System.out.println(Modifier.toString(field.getModifiers()) + " " +  field.getType().getCanonicalName() + " " + field.getName());
    }
}