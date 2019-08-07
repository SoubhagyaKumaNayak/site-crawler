package me.adeshina.sitecrawler;

import me.adeshina.sitecrawler.internal.HttpUtil;

/**
 * Configuration for the crawler.
 */
public final class CrawlConfig {

    private int crawlDelaySeconds = 7;
    private int maxPages = 750;
    private boolean respectRobotsFile = true;
    private String userAgent = HttpUtil.userAgent();

    /**
     * Sets the politeness delay i.e the number of seconds between successive requests to the site.
     * The default is seven(7) seconds.
     */
    public void crawlDelaySeconds(int crawlDelaySeconds) {
        this.crawlDelaySeconds = crawlDelaySeconds;
    }

    /**
     * Sets the maximum number of pages to crawl. Defaults to 750
     * @param maxPages The number of pages
     */
    public void maxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    /**
     * Returns the maximum number of pages to be crawled
     */
    public int maxPages() {
        return maxPages;
    }

    /**
     * Returns the politeness delay i.e number of seconds between successive requests to the website.
     */
    public int crawlDelaySeconds() {
        return crawlDelaySeconds;
    }

    /**
     * Returns the politeness delay in milliseconds.
     */
    public long delayMilliSeconds() {
        return crawlDelaySeconds * 1000;
    }

    /**
     * Returns true if the domain's robots.txt should be respected. False otherwise.
     */
    public boolean respectRobotsFile() {
        return respectRobotsFile;
    }

    /**
     * Sets whether or not to respect the domain's robots.txt file.
     */
    public void respectRobotsFile(boolean respectRobotsFile) {
        this.respectRobotsFile = respectRobotsFile;
    }

    /**
     * Sets the user-agent string for HTTP requests. Defaults to user-agents of major browsers chosen randomly.
     */
    public void userAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Returns the user-agent string that is used for HTTP. This will be randomly chosen if not explicity set.
     */
    public String userAgent() {
        return userAgent;
    }
}
