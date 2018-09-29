package org.schabi.newpipe.extractor.services.dtube;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.SuggestionExtractor;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.linkhandler.*;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.DynamicSettings;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.dtube.extractors.DTubeChannelExtractor;
import org.schabi.newpipe.extractor.services.dtube.extractors.DTubeKioskExtractor;
import org.schabi.newpipe.extractor.services.dtube.extractors.DTubeStreamExtractor;
import org.schabi.newpipe.extractor.services.dtube.extractors.DTubeSuggestionExtractor;
import org.schabi.newpipe.extractor.services.dtube.linkHandler.DTubeLinkHandlerFactory;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.VIDEO;

public class DTubeService extends StreamingService {
    protected static Settings settings;
    protected static boolean settingsAreDynamic = true;

    public DTubeService(int id) {
        super(id, "DTube", singletonList(VIDEO), supportedLocales);
        settings = new DTubeSettings(this);
    }

    @Override
    public boolean isDynamicSettings() {
        return settingsAreDynamic;
    }

    @Override
    public void refreshSettings() throws IOException {
        if (isDynamicSettings()) {
            ((DynamicSettings) settings).refresh();
        }
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public void updateSettings(Settings newSettings) {
        settings = newSettings;
        settingsAreDynamic = newSettings instanceof DynamicSettings;
    }

    @Override
    public LinkHandlerFactory getStreamUIHFactory() {
        return DTubeLinkHandlerFactory.getVideoInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelUIHFactory() {
        return DTubeLinkHandlerFactory.getChannelInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistUIHFactory() {
        return null;
    }

    @Override
    public SearchQueryHandlerFactory getSearchQIHFactory() {
        return null;
    }

    @Override
    public SearchExtractor getSearchExtractor(SearchQueryHandler queryHandler, String contentCountry) {
        return null;
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new DTubeSuggestionExtractor(getServiceId());
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        KioskList.KioskExtractorFactory kioskFactory = new KioskList.KioskExtractorFactory() {
            @Override
            public KioskExtractor createNewKiosk(StreamingService streamingService, String url, String id) throws ExtractionException {
                return new DTubeKioskExtractor(
                        DTubeService.this,
                        DTubeLinkHandlerFactory.getKioskInstance().fromUrl(url),
                        id
                );
            }
        };

        KioskList list = new KioskList(getServiceId());

        final DTubeLinkHandlerFactory h = new DTubeLinkHandlerFactory(
                false,
                false,
                true,
                false
        );

        try {
            list.addKioskEntry(kioskFactory, h, DTubeKiosk.HOT.getId());
            list.addKioskEntry(kioskFactory, h, DTubeKiosk.TRENDING.getId());
            list.addKioskEntry(kioskFactory, h, DTubeKiosk.NEW.getId());
        } catch (Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }

    @Override
    public ChannelExtractor getChannelExtractor(ListLinkHandler urlIdHandler) throws ExtractionException {
        return new DTubeChannelExtractor(this, urlIdHandler);
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(ListLinkHandler urlIdHandler) throws ExtractionException {
        return null;
    }

    @Override
    public StreamExtractor getStreamExtractor(LinkHandler urlIdHandler) throws ExtractionException {
        return new DTubeStreamExtractor(this, urlIdHandler);
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }
}
