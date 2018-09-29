package org.schabi.newpipe.extractor.services.steemit;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.constants.Keys;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.navigator.method.StreamNavigatorPageMethod;
import org.schabi.newpipe.extractor.services.dtube.extractors.DTubeStreamInfoItemExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class SteemitStreamNaviPageMethod implements StreamNavigatorPageMethod<SteemitStreamRequestData> {
    private static final SteemitStreamNaviPageMethod instance = new SteemitStreamNaviPageMethod();

    public static SteemitStreamNaviPageMethod getInstance() {
        return instance;
    }

    @Override
    public String request(SteemitStreamRequestData data, Map<String, Object> addOptions) throws IOException {
        HttpDownloader downloader = NewPipe.getDownloader();

        JsonObject options = new JsonObject();
        options.put("tag", data.getTag());
        // without no output
        options.put("limit", data.getLimit());
        if (addOptions != null) {
            for (Map.Entry<String, Object> entry:addOptions.entrySet()) {
                options.put(entry.getKey(), entry.getValue());
            }
        }

        int len = data.getParams().length;
        Object[] actualParams = new Object[len+1];
        for (int i = 0; i < len; i++) {
            actualParams[i] = data.getParams()[i];
        }
        actualParams[len] = new Object[]{options};

        byte[] request = SteemitParsingHelper.getSteemitRequest("call", actualParams).getBytes();
        return downloader.download(SteemitParsingHelper.STEEMIT_ENDPOINT, request);
    }

    @Override
    public StreamInfoItemsCollector getCollector(SteemitStreamRequestData data, String body) throws ParsingException {
        StreamInfoItemsCollector collector = new StreamInfoItemsCollector(data.getService().getServiceId());
        try {
            JsonObject jsonResponse = JsonParser.object().from(body);
            if (jsonResponse.has(Words.RESULT)) {
                for (Object obj : jsonResponse.getArray(Words.RESULT)) {
                    if (!(obj instanceof JsonObject)) {
                        continue;
                    }

                    JsonObject json = (JsonObject) obj;
                    if (json.has(Keys.JSON_METADATA) && (
                            json.isString(Keys.JSON_METADATA) ||
                                    json.get(Keys.JSON_METADATA) instanceof JsonObject
                    )) {
                        JsonObject meta = json.isString(Keys.JSON_METADATA) ?
                                JsonParser.object().from(json.getString(Keys.JSON_METADATA)) :
                                json.getObject(Keys.JSON_METADATA);

                        // currently only d.tube support
                        // TODO add more steemit plattforms
                        if (meta.has("app")
                                && meta.isString("app")
                                && meta.getString("app").startsWith("dtube")
                                && meta.has(Words.VIDEO)
                                && meta.get(Words.VIDEO) instanceof JsonObject) {
                            collector.commit(new DTubeStreamInfoItemExtractor(data.getService(), meta.getObject(Words.VIDEO), json));
                        }
                    }
                }
            } else {
                throw new ParsingException("response \""+jsonResponse.toString()+"\" has no result");
            }
        } catch (JsonParserException ex) {
            throw new ParsingException(ex.getMessage(), ex.getCause());
        }
        return null;
    }

    @Override
    public String nextPage(SteemitStreamRequestData data, List<StreamInfoItem> items) throws ParsingException {
        int size = items.size();
        if (size > 0) {
            StreamInfoItem lastItem = items.get(size - 1);
            String url = lastItem.getUrl();
            String permId = data.getUrlIdHandler().getPermId(url);
            String author = data.getUrlIdHandler().getAuthor(url);
            try {
                return url + "?start_author=" + URLEncoder.encode(author, Encodings.UTF_8) +
                        "&start_permlink=" + URLEncoder.encode(permId, Encodings.UTF_8);
            } catch (UnsupportedEncodingException ex) {
                throw new ParsingException(ex.getMessage(), ex.getCause());
            }
        }
        return data.getUrl();
    }
}
