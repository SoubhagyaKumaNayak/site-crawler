package me.adeshina.webcrawler.internal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RobotsFileParser {

    public static Optional<List<Pattern>> disallowedPatterns(String domain) {

        List<Pattern> disallowed = new ArrayList<>();

        try {
            URL url = new URL(domain.concat("/robots.txt"));
            InputStream fileInputStream = url.openStream();
            Scanner robotsFile = new Scanner(new InputStreamReader(fileInputStream));

            while (robotsFile.hasNextLine()) {
                String line = robotsFile.nextLine();
                if (!line.isEmpty() && line.equalsIgnoreCase("User-agent: *")) {
                    while (robotsFile.hasNextLine()) {
                        String anotherLine = robotsFile.nextLine();
                        if (anotherLine.matches("[Dd]isallow: .*")) {
                            anotherLine.replaceAll("\\Q*\\E", ".*");
                            disallowed.add(Pattern.compile(anotherLine));
                        }
                    }
                }
            }
            robotsFile.close();
        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.of(disallowed);
    }
}
