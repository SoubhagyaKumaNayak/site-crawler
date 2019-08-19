package me.adeshina.sitecrawler;

import java.util.Set;

import me.adeshina.sitecrawler.dto.WebPage;
import me.adeshina.sitecrawler.exception.CrawlerCreateException;
import me.adeshina.sitecrawler.exception.InvalidDomainUrlException;

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
        assertThrows(InvalidDomainUrlException.class, () -> SiteCrawler.create(domain, config));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://www.facebook.com/news/2", "https://google.com/news/"})
    void shouldThrowExceptionForNonTopLevelDomainUrl(String url) {
        assertThrows(InvalidDomainUrlException.class, () -> SiteCrawler.create(url, config));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://goo_gle.com/", "https://www.example2.com"})
    void shouldAcceptTopLevelDomainUrl(String domain) {
        assertDoesNotThrow(() -> SiteCrawler.create(domain, config));
    }
}
