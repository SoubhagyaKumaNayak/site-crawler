package me.adeshina.webcrawler.dto;

import java.util.HashSet;
import java.util.Set;

// todo: Javadoc everything....
public final class CrawlResult {

    private String domain;
    private Set<WebPage> pages = new HashSet<>();

    public CrawlResult(String domain) {
        this.domain = domain;
    }

    public void addPage(WebPage page) {
        pages.add(page);
    }

    private Set<WebPage> getPages() {
        return new HashSet<>(pages);
    }

}
