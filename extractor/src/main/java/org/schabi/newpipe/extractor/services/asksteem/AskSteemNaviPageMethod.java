package org.schabi.newpipe.extractor.services.asksteem;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.navigator.method.StreamNavigatorPageMethod;
import org.schabi.newpipe.extractor.services.dtube.extractors.DTubeStreamInfoItemExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AskSteemNaviPageMethod implements StreamNavigatorPageMethod<AskSteemStreamRequestData> {
    private static final AskSteemNaviPageMethod instance = new AskSteemNaviPageMethod();

    protected int page = 0;
    protected Map<String, String> params = new HashMap<String, String>();

    public static AskSteemNaviPageMethod getInstance() {
        return instance;
    }

    // useless java trash
    protected class ContainStringKeyMap<V> {
        protected AskSteemStreamRequestData data;
        protected Map<String, V> options;

        public ContainStringKeyMap(AskSteemStreamRequestData data) {
            this.data = data;
        }

        public ContainStringKeyMap(Map<String, V> options) {
            this.options = options;
        }

        public Map<String, ?> getMap() {
            return options == null ? data.getMap() : options;
        }
    }

    // TODO move to OtherParams package
    protected Map<String, String> getParams(ContainStringKeyMap...containers) {
        if (containers == null) {
            return params;
        }
        for (ContainStringKeyMap container:containers) {
            for (Object entry : container.getMap().entrySet()) {
                Map.Entry<String, ?> mapEntry = (Map.Entry<String, ?>) entry;
                params.put(mapEntry.getKey(), (String) mapEntry.getValue());
            }
        }
        return params;
    }

    // TODO move to OtherUrl package
    protected String getQueryFromMap(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder query = new StringBuilder("?");
        for (Map.Entry<String, String> param:params.entrySet()) {
            query.append(URLEncoder.encode(param.getKey(), Encodings.UTF_8)).append('=').append(URLEncoder.encode(param.getValue(), Encodings.UTF_8)).append('&');
        }
        query.setLength(query.length()-1);
        return query.toString();
    }

    protected void updateParams(Map<String, String> params) {
        if (params.keySet().contains("pg")) {
            try {
                page = Integer.parseInt(params.get("pg"));
            } catch (NumberFormatException ex) {
                // TODO better error handling
            }
        }
        this.params = params;
    }

    protected String getUrl(String path, Map<String, String> params) throws UnsupportedEncodingException {
        return AskSteemParsingHelper.ASKSTEEM_ENDPOINT+'/'+path+'/'+getQueryFromMap(params);
    }

    @Override
    public String request(AskSteemStreamRequestData data, Map<String, Object> options) throws IOException {
        HttpDownloader downloader = NewPipe.getDownloader();
        Map<String, String> params = getParams(new ContainStringKeyMap(data), new ContainStringKeyMap<Object>(options));
        updateParams(params);
        return downloader.download(getUrl(data.getPath(), params));
    }

    @Override
    public StreamInfoItemsCollector getCollector(AskSteemStreamRequestData data, String body) throws ParsingException {
        StreamInfoItemsCollector collector = new StreamInfoItemsCollector(data.getService().getServiceId());
        try {
            JsonObject object = JsonParser.object().from(body);
            if ((!object.has(Words.RESULTS)) && object.has("error") && object.isBoolean("error") && object.getBoolean("error")) {
                String message = object.has("message") && object.isString("message") ? object.getString("message") : "server response with error and without message";
                throw new ParsingException(message);
            }
            if (object.has(Words.RESULTS) && object.get(Words.RESULTS) instanceof JsonArray) {
                for (Object video:object.getArray(Words.RESULTS)) {
                    if (!(video instanceof JsonObject)) {
                        continue;
                    }

                    JsonObject jsonVideo = (JsonObject) video;
                    if (jsonVideo.has(Words.META) && jsonVideo.get(Words.META) instanceof JsonObject) {
                        collector.commit(new DTubeStreamInfoItemExtractor(data.getService(), jsonVideo.getObject(Words.META).getObject(Words.VIDEO), jsonVideo));
                    }
                }
            }
        } catch (JsonParserException e) {
            throw new ParsingException(e.getMessage(), e);
        }
        return collector;
    }

    @Override
    public String nextPage(AskSteemStreamRequestData data, List<StreamInfoItem> items) throws ParsingException {
        Map<String, String> parameters = getParams(new ContainStringKeyMap<String>(params), new ContainStringKeyMap(data));
        page++;
        parameters.put("pg",Integer.toString(page));
        updateParams(parameters);
        try {
            return getQueryFromMap(parameters);
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
    }
}
