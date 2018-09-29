package org.schabi.newpipe.extractor.services.dtube.extractors;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.navigator.StreamNavigatorPage;
import org.schabi.newpipe.extractor.services.dtube.linkHandler.DTubeLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.dtube.DTubeParsingHelper;
import org.schabi.newpipe.extractor.services.steemit.SteemitParsingHelper;
import org.schabi.newpipe.extractor.services.steemit.SteemitStreamNaviPageMethod;
import org.schabi.newpipe.extractor.services.steemit.SteemitStreamRequestData;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import javax.annotation.Nonnull;
import java.io.IOException;

public class DTubeChannelExtractor extends ChannelExtractor {
    protected StreamNavigatorPage pageNavi;
    protected JsonObject result;
    protected JsonObject meta;

    public DTubeChannelExtractor(StreamingService service, ListLinkHandler urlIdHandler) {
        super(service, urlIdHandler);
    }

    @Override
    public String getAvatarUrl() throws ParsingException {
        assertPageFetched();
        return SteemitParsingHelper.STEEMIT_IMG_ENDPOINT+ SteemitParsingHelper.PROFIL_SIZE_STEEMIT +DTubeParsingHelper.getStringFromJson(Words.META, meta, "profile_image");
    }

    @Override
    public String getBannerUrl() throws ParsingException {
        assertPageFetched();
        return SteemitParsingHelper.STEEMIT_IMG_ENDPOINT+ SteemitParsingHelper.BANNER_SIZE_STEEMIT +DTubeParsingHelper.getStringFromJson(Words.META, meta, "cover_image");
    }

    @Override
    public String getFeedUrl() throws ParsingException {
        // NO SUPPORT YET
        return null;
    }

    @Override
    public long getSubscriberCount() throws ParsingException {
        HttpDownloader dl = NewPipe.getDownloader();
        byte[] request = SteemitParsingHelper.getSteemitRequest("call", new Object[]{"follow_api","get_follow_count", new Object[]{getId()}}).getBytes();

        try {
            String response = dl.download(SteemitParsingHelper.STEEMIT_ENDPOINT, request);
            return JsonParser.object().from(response).getObject(Words.RESULT).getNumber("follower_count").longValue();
        } catch (IOException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        } catch (JsonParserException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String getDescription() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getStringFromJson(Words.META, meta,"about").getData();
    }

    @Override
    public String[] getDonationLinks() throws ParsingException {
        return new String[0];
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

    @Override
    public void onFetchPage(@Nonnull HttpDownloader downloader) throws IOException, ExtractionException {
        DTubeLinkHandlerFactory urlHandler = DTubeLinkHandlerFactory.getChannelInstance();
        Object[] params = urlHandler.getSteemitParams(getOriginalUrl());
        DTubeParsingHelper.DTubeResultAndMeta resultAndMeta = SteemitParsingHelper.getResultAndMetaFromSteemitContent(
                downloader,
                "call",
                Words.PROFILE,
                new Object[]{"database_api","get_accounts", params}
        );
        meta = resultAndMeta.getMeta();
        result = resultAndMeta.getResult();
        pageNavi = new StreamNavigatorPage<SteemitStreamRequestData, SteemitStreamNaviPageMethod>(
                new SteemitStreamRequestData(50,
                urlHandler.getAuthor(getOriginalUrl()),
                getService(),
                new Object[]{"database_api", "get_discussions_by_blog"},
                ((DTubeLinkHandlerFactory) getService().getStreamUIHFactory())),
                SteemitStreamNaviPageMethod.getInstance()
        );
    }

    @Nonnull
    @Override
    public String getId() throws ParsingException {
        return DTubeLinkHandlerFactory.getChannelInstance().getAuthor(getOriginalUrl());
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getStringFromJson(Words.META, meta,"name").getData();
    }
}
