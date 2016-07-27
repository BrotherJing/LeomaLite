package brotherjing.com.leomalite;

import org.junit.Test;

import brotherjing.com.leomalite.util.VersionChecker;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void versionCheck(){
        String v1 = "4.3.1";
        String v2 = "4.3.1.1";
        assertTrue(VersionChecker.isNewerVersion(v2,v1));
    }

}