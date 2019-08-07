package me.adeshina.sitecrawler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A FIFO structure used to schedule URLs for crawling. It ensures no URL is crawled twice and only URLs eligible for
 * crawling are queued. A URL is eligible for crawling if it:
 *
 * <ul>
 *     <li>Doesn't match any Disallow directives specified by the site's robots.txt</li>
 *     <li>Is relative to the site</li>
 *     <li>points to page that contains HTML</li>
 * </ul>
 */
final class CrawlQueue {

    private Queue<String> queue = new ArrayDeque<>(400);
    private Set<String> alreadyCrawled = new HashSet<>();
    private List<Pattern> disallowPatterns = new ArrayList<>();

    private String domain;

    {
        disallowPatterns.add(Pattern.compile(".*\\Q.\\E(pdf|epub|csv|zip|ico)"));
        disallowPatterns.add(Pattern.compile(".*\\Q.\\E(png|jpeg|gif)"));
        disallowPatterns.add(Pattern.compile(".*\\Q.\\E(ogg|wav|flac|mp3|mp4)"));
        disallowPatterns.add(Pattern.compile(".*#.*"));
    }

    CrawlQueue(String domain) {
        this.domain = domain;
    }

    /**
     * Specifies URL patterns disallowed by the site's robots.txt file.
     */
    void setDisallowPatterns(List<Pattern> disallowPatterns) {
        this.disallowPatterns.addAll(disallowPatterns);
    }

    /**
     * Schedules a URL for crawling if it has not been crawled and is valid for crawling. Validity means: it
     * doesn't match any Disallow directives specified by the site's robots.txt, it is relative to the site, and it
     * points to page that contains HTML.
     *
     * @return True if the URL was added to the queue. False otherwise.
     */
    boolean add(String url) {

        boolean valid = disallowPatterns.stream().noneMatch(pattern -> pattern.matcher(url).matches());
        boolean notCrawled = !alreadyCrawled.contains(url) && url.startsWith(domain);

        if (valid && notCrawled) {
            return queue.add(url);
        } else {
            return false;
        }
    }

    /**
     * Returns the next URL scheduled for crawling, if there is one.
     *
     * @return A URL if one there is any in the queue.
     */
    Optional<String> next() {
        String url = queue.poll();
        return url == null ? Optional.empty() : Optional.of(url);
    }

    /**
     * Marks a URL that has already been crawled.
     */
    void addToCrawled(String url) {
        alreadyCrawled.add(url);
    }

    /**
     * Checks if the crawl queue is empty.
     *
     * @return true if there are no URLs available for crawling, false otherwise.
     */
    boolean isEmpty() {
        return queue.isEmpty();
    }

}
