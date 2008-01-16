package com.nraynaud.sport.web.view;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;

public class PageDetail {
    private String content = "";
    private String title = "";
    private final List<String> javascript = new ArrayList<String>(1);
    private boolean loginHeader;
    private boolean showAdvert = true;

    public static PageDetail detailFor(final ServletRequest request) {
        PageDetail detail = (PageDetail) request.getAttribute("detail");
        if (detail == null) {
            detail = new PageDetail();
            request.setAttribute("detail", detail);
        }
        return detail;
    }

    private PageDetail() {
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addJavascript(final String script) {
        javascript.add(script);
    }

    public List<String> getJavascript() {
        return javascript;
    }

    public void setLoginHeader(final boolean loginHeader) {
        this.loginHeader = loginHeader;
    }

    public boolean isLoginHeader() {
        return loginHeader;
    }

    public boolean isShowAdvert() {
        return showAdvert;
    }

    public void setShowAdvert(final boolean showAdvert) {
        this.showAdvert = showAdvert;
    }
}
