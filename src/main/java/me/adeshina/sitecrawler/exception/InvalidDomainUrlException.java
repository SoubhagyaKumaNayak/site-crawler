package me.adeshina.sitecrawler.exception;

public class InvalidDomainUrlException extends CrawlerCreateException {
    private static String message = "The site URL is not valid";

    public InvalidDomainUrlException() {
        super(message);
    }
}
