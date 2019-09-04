[![Build Status](https://travis-ci.com/abdulwahabO/site-crawler.svg?branch=master)](https://travis-ci.com/abdulwahabO/site-crawler)

# About

A simple crawler library for scraping the contents of websites. Written in Java using JDK 1.8. Not intended for any serious production use.

# Usage

First the project has to be built locally with Maven. At the project root, use `mvn clean install`. Then add the 
following to your `pom.xml`

```xml
<dependency>
      <groupId>me.adeshina</groupId>
      <artifactId>site-crawler</artifactId>
      <version>1.0.0-SNAPSHOT</version>
</dependency>
```
To crawl a website, you'll need to first configure the crawling process, obtain a crawler for the site and start it. E.g

```java
public class CrawlMySite {
    
    public void useCrawler() {
        
        CrawlConfig config = new CrawlConfig();
        config.maxPages(2);
        config.respectRobotsFile(false);
        config.crawlDelaySeconds(1);
                        
        // Only URLs in this domain will be visited
        String site = "https://twitter.com";
                
        SiteCrawler twitterCrawler = SiteCrawler.get(site, config);
                        
        // Blocking call
        Set<WebPage> pages = twitterCrawler.start();
    }
            
}
```

# Dependecies

* Jsoup 
* JUnit 5
* Mockito
