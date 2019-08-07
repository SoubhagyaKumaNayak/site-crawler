package me.adeshina.sitecrawler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrawlQueueTest {

    private CrawlQueue queue;
    private final String domain = "https://www.example.com/";

    @BeforeEach
    void setup() {
        queue = new CrawlQueue(domain);
    }

    @ParameterizedTest
    @ValueSource(strings = {domain + "section/page", domain + "content/songs.html", domain + "docs/paper"})
    void shouldNotAddAlreadyCrawledUrl(String url) {
        queue.addToCrawled(url);
        boolean added = queue.add(url);
        assertFalse(added);
    }

    @ParameterizedTest
    @ValueSource(strings = {domain + "section/#page", domain + "content/song.mp3", domain + "docs/paper.pdf"})
    void shouldNotAddDisallowedUrl(String url) {
        boolean added = queue.add(url);
        assertFalse(added);
    }

    @ParameterizedTest
    @ValueSource(strings = {domain + "section/page", domain + "content/song.html", domain + "docs/paper"})
    void shouldAddAllowedUrl(String url) {
        boolean added = queue.add(url);
        assertTrue(added);
    }

}
