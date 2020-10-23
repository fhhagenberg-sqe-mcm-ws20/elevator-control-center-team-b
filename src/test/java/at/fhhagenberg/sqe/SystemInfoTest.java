package at.fhhagenberg.sqe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SystemInfoTest {
    @Test
    public void testJavaVersion() {
        assertTrue(SystemInfo.javaVersion().contains("15"));
    }

    @Test
    public void testJavafxVersion() {
        if (SystemInfo.javafxVersion() != null)
            assertEquals("13", SystemInfo.javafxVersion());
        else
            System.out.println("Could not find javafx version");
    }
}