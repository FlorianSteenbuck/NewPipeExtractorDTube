package org.schabi.newpipe.extractor.services.dtube.extractors;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandler;
import org.schabi.newpipe.extractor.navigator.StreamNavigatorPage;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.asksteem.AskSteemNaviPageMethod;
import org.schabi.newpipe.extractor.services.asksteem.AskSteemStreamRequestData;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DTubeSearchExtractor extends SearchExtractor {
    private StreamingService service;
    private StreamNavigatorPage streamNavi;

    public DTubeSearchExtractor(StreamingService service, SearchQueryHandler urlIdHandler, String contentCountry) {
        super(service, urlIdHandler, contentCountry);
    }

    @Override
    public String getSearchSuggestion() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public InfoItemsPage<InfoItem> getInitialPage() throws IOException, ExtractionException {
        return streamNavi.getInitialPage();
    }

    @Override
    public String getNextPageUrl() throws IOException, ExtractionException {
        return streamNavi.getNextPageUrl();
    }

    @Override
    public InfoItemsPage<InfoItem> getPage(String pageUrl) throws IOException, ExtractionException {
        return streamNavi.getPage(pageUrl);
    }

    @Override
    public void onFetchPage(@Nonnull HttpDownloader downloader) throws IOException, ExtractionException {
        Map<String, String> query = new HashMap<String, String>();
        query.put("q", "meta.video.info.title:* AND "+getSearchString()+" ");
        query.put("include", "meta");

        streamNavi = new StreamNavigatorPage<AskSteemStreamRequestData, AskSteemNaviPageMethod>(
                new AskSteemStreamRequestData(query, "search", getService()),
                AskSteemNaviPageMethod.getInstance()
        );
    }
}
