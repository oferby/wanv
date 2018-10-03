import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/28/2015.
 */
public class TestRegex {


    @Test
    public void testIpAddress(){

        String ip1 = "255.255.255.255/32";
        String ip2 = "192.168.1.2/24";
        String ip3 = "10.0.0.1/8";
        String ip4 = "2.3.4.5/16";
        String pattern = "\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}/\\d{1,2}";

        boolean isIpAddress  = ip1.matches(pattern);

        assert isIpAddress;

        isIpAddress  = ip2.matches(pattern);

        assert isIpAddress;

        isIpAddress  = ip3.matches(pattern);

        assert isIpAddress;

        isIpAddress  = ip4.matches(pattern);

        assert isIpAddress;





    }



}
