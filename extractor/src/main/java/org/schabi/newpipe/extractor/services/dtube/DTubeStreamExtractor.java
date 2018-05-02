package org.schabi.newpipe.extractor.services.dtube;

import com.grack.nanojson.*;
import org.schabi.newpipe.extractor.*;
import org.schabi.newpipe.extractor.constants.Keys;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.http.HttpDownloader;
import org.schabi.newpipe.settings.model.settings.interfaces.Settings;
import org.schabi.newpipe.extractor.stream.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DTubeStreamExtractor extends StreamExtractor {
    public static final String RSHARES = "rshares";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    private JsonObject result;
    private JsonObject meta;

    private interface DTubeVoteFilter {
        boolean is(long rshares);
    }

    public DTubeStreamExtractor(StreamingService service, String url) {
        super(service, url);
    }

    @Nonnull
    @Override
    public String getUploadDate() throws ParsingException {
        assertPageFetched();
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newDateFormat.format(new Date(DTubeParsingHelper.getLongTimeJsonFromResult(result, Words.ACTIVE, DATE_FORMAT, DATE_FORMATTER)));
    }

    @Nonnull
    @Override
    public String getThumbnailUrl() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getThumbnail(meta, getService());
    }

    @Nonnull
    @Override
    public String getDescription() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getStringFromJson(
                "meta.content",
                DTubeParsingHelper.getContentObject(meta, null).getData(),
                "description"
        ).getData();
    }

    @Override
    public int getAgeLimit() throws ParsingException {
        assertPageFetched();
        // NO SUPPORT I can not find anything in the d.tube or steemit api
        return 0;
    }

    @Override
    public long getLength() throws ParsingException {
        assertPageFetched();
        DTubeParsingHelper.DTubePathReturnData<JsonObject> args = DTubeParsingHelper.getInfoObject(meta,null);
        JsonObject info = args.getData();
        String path = args.getPath();

        if (info.has(Words.DURATION)) {
            if (info.isNumber(Words.DURATION)) {
                return info.getNumber(Words.DURATION).longValue();
            } else {
                throw new ParsingException(path+"."+Words.DURATION+" is not a number");
            }
        }
        throw new ParsingException(path+" got no "+ Words.DURATION +" object");
    }

    @Override
    public long getTimeStamp() throws ParsingException {
        assertPageFetched();
        return -1;
    }

    @Override
    public long getViewCount() throws ParsingException {
        assertPageFetched();
        // Need Support low priority on steemit https://github.com/steemit/condenser/issues/812
        // TODO check if we can parse this from the steemit frontend
        return -1;
    }

    private long getCountOfVotesByFilter(DTubeVoteFilter filter) throws ParsingException {
        if (result.has(Keys.ACTIVE_VOTES)) {
            if (result.get(Keys.ACTIVE_VOTES) instanceof JsonArray) {
                long countOfVotes = 0;
                JsonArray arr = result.getArray(Keys.ACTIVE_VOTES);
                for (Object obj:arr) {
                    if (!(obj instanceof JsonObject)) {}
                    JsonObject jsonObject = (JsonObject) obj;
                    if (jsonObject.has(RSHARES) && jsonObject.isString(RSHARES)) {
                        try {
                            long rshares = Long.parseLong(String.valueOf(jsonObject.isString(RSHARES)));
                            if (filter.is(rshares)) {
                                countOfVotes++;
                            }
                        } catch (NumberFormatException e) {
                            // TODO better number parsing
                            // TODO better error handling
                        }
                    }
                }
                return countOfVotes;
            } else {
                throw new ParsingException(Words.RESULT +"."+ Keys.ACTIVE_VOTES +" is not a array");
            }
        }
        throw new ParsingException(Words.RESULT +" got no "+ Keys.ACTIVE_VOTES +" object");
    }

    @Override
    public long getLikeCount() throws ParsingException {
        assertPageFetched();
        return getCountOfVotesByFilter(new DTubeVoteFilter() {
            @Override
            public boolean is(long rshares) {
                return rshares < 0;
            }
        });
    }

    @Override
    public long getDislikeCount() throws ParsingException {
        assertPageFetched();
        return getCountOfVotesByFilter(new DTubeVoteFilter() {
            @Override
            public boolean is(long rshares) {
                return rshares >= 0;
            }
        });
    }

    @Nonnull
    @Override
    public String getUploaderName() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getStringFromMetaInfo(meta,"author").getData();
    }

    @Nonnull
    @Override
    public String getUploaderUrl() throws ParsingException {
        assertPageFetched();
        return getService().getChannelUrlIdHandler().getUrl(getUploaderName());
    }

    @Nonnull
    @Override
    public String getUploaderAvatarUrl() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.STEEMIT_IMG_ENDPOINT +"/u/"+getUploaderName()+"/avatar/small";
    }

    @Nonnull
    @Override
    public String getDashMpdUrl() throws ParsingException {
        assertPageFetched();
        return "";
    }

    @Nonnull
    @Override
    public String getHlsUrl() throws ParsingException {
        assertPageFetched();
        return "";
    }

    @Override
    public List<AudioStream> getAudioStreams() throws IOException, ExtractionException {
        assertPageFetched();
        return new ArrayList<AudioStream>();
    }

    @Override
    public List<VideoStream> getVideoStreams() throws IOException, ExtractionException {
        assertPageFetched();
        List<VideoStream> videoStreams = new ArrayList<VideoStream>();

        JsonObject contentObject = DTubeParsingHelper.getContentObject(meta,null).getData();
        for (Map.Entry<String, Object> contentEntry:contentObject.entrySet()) {
            String key = contentEntry.getKey();
            Object value = contentEntry.getValue();

            if ((!(key.startsWith(Words.VIDEO))) ||
                (!(key.endsWith(Words.HASH))) ||
                (!(value instanceof String)) ||
                (value instanceof String && ((String) value).trim().isEmpty())) {
                continue;
            }

            String variant = key.substring(5, key.length()-4).trim();
            variant = variant.isEmpty() ? "Source" : variant;
            String strValue = (String) value;

            Settings settings = getService().getSettings();
            if (settings.has(DTubeSettings.ID_IPFS_GATEWAYS)) {
                for (String gateway : (List<String>) settings.get(DTubeSettings.ID_IPFS_GATEWAYS)) {
                    String url = gateway + "/" + strValue;
                    boolean alreadyGotVideoStream = false;

                    Map<String, List<String>> headers = NewPipe.getDownloader().downloadHead(url);
                    if (headers.containsKey("Content-Typ")) {
                        List<String> contentTypes = headers.get("Content-Typ");
                        for (String contentTyp : contentTypes) {
                            // multiple content typ parser
                            // sometimes their is also a options but this is a wrong mime typ format caused by the '=' char
                            for (String mimeTyp : contentTyp.split(";")) {
                                MediaFormat mediaFormat = MediaFormat.getFromMimeType(mimeTyp);
                                if (mediaFormat != null) {
                                    videoStreams.add(new VideoStream(url, mediaFormat, variant));
                                    alreadyGotVideoStream = true;
                                    break;
                                }
                            }

                            if (alreadyGotVideoStream) {
                                break;
                            }
                        }
                    }

                    if (!alreadyGotVideoStream) {
                        videoStreams.add(new VideoStream(url, MediaFormat.UNKNOWN, variant));
                    }
                }
            }
        }
        return videoStreams;
    }

    @Override
    public List<VideoStream> getVideoOnlyStreams() throws IOException, ExtractionException {
        assertPageFetched();
        return new ArrayList<VideoStream>();
    }

    @Nonnull
    @Override
    public List<Subtitles> getSubtitlesDefault() throws IOException, ExtractionException {
        assertPageFetched();
        return new ArrayList<Subtitles>();
    }

    @Nonnull
    @Override
    public List<Subtitles> getSubtitles(SubtitlesFormat format) throws IOException, ExtractionException {
        assertPageFetched();
        return new ArrayList<Subtitles>();
    }

    @Override
    public StreamType getStreamType() throws ParsingException {
        assertPageFetched();
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public StreamInfoItem getNextVideo() throws IOException, ExtractionException {
        assertPageFetched();
        return null;
    }

    @Override
    public StreamInfoItemsCollector getRelatedVideos() throws IOException, ExtractionException {
        assertPageFetched();
        StreamInfoItemsCollector collector = new StreamInfoItemsCollector(getServiceId());

        String url = DTubeParsingHelper.ASKSTEEM_ENDPOINT +"/related?author="+getUploaderName()+"&permlink="+getPermLink()+"&include=meta,payout&q=created:>="+getUploadDate()+" AND meta.video.info.title:*";
        for (StreamInfoItemExtractor extractor:DTubeParsingHelper.getAskSteemStreamExtractorsByUrl(url, getService())) {
            collector.commit(extractor);
        }
        return collector;
    }

    protected String getPermLink() throws ParsingException {
        return DTubeParsingHelper.getStringFromMetaInfo(meta, Words.PERMLINK).getData();
    }

    @Override
    public String getErrorMessage() {
        assertPageFetched();
        return null;
    }

    @Override
    public void onFetchPage(@Nonnull HttpDownloader downloader) throws IOException, ExtractionException {
        DTubeUrlIdHandler urlHandler = (DTubeUrlIdHandler) getUrlIdHandler();
        DTubeParsingHelper.DTubeResultAndMeta resultAndMeta = DTubeParsingHelper.getResultAndMetaFromSteemitContent(
                downloader,
                Words.VIDEO,
                urlHandler.getSteemitParams(getCleanUrl())
        );
        meta = resultAndMeta.getMeta();
        result = resultAndMeta.getResult();
    }

    @Nonnull
    @Override
    public String getId() throws ParsingException {
        return getUrlIdHandler().getId(getCleanUrl());
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getStringFromMetaInfo(meta, Words.TITLE).getData();
    }
}
