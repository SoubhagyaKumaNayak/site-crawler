package me.adeshina.webcrawler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import me.adeshina.webcrawler.dto.CrawlResult;
import me.adeshina.webcrawler.dto.WebPage;
import me.adeshina.webcrawler.exception.CrawlException;
import me.adeshina.webcrawler.internal.util.HttpUtil;
import me.adeshina.webcrawler.internal.util.LinkFilter;
import me.adeshina.webcrawler.internal.util.RobotsFileParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

// todo: can always be renamed to the new name of the library..
// todo: doc should warn user about number of OS threads in use......
// todo: readme should note it's designed to run on a single machine...

public final class WebCrawler {

    // TODO: much later: After version 1.0.0, add use of non-blocking call via CompleteableFutures...

    private String domain;
    private CrawlerConfig config;

    public WebCrawler(String domain, CrawlerConfig config) {
        this.domain = domain;
        this.config = config;
    }

    /**
     *
     * @return
     * @throws CrawlException If the domain ----------------------
     */
    public CrawlResult crawl() throws CrawlException {

        CrawlQueue queue = new CrawlQueue();
        CrawlResult result = new CrawlResult(domain);

        Optional<List<Pattern>> filters = RobotsFileParser.disallowedPatterns(domain);
        LinkFilter linkFilter = filters.isPresent() ? new LinkFilter(domain, filters.get()) : new LinkFilter(domain);

        try {
            Connection.Response response =
                    Jsoup.connect(domain).userAgent(HttpUtil.userAgent()).execute().bufferUp();

            Document page = response.parse();
            Elements hyperlinks = page.select("a[href]");

            hyperlinks.forEach((hyperlink) -> {
                String completeUrl = hyperlink.absUrl("href");
                if (linkFilter.isCrawlable(completeUrl)) {
                    queue.queue(completeUrl);
                }
            });

            result.addPage(new WebPage(domain, page.title(), response.body()));
            queue.done(domain);

        } catch (IOException e) {
            throw new CrawlException("Could not crawl domain: " + domain, e);
        }

        ExecutorService executor = Executors.newFixedThreadPool(config.getThreads());

        for (int i = 0; i > config.getThreads(); i++) {
            executor.submit(new CrawlWorker(config, queue, result, linkFilter));
        }

        return result;
    }

}
