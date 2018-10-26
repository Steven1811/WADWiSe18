package de.htw.ai.wad.findmefinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class AnnotationChecker {
    private ArrayList<Method> findmeMethods = new ArrayList();
    private ArrayList<Field> findmeFields = new ArrayList();

    public AnnotationChecker(Class classToCheck) {
        Method[] methods = classToCheck.getDeclaredMethods();
        Field[] fields = classToCheck.getDeclaredFields();

        for(Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for(Annotation annotation : annotations) {
                if(annotation instanceof FindMe) {
                    findmeMethods.add(method);
                }
            }
        }

        for(Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for(Annotation annotation : annotations) {
                if(annotation instanceof FindMe) {
                    findmeFields.add(field);
                }
            }
        }
    }

    public ArrayList<Field> getFindmeFields() {
        return findmeFields;
    }

    public ArrayList<Method> getFindmeMethods() {
        return findmeMethods;
    }
}
