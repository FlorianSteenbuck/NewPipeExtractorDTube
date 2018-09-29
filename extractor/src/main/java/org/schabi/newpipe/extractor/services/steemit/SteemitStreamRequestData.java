package org.schabi.newpipe.extractor.services.steemit;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.services.dtube.linkHandler.DTubeLinkHandlerFactory;

public class SteemitStreamRequestData{
    protected StreamingService service;
    protected String url;
    protected int limit;
    protected String tag;
    protected Object[] params;
    protected DTubeLinkHandlerFactory urlIdHandler;

    public SteemitStreamRequestData(int limit, String tag, StreamingService service, Object[] params, DTubeLinkHandlerFactory urlIdHandler) {
        this.limit = limit;
        this.tag = tag;
        this.service = service;
        this.params = params;
        this.urlIdHandler = urlIdHandler;
    }

    public StreamingService getService() {
        return service;
    }

    public int getLimit() {
        return limit;
    }

    public Object[] getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public DTubeLinkHandlerFactory getUrlIdHandler() {
        return urlIdHandler;
    }
}