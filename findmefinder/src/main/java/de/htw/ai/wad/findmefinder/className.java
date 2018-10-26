package de.htw.ai.wad.findmefinder;

import java.math.BigDecimal;

public class className {
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