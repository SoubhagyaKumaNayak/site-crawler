package me.adeshina.sitecrawler.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Exposes utility methods for working with HTTP.
 */
public final class HttpUtil {

    private static final String FIREFOX_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:10.0) "
            + "Gecko/20100101 Firefox/62.0";

    private static final String CHROME_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 "
                    + "Safari/537.36";

    private static List<String> userAgents = Arrays.asList(CHROME_AGENT, FIREFOX_AGENT);

    /**
     * Returns a random user agent string. The user agents are those of major browsers.
     * @return a browser's user agent.
     */
    public static String userAgent() {
        Random random = new Random();
        return userAgents.get(random.nextInt(userAgents.size()));
    }
}
