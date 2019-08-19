package me.adeshina.sitecrawler.exception;

import me.adeshina.sitecrawler.SiteCrawler;

/**
 * Base class for exceptions thrown when creating a {@link SiteCrawler}.
 * <br>
 * Concrete subclasses:
 * {@link InvalidMaxPagesException},
 * {@link InvalidDomainUrlException},
 * {@link InvalidPolitenessDelayException}
 */
public abstract class CrawlerCreateException extends Exception {

    public CrawlerCreateException(String message) {
        super(message);
    }
}
