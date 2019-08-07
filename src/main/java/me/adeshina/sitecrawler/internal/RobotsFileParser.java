package me.adeshina.sitecrawler.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parser for robots.txt files. Only recognises Disallow directives, Allow and Crawl-Delay directives are ignored.
 */
public class RobotsFileParser {

    private final String domain;

    public RobotsFileParser(String domain) {
        this.domain = domain;
    }

    /**
     * Returns a list of URL patterns, if any, for which the domain's robots.txt file disallows crawling.
     */
    public Optional<List<Pattern>> disallowedPatterns() {

        Optional<List<String>> directives = disallowDirectives();

        if (directives.isPresent()) {
            List<Pattern> disallowed = directives.get()
                                                 .stream()
                                                 .map(directive -> directive.replaceAll("\\Q*\\E", ".*"))
                                                 .map(Pattern::compile)
                                                 .collect(Collectors.toList());

            return Optional.of(disallowed);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Reads from the robots.txt file and returns the disallow directives, if any.
     * The {@link Optional} returned would be empty if there was a error reading from the file.
     */
    private Optional<List<String>> disallowDirectives() {
        List<String> directives = new ArrayList<>();

        try (Scanner scanner = new Scanner(new InputStreamReader(openRobotsFile()))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty() && line.equalsIgnoreCase("User-agent: *")) {
                    while (scanner.hasNextLine()) {
                        String directive = scanner.nextLine();
                        if (directive.matches("[Dd]isallow: .*")) {
                            directive = directive.split(":")[1].trim();
                            directives.add(directive);
                        }
                    }
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.of(directives);
    }

    /**
     * Returns an input stream for reading from the robots.txt file
     * @throws IOException
     */
    InputStream openRobotsFile() throws IOException {
        URL url = new URL(domain.concat("/robots.txt"));
        return url.openStream();
    }
}
