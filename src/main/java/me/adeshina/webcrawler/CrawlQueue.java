package me.adeshina.webcrawler;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

final class CrawlQueue {

    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(100, true);
    private Set<String> alreadyCrawled = new HashSet<>();

    /**
     * @param url A URL that is candidate for crawling.
     */
    void queue(String url) {
        if (!alreadyCrawled.contains(url)) {
            try {
                queue.offer(url, 100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Returns an {@link Optional} that will contain the next URL scheduled for crawling if there is one. If the queue
     * is empty this method blocks for 100 milliseconds while waiting for a new element.
     *
     * @return An {@link Optional} that could contain a URL.
     */
    Optional<String> next() {
        try {
            String url = queue.poll(100, TimeUnit.MILLISECONDS);
            return url == null ? Optional.empty() : Optional.of(url);
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    /**
     * Adds a URL to a collection of those that have already been crawled.
     *
     * @param url a URL that should no longer be crawled
     */
    void done(String url) {
        alreadyCrawled.add(url);
    }

    /**
     * Checks if the crawl queue is empty.
     *
     * @return true if there are no URLs in queue, false otherwise.
     */
    boolean isEmpty() {
        return queue.isEmpty();
    }
}
