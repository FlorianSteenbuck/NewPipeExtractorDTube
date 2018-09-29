package org.schabi.newpipe.extractor.services.dtube.extractors;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.navigator.NavigatorPage;
import org.schabi.newpipe.extractor.navigator.StreamNavigatorPage;
import org.schabi.newpipe.extractor.services.dtube.DTubeKiosk;
import org.schabi.newpipe.extractor.services.dtube.linkHandler.DTubeLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.steemit.SteemitStreamNaviPageMethod;
import org.schabi.newpipe.extractor.services.steemit.SteemitStreamRequestData;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import javax.annotation.Nonnull;
import java.io.IOException;

public class DTubeKioskExtractor extends KioskExtractor {
    private NavigatorPage pageNavi;
    private DTubeKiosk kiosk;

    public DTubeKioskExtractor(StreamingService streamingService, ListLinkHandler url, String kioskId) throws ExtractionException {
        super(streamingService, url, kioskId);
    }

    @Override
    public void onFetchPage(@Nonnull HttpDownloader downloader) throws IOException, ExtractionException {
        kiosk = DTubeKiosk.getKioskById(getId());
        pageNavi = new StreamNavigatorPage<SteemitStreamRequestData, SteemitStreamNaviPageMethod>(
                new SteemitStreamRequestData(12,
                        "dtube",
                        getService(),
                        new Object[]{"database_api", "get_discussions_by_"+kiosk.getBy()},
                        DTubeLinkHandlerFactory.getVideoInstance()),
                SteemitStreamNaviPageMethod.getInstance()
        );
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        assertPageFetched();
        return kiosk.getName();
    }

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        return pageNavi.getInitialPage();
    }

    @Override
    public String getNextPageUrl() throws IOException, ExtractionException {
        return pageNavi.getNextPageUrl();
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(String nextPageUrl) throws IOException, ExtractionException {
        return pageNavi.getPage(nextPageUrl);
    }
}
