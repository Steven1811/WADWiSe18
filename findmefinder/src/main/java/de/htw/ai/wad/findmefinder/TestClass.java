package de.htw.ai.wad.findmefinder;

public class TestClass {

    private int test1;

    @FindMe()
    private String test2;

    public void setTest1()
    {
        this.test1 = 1;
    }

    @FindMe
    public void setTest2()
    {
        this.test2 = "2";
    }

}