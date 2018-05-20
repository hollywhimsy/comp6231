package test;

import org.junit.Assert;

public class PrintAssert extends Assert {
    public static void assertEquals(String message, Object expected, Object actual){
        Assert.assertEquals(message,expected,actual);
        System.out.println(message + "Expected Object: " + expected.toString() + "Actual Object: " + actual.toString());
    }
}
