package me.adeshina.webcrawler;

import java.util.concurrent.atomic.AtomicInteger;

public final class CrawlerConfig {

    private int crawlDelaySeconds = 35;
    private AtomicInteger maxPages = new AtomicInteger(100);
    private int threads = 2;

    /**
     * Sets how many seconds each thread should wait before making another HTTP request to the host.
     * @param crawlDelaySeconds politeness delay
     */
    public void setCrawlDelaySeconds(int crawlDelaySeconds) {
        this.crawlDelaySeconds = crawlDelaySeconds;
    }

    /**
     * Sets the maximum number of pages to be crawled;
     * @param maxPages maximum number of pages.
     */
    public void setMaxPages(int maxPages) {
        this.maxPages = new AtomicInteger(maxPages);
    }

    public AtomicInteger getMaxPages() {
        return maxPages;
    }

    public long getDelayMilliSecs() {
        return crawlDelaySeconds * 1000;
    }

    /**
     * Set the number of threads to use for crawling. More threads mean the process is faster. Defaults to two.
     * @param threads number of threads.
     */
    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getThreads() {
        return threads;
    }
}
