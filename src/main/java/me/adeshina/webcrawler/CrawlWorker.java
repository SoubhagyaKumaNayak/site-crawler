package me.adeshina.webcrawler;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import me.adeshina.webcrawler.dto.CrawlResult;
import me.adeshina.webcrawler.dto.WebPage;
import me.adeshina.webcrawler.internal.util.HttpUtil;
import me.adeshina.webcrawler.internal.util.LinkFilter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 */
class CrawlWorker implements Runnable {

    private CrawlerConfig config;
    private CrawlQueue queue;
    private LinkFilter linkFilter;
    private CrawlResult result;

    CrawlWorker(CrawlerConfig config, CrawlQueue queue, CrawlResult data, LinkFilter linkFilter) {
        this.config = config;
        this.queue = queue;
        this.result = data;
        this.linkFilter = linkFilter;
    }

    @Override
    public void run() {

        AtomicInteger pagesCountDown = config.getMaxPages();

        while (pagesCountDown.get() != 0 && !queue.isEmpty()) {

            Optional<String> optionalUrl = queue.next();
            optionalUrl.ifPresent((url) -> {
                try {
                    Connection.Response response =
                            Jsoup.connect(url).userAgent(HttpUtil.userAgent()).execute().bufferUp();

                    Document page = response.parse();
                    Elements hyperlinks = page.select("a[href]");

                    hyperlinks.forEach((hyperlink) -> {
                        String completeUrl = hyperlink.absUrl("href");
                        if (linkFilter.isCrawlable(completeUrl)) {
                            queue.queue(completeUrl);
                        }
                    });

                    result.addPage(new WebPage(url, page.title(), response.body()));
                    queue.done(url);
                    pagesCountDown.decrementAndGet();
                    Thread.sleep(config.getDelayMilliSecs());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
