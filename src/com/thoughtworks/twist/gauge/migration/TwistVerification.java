package com.thoughtworks.twist.gauge.migration;

import junit.framework.Assert;

public class TwistVerification {
    public static void verify(boolean condition) {
        Assert.assertTrue((String)null, condition);
    }

    public static void verify(String message, boolean condition) {
        Assert.assertTrue(message, condition);
    }

    public static void verifyTrue(boolean condition) {
        Assert.assertTrue((String)null, condition);
    }

    public static void verifyTrue(String message, boolean condition) {
        Assert.assertTrue(message, condition);
    }

    public static void verifyFalse(boolean condition) {
        Assert.assertFalse((String)null, condition);
    }
    
    public static void verifyFalse(String message, boolean condition) {
        Assert.assertFalse(message, condition);
    }

    public static void verifyEquals(Object expected, Object actual) {
        Assert.assertEquals((String)null, expected, actual);
    }
    
    public static void verifyEquals(String message, Object expected, Object actual){
    	Assert.assertEquals(message, expected, actual);
    }

}
