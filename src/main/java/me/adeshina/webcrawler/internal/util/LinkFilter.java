package me.adeshina.webcrawler.internal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LinkFilter {

    private String domain;
    private List<Pattern> filters = new ArrayList<>();

    {
        filters.add(Pattern.compile(".*\\Q.\\E(pdf|epub)"));
        filters.add(Pattern.compile(".*\\Q.\\E(png|jpeg|gif)"));
        filters.add(Pattern.compile(".*\\Q.\\E(ogg|wav|flac|mp3|mp4)"));
        // todo: filter out URLs with hash #
    }

    public LinkFilter(String domain, List<Pattern> filters) {
        this.domain = domain;
        this.filters.addAll(filters);
    }

    public LinkFilter(String domain) {
        this.domain = domain;
    }

    public boolean isCrawlable(String url) {
        return filters.stream().noneMatch(pattern -> pattern.matcher(url).matches()) && url.contains(domain);
    }
}
