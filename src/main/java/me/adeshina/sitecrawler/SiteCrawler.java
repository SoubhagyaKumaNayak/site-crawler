package me.adeshina.sitecrawler;

import java.util.List;
import java.util.Optional;

import java.util.Set;
import java.util.regex.Pattern;

import me.adeshina.sitecrawler.dto.WebPage;
import me.adeshina.sitecrawler.exception.CrawlerCreateException;
import me.adeshina.sitecrawler.exception.InvalidDomainUrlException;
import me.adeshina.sitecrawler.exception.InvalidMaxPagesException;
import me.adeshina.sitecrawler.exception.InvalidPolitenessDelayException;
import me.adeshina.sitecrawler.internal.RobotsFileParser;

/**
 * Entry point for a single-threaded crawler that scraps pages from a single site. The crawl is a blocking operation.
 * Configuration options include: maximum number of pages to fetch, the politeness delay between
 * successive requests to the site, and whether or not to respect robots.txt. Any external URLs will be ignored.
 *
 * Start by creating a crawler using {@link SiteCrawler#create(String, CrawlConfig)}.
 */
public final class SiteCrawler {

    private String site;
    private CrawlConfig config;

    private SiteCrawler(String site, CrawlConfig config) {
        this.site = site;
        this.config = config;
    }

    /**
     * Returns a crawler which will scrap a site using the specified config.
     *
     * @param site The full URL of the site e.g https://www.facebook.com.
     * @param config The config for the crawl.
     * @return A crawler for the given site.
     * @throws CrawlerCreateException if the site URL or a config field is invalid
     */
    public static SiteCrawler create(String site, CrawlConfig config) throws CrawlerCreateException {

        boolean validDomain = Pattern.matches("https?://[-_0-9a-zA-Z.]*/?$", site);
        boolean validMaxPages = config.maxPages() >= 1;
        boolean validDelaySeconds = config.crawlDelaySeconds() >= 1;

        if (!validMaxPages) {
            throw new InvalidMaxPagesException();
        } else if (!validDelaySeconds) {
            throw new InvalidPolitenessDelayException();
        } else if (!validDomain) {
            throw new InvalidDomainUrlException();
        }

        return new SiteCrawler(site, config);
    }

    /**
     * Starts the crawler. This is a blocking call.
     * @return The crawled pages from the site.
     */
    public Set<WebPage> start() {

        CrawlQueue queue = new CrawlQueue(site);

        if (config.respectRobotsFile()) {
            RobotsFileParser robotsFileParser = new RobotsFileParser(site);
            Optional<List<Pattern>> filters = robotsFileParser.disallowedPatterns();
            filters.ifPresent(queue::setDisallowPatterns);
        }

        CrawlWorker crawlWorker = new CrawlWorker(config, queue, site);

        return crawlWorker.start();
    }
}
