package me.adeshina.sitecrawler;

import java.util.List;
import java.util.Optional;

import java.util.Set;
import java.util.regex.Pattern;

import me.adeshina.sitecrawler.dto.WebPage;
import me.adeshina.sitecrawler.exception.CrawlException;
import me.adeshina.sitecrawler.internal.RobotsFileParser;

/**
 * Entry point for a single-threaded crawler that scraps pages from a single site. The crawl is a blocking operation.
 * Configuration options include: maximum number of pages to fetch, the politeness delay between
 * successive requests to the site, and whether or not to respect robots.txt. Any external URLs will be ignored.
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
     * @throws CrawlException
     */
    public static SiteCrawler get(String site, CrawlConfig config) throws CrawlException {

        boolean validDomain = Pattern.matches("https?://[0-9a-zA-Z.]*/?$", site);
        boolean validMaxPages = config.maxPages() >= 1;
        boolean validDelaySeconds = config.crawlDelaySeconds() >= 1;

        if (!validMaxPages) {
            throw new CrawlException("Maximum number of pages to crawl cannot be negative");
        } else if (!validDelaySeconds) {
            throw new CrawlException("Number of seconds between each page download cannot be negative");
        } else if (!validDomain) {
            throw new CrawlException("The site URL is not valid");
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
