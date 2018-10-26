/**
 * Note:
 * This file needs to be separately compiled with the command: javac TestClass.java
 * This will generate a testClass.class file that can be used for testing with the ClassLoader
 *
 * Why is this necessary?:
 * It is necessary because otherwise the ClassLoader would look in the wrong package scope.
 * Notice that this file does not contain any package declaration for that reason.
 * The ClassLoader only finds classes which are located in the default package scope.
 */

import java.math.BigDecimal;
import de.htw.ai.wad.findmefinder.FindMe;

public class TestClass {
    //Fields
    @FindMe
    public static String publicStaticField;

    @FindMe
    private final int privatesFinalField = 1;

    @FindMe
    protected BigDecimal accountBalance;


    //Methods
    @FindMe
    public static void method1() {

    }

    @FindMe
    private int method2() {
        return 1;
    }

    @FindMe
    protected final String method3() {
        return "Hello world!";
    }
}