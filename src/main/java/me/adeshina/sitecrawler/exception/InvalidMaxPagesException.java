package me.adeshina.sitecrawler.exception;

import me.adeshina.sitecrawler.SiteCrawler;

/**
 * Thrown on attempts to configure a {@link SiteCrawler} using an unacceptable value for max pages.
 * E.g if the number is less than zero.
 */
public class InvalidMaxPagesException extends CrawlerCreateException {
    private static String message = "Maximum number of pages to crawl cannot be negative";

    public InvalidMaxPagesException() {
        super(message);
    }
}
