package me.adeshina.sitecrawler.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class RobotsFileParserTest {

    private RobotsFileParser robotsFileUtil = new RobotsFileParser("example.com");
    private RobotsFileParser spy = spy(robotsFileUtil);
    private List<Pattern> disallowedUrlPatterns;

    @BeforeEach
    public void setup() throws IOException {

        InputStream robotsFileInputStream = Files.newInputStream(Paths.get("src/test/resources/robots.txt"));

        doReturn(robotsFileInputStream).when(spy).openRobotsFile();
        doCallRealMethod().when(spy).disallowedPatterns();

        disallowedUrlPatterns = spy.disallowedPatterns().get();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/contents", "/pages/bac/french", "/hello/welcome"})
    public void shouldMatchDisallowedUrls(String url) {
        assertTrue(disallowedUrlPatterns.stream().anyMatch(pattern -> pattern.matcher(url).matches()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/pages/hello.html", "/news/headlines"})
    public void shouldNotMatchAllowedUrls(String url) {
        assertTrue(disallowedUrlPatterns.stream().noneMatch(pattern -> pattern.matcher(url).matches()));
    }

}
