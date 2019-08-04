package me.adeshina.webcrawler.dto;

// todo: find a better name --
public class WebPage {

    private String title;
    private String url;
    private String rawHtml;

    public WebPage(String url, String title, String rawHtml) {
        this.url = url;
        this.title = title;
        this.rawHtml = rawHtml;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRawHtml() {
        return rawHtml;
    }

    public void setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
    }
}
