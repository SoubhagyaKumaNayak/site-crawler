package me.adeshina.sitecrawler.dto;

/**
 * Data of a webpage from the site been crawled.
 */
public class WebPage {

    private String title;
    private String url;
    private String html;

    public WebPage(String url, String title, String html) {
        this.url = url;
        this.title = title;
        this.html = html;
    }

    /**
     * Returns the complete URL of the web page.
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the title of the web page.
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the HTML markup of the page. The markup is stripped of elements like <script>, <style>, <svg> etc.
     */
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
