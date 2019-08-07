package me.adeshina.sitecrawler;

import java.util.Set;
import me.adeshina.sitecrawler.dto.WebPage;
import me.adeshina.sitecrawler.exception.CrawlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiteCrawlerTest {

    private CrawlConfig config = new CrawlConfig();

    @BeforeEach
    public void setup() {
        config.maxPages(2);
        config.respectRobotsFile(false);
        config.crawlDelaySeconds(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/www.google.com", "https:/google.com"})
    void shouldThrowExceptionForInvalidUrls(String domain) {
        assertThrows(CrawlException.class, () -> SiteCrawler.get(domain, config));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://www.facebook.com/news/2", "https://google.com/news/"})
    void shouldThrowExceptionForNonTopLevelDomainUrl(String url) {
        assertThrows(CrawlException.class, () -> SiteCrawler.get(url, config));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://google.com/", "https://www.example2.com"})
    void shouldAcceptTopLevelDomainUrl(String domain) {
        assertDoesNotThrow(() -> SiteCrawler.get(domain, config));
    }

    @Test
    void shouldReturnMaxPages() throws Exception {

        String site = "https://twitter.com";
        SiteCrawler twitterCrawler = SiteCrawler.get(site, config);

        Set<WebPage> pages = twitterCrawler.start();
        boolean noExternalPages = pages.stream().allMatch(page -> page.getUrl().startsWith(site));

        assertNotNull(pages);
        assertFalse(pages.isEmpty(), "No web pages in result");
        assertEquals(2, pages.size());
        assertTrue(noExternalPages, "URLs outside " + site + " were crawled");
    }
}
