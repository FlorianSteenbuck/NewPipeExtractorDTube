package org.schabi.newpipe.extractor.services.dtube.extractors;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.SuggestionExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.asksteem.AskSteemParsingHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DTubeSuggestionExtractor extends SuggestionExtractor {
    public DTubeSuggestionExtractor(int serviceId) {
        super(serviceId);
    }

    @Override
    public List<String> suggestionList(String query, String contentCountry) throws IOException, ExtractionException {
        HttpDownloader dl = NewPipe.getDownloader();
        List<String> suggestions = new ArrayList<>();

        String url = AskSteemParsingHelper.ASKSTEEM_ENDPOINT+"/suggestions?term="+ URLEncoder.encode(query, "UTF-8");

        String response = dl.download(url, contentCountry);
        try {
            JsonArray collection = JsonParser.array().from(response).getArray(1, new JsonArray());
            for (Object suggestion : collection) {
                if (!(suggestion instanceof JsonArray)) continue;
                String suggestionStr = ((JsonArray)suggestion).getString(0);
                if (suggestionStr == null) continue;
                suggestions.add(suggestionStr);
            }

            return suggestions;
        } catch (JsonParserException e) {
            throw new ParsingException("Could not parse json response", e);
        }
    }
}
