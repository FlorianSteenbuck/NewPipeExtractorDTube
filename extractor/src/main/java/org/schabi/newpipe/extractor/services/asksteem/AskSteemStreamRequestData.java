package org.schabi.newpipe.extractor.services.asksteem;

import org.schabi.newpipe.extractor.StreamingService;

import java.util.Map;

public class AskSteemStreamRequestData {
    protected Map<String, String> map;
    protected String path;
    protected StreamingService service;

    public AskSteemStreamRequestData(Map<String, String> map, String path, StreamingService service) {
        this.map = map;
        this.path = path;
        this.service = service;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String getPath() {
        return path;
    }

    public StreamingService getService() {
        return service;
    }
}
