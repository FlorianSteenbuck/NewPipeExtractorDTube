package org.schabi.newpipe.extractor.services.dailymotion;

import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;
import org.schabi.newpipe.extractor.Locale;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.SuggestionExtractor;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.linkhandler.*;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.schabi.newpipe.extractor.Locale.*;

public class DailymotionService extends StreamingService {
    public DailymotionService(int id, String name) {
        super(
                id,
                name,
                asList(
                        ServiceInfo.MediaCapability.VIDEO,
                        ServiceInfo.MediaCapability.LIVE
                ), Locale.from(GREECE,
                        "gb",
                        "lb",
                        TAIWAN_PROVINCE_OF_CHINA,
                        TURKEY,
                        THAILAND,
                        DENMARK,
                        GERMANY,
                        QATAR,
                        EGYPT,
                        SOUTH_AFRICA,
                        SPAIN,
                        RUSSIAN_FEDERATION,
                        ITALY,
                        ROMANIA,
                        BELGIUM,
                        BAHRAIN,
                        BRAZIL,
                        TUNISIA,
                        OMAN,
                        CÔTE_D_IVOIRE,
                        SWITZERLAND,
                        "cn",
                        CANADA,
                        PORTUGAL,
                        PAKISTAN,
                        "ph",
                        POLAND,
                        HONG_KONG,
                        VIET_NAM,
                        JAPAN,
                        MONTENEGRO,
                        MOROCCO,
                        MALAYSIA,
                        MEXICO,
                        ZIMBABWE,
                        SAUDI_ARABIA,
                        UNITED_ARAB_EMIRATES,
                        ANDORRA,
                        "ar",
                        AUSTRALIA,
                        AUSTRIA,
                        INDIA,
                        IRELAND,
                        "id",
                        NETHERLANDS,
                        UNITED_STATES,
                        NIGERIA,
                        FRANCE,
                        KOREA_REPUBLIC_OF,
                        KUWAIT,
                        SENEGAL,
                        SINGAPORE,
                        SWEDEN
                )
        );
    }

    @Override
    public boolean isDynamicSettings() {
        return false;
    }

    @Override
    public void refreshSettings() throws IOException, ReCaptchaException {

    }

    @Override
    public Settings getSettings() {
        return null;
    }

    @Override
    public void updateSettings(Settings newSettings) {

    }

    @Override
    public LinkHandlerFactory getStreamUIHFactory() {
        return null;
    }

    @Override
    public ListLinkHandlerFactory getChannelUIHFactory() {
        return null;
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
        return null;
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        return null;
    }

    @Override
    public ChannelExtractor getChannelExtractor(ListLinkHandler urlIdHandler) throws ExtractionException {
        return null;
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(ListLinkHandler urlIdHandler) throws ExtractionException {
        return null;
    }

    @Override
    public StreamExtractor getStreamExtractor(LinkHandler UIHFactory) throws ExtractionException {
        return null;
    }
}
