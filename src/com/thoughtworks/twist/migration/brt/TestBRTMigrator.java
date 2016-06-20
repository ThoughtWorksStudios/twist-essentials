package com.thoughtworks.twist.migration.brt;
import junit.framework.TestCase;

public class TestBRTMigrator extends TestCase {
    private BRTMigrator brtMigrator = new BRTMigrator();

    public void testCamelCase() {
        assertEquals("helloWorld", brtMigrator.camelCase("hello World"));
        assertEquals("helloWorld", brtMigrator.camelCase("hello world"));
        assertEquals("helloWoRld", brtMigrator.camelCase("hello woRld"));
        assertEquals("helloWoRld", brtMigrator.camelCase("Hello woRld"));
        assertEquals("helloWoRld", brtMigrator.camelCase("Hello   woRld"));
    }
}
