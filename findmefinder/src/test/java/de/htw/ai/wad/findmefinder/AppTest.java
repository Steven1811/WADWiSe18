package de.htw.ai.wad.findmefinder;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.NoSuchFileException;

/**
 * TODO: Unit test for simple App.
 */
public class AppTest 
{
    public class TClass1
    {
        @FindMe
        public void publicMethod(){}

        public void secondPublicMethod(){};
    }

    public class TClass2
    {
        @FindMe
        public String publicField;

        public String secondPublicField;
    }

    Class tc1;
    Class tc2;
    Class clazz;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test(expected = NullPointerException.class)
    public void nullClass()
    {
        AnnotationChecker c = new AnnotationChecker(clazz);
    }

    @Test
    public void noNotatedMethods()
    {
        AnnotationChecker a = new AnnotationChecker(tc2);
        assertTrue(a.getFindmeMethods().isEmpty());
    }

    @Test
    public void noNotatedFields()
    {
        AnnotationChecker a = new AnnotationChecker(tc1);
        assertTrue(a.getFindmeFields().isEmpty());
    }

    @Test
    public void ContainsMethod()
    {
        AnnotationChecker a = new AnnotationChecker(tc1);
        try {
            Method m = tc1.getMethod("publicMethod");
            assertTrue(a.getFindmeMethods().contains(m));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void ContainsField()
    {
        AnnotationChecker a = new AnnotationChecker(tc2);
        try {
            Field f = tc2.getField("publicField");
            assertTrue(a.getFindmeFields().contains(f));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void doesNotContainMethod()
    {
        AnnotationChecker a = new AnnotationChecker(tc1);
        try {
            Method m = tc1.getMethod("secondPublicMethod");
            assertTrue(!a.getFindmeMethods().contains(m));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void doesNotContainField()
    {
        AnnotationChecker a = new AnnotationChecker(tc2);
        try {
            Field f = tc2.getField("secondPublicField");
            assertTrue(!a.getFindmeFields().contains(f));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    @Before
    public void initialized()
    {
        tc1 = TClass1.class;
        tc2 = TClass2.class;
        clazz = null;
    }
}
