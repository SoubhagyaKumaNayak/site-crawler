package me.adeshina.sitecrawler.exception;

public class InvalidPolitenessDelayException extends CrawlerCreateException {
    private static String message = "Number of seconds between each page download cannot be negative";

    public InvalidPolitenessDelayException() {
        super(message);
    }
}
