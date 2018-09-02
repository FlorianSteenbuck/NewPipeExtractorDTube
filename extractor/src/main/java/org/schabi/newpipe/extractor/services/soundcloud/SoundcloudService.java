package org.schabi.newpipe.extractor.services.soundcloud;

import org.schabi.newpipe.extractor.*;
import org.schabi.newpipe.extractor.linkhandler.*;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import com.github.FlorianSteenbuck.other.settings.model.settings.EmptySettings;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;

import static java.util.Collections.singletonList;
import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.AUDIO;

public class SoundcloudService extends StreamingService {

    public SoundcloudService(int id) {
        super(id, "SoundCloud", singletonList(AUDIO), supportedLocales);
    }

    @Override
    public SearchExtractor getSearchExtractor(SearchQueryHandler queryHandler, String contentCountry) {
        return new SoundcloudSearchExtractor(this, queryHandler, contentCountry);
    }

    @Override
    public SearchQueryHandlerFactory getSearchQIHFactory() {
        return new SoundcloudSearchQueryHandlerFactory();
    }

    @Override
    public LinkHandlerFactory getStreamUIHFactory() {
        return SoundcloudStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelUIHFactory() {
        return SoundcloudChannelLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistUIHFactory() {
        return SoundcloudPlaylistLinkHandlerFactory.getInstance();
    }


    @Override
    public StreamExtractor getStreamExtractor(LinkHandler LinkHandler) {
        return new SoundcloudStreamExtractor(this, LinkHandler);
    }

    @Override
    public ChannelExtractor getChannelExtractor(ListLinkHandler urlIdHandler) {
        return new SoundcloudChannelExtractor(this, urlIdHandler);
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(ListLinkHandler urlIdHandler) {
        return new SoundcloudPlaylistExtractor(this, urlIdHandler);
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new SoundcloudSuggestionExtractor(getServiceId());
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        KioskList.KioskExtractorFactory chartsFactory = new KioskList.KioskExtractorFactory() {
            @Override
            public KioskExtractor createNewKiosk(StreamingService streamingService,
                                                 String url,
                                                 String id)
                    throws ExtractionException {
                return new SoundcloudChartsExtractor(SoundcloudService.this,
                        new SoundcloudChartsLinkHandlerFactory().fromUrl(url), id);
            }
        };

        KioskList list = new KioskList(getServiceId());

        // add kiosks here e.g.:
        final SoundcloudChartsLinkHandlerFactory h = new SoundcloudChartsLinkHandlerFactory();
        try {
            list.addKioskEntry(chartsFactory, h, "Top 50");
            list.addKioskEntry(chartsFactory, h, "New & hot");
        } catch (Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }


    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return new SoundcloudSubscriptionExtractor(this);
    }

    @Override
    public boolean isDynamicSettings() {
        return false;
    }

    @Override
    public void refreshSettings() {
        // dummy
    }

    @Override
    public Settings getSettings() {
        return new EmptySettings();
    }

    @Override
    public void updateSettings(Settings newSettings) {
        // dummy
    }
}
