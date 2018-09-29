package org.schabi.newpipe.extractor;

import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.linkhandler.*;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;

import java.io.IOException;
import java.util.*;

public abstract class StreamingService {
    public static class ServiceInfo {
        private final String name;
        private final List<MediaCapability> mediaCapabilities;

        public ServiceInfo(String name, List<MediaCapability> mediaCapabilities) {
            this.name = name;
            this.mediaCapabilities = Collections.unmodifiableList(mediaCapabilities);
        }

        public String getName() {
            return name;
        }

        public List<MediaCapability> getMediaCapabilities() {
            return mediaCapabilities;
        }

        public enum MediaCapability {
            AUDIO, VIDEO, LIVE
        }
    }

    public enum LinkType {
        NONE,
        STREAM,
        CHANNEL,
        PLAYLIST
    }

    private final int serviceId;
    private final ServiceInfo serviceInfo;
    private final Map<Locale, Locale.DefinedLocal> supportedLocales;

    public StreamingService(int id, String name, List<ServiceInfo.MediaCapability> capabilities, Collection<Locale.DefinedLocal> supportedLocales) {
        this.serviceId = id;
        this.supportedLocales = new HashMap<Locale, Locale.DefinedLocal>();
        for (Locale.DefinedLocal local:supportedLocales) {
            this.supportedLocales.put(local.getLocale(), local);
        }
        this.serviceInfo = new ServiceInfo(name, capabilities);
    }

    public final int getServiceId() {
        return serviceId;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    @Override
    public String toString() {
        return serviceId + ":" + serviceInfo.getName();
    }

    public Map<Locale, Locale.DefinedLocal> getSupportedLocales() {
        return supportedLocales;
    }

    public abstract boolean isDynamicSettings();
    public abstract void refreshSettings() throws IOException, ReCaptchaException;
    public abstract Settings getSettings();
    public abstract void updateSettings(Settings newSettings);

    ////////////////////////////////////////////
    // Url Id handler
    ////////////////////////////////////////////
    public abstract LinkHandlerFactory getStreamUIHFactory();
    public abstract ListLinkHandlerFactory getChannelUIHFactory();
    public abstract ListLinkHandlerFactory getPlaylistUIHFactory();
    public abstract SearchQueryHandlerFactory getSearchQIHFactory();


    ////////////////////////////////////////////
    // Extractor
    ////////////////////////////////////////////
    public abstract SearchExtractor getSearchExtractor(SearchQueryHandler queryHandler, String contentCountry);
    public abstract SuggestionExtractor getSuggestionExtractor();
    public abstract SubscriptionExtractor getSubscriptionExtractor();
    public abstract KioskList getKioskList() throws ExtractionException;

    public abstract ChannelExtractor getChannelExtractor(ListLinkHandler urlIdHandler) throws ExtractionException;
    public abstract PlaylistExtractor getPlaylistExtractor(ListLinkHandler urlIdHandler) throws ExtractionException;
    public abstract StreamExtractor getStreamExtractor(LinkHandler UIHFactory) throws ExtractionException;

    public SearchExtractor getSearchExtractor(String query, List<String> contentFilter, String sortFilter, String contentCountry) throws ExtractionException {
        return getSearchExtractor(getSearchQIHFactory().fromQuery(query, contentFilter, sortFilter), contentCountry);
    }

    public ChannelExtractor getChannelExtractor(String id, List<String> contentFilter, String sortFilter) throws ExtractionException {
        return getChannelExtractor(getChannelUIHFactory().fromQuery(id, contentFilter, sortFilter));
    }

    public PlaylistExtractor getPlaylistExtractor(String id, List<String> contentFilter, String sortFilter) throws ExtractionException {
        return getPlaylistExtractor(getPlaylistUIHFactory().fromQuery(id, contentFilter, sortFilter));
    }

    public SearchExtractor getSearchExtractor(String query, String contentCountry) throws ExtractionException {
        return getSearchExtractor(getSearchQIHFactory().fromQuery(query), contentCountry);
    }

    public ChannelExtractor getChannelExtractor(String url) throws ExtractionException {
        return getChannelExtractor(getChannelUIHFactory().fromUrl(url));
    }

    public PlaylistExtractor getPlaylistExtractor(String url) throws ExtractionException {
        return getPlaylistExtractor(getPlaylistUIHFactory().fromUrl(url));
    }

    public StreamExtractor getStreamExtractor(String url) throws ExtractionException {
        return getStreamExtractor(getStreamUIHFactory().fromUrl(url));
    }



    /**
     * figure out where the link is pointing to (a channel, video, playlist, etc.)
     */
    public final LinkType getLinkTypeByUrl(String url) throws ParsingException {
        LinkHandlerFactory sH = getStreamUIHFactory();
        LinkHandlerFactory cH = getChannelUIHFactory();
        LinkHandlerFactory pH = getPlaylistUIHFactory();

        if (sH.acceptUrl(url)) {
            return LinkType.STREAM;
        } else if (cH.acceptUrl(url)) {
            return LinkType.CHANNEL;
        } else if (pH.acceptUrl(url)) {
            return LinkType.PLAYLIST;
        } else {
            return LinkType.NONE;
        }
    }
}
