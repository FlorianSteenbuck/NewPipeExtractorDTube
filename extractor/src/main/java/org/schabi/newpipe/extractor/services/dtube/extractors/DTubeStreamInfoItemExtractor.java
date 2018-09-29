package org.schabi.newpipe.extractor.services.dtube.extractors;

import com.grack.nanojson.JsonObject;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.dtube.DTubeParsingHelper;
import org.schabi.newpipe.extractor.services.steemit.SteemitParsingHelper;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.schabi.newpipe.extractor.services.dtube.extractors.DTubeStreamExtractor.DATE_FORMAT;
import static org.schabi.newpipe.extractor.services.dtube.extractors.DTubeStreamExtractor.DATE_FORMATTER;

public class DTubeStreamInfoItemExtractor implements StreamInfoItemExtractor {
    protected JsonObject rootObject;
    protected JsonObject videoMetaData;
    protected StreamingService service;

    public DTubeStreamInfoItemExtractor(StreamingService service, JsonObject videoMetaData, JsonObject rootObject) {
        this.videoMetaData = videoMetaData;
        this.rootObject = rootObject;
        this.service = service;
    }

    @Override
    public StreamType getStreamType() throws ParsingException {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public boolean isAd() throws ParsingException {
        return false;
    }

    @Override
    public long getDuration() throws ParsingException {
        DTubeParsingHelper.DTubePathReturnData<JsonObject> args = DTubeParsingHelper.getInfoObject(videoMetaData,null);
        JsonObject info = args.getData();
        String path = args.getPath();

        if (info.has(Words.DURATION)) {
            if (info.isNumber(Words.DURATION)) {
                return new Double(info.getNumber(Words.DURATION).doubleValue()*1000).longValue();
            } else if (info.isString(Words.DURATION)) {
                Number numberLong = null;
                try {
                    numberLong = Double.parseDouble(info.getString(Words.DURATION));
                } catch (NumberFormatException ex) {
                    try {
                        numberLong = Integer.parseInt(info.getString(Words.DURATION));
                    } catch (NumberFormatException ex1) {
                        StringBuilder stackBuilder = new StringBuilder("[0]").append(ex.getMessage());
                        for (StackTraceElement exElTrace:ex.getStackTrace()) {
                            stackBuilder.append(new StackTraceElement(exElTrace.getClassName(), exElTrace.getMethodName(), "[0] " + exElTrace.getFileName(), exElTrace.getLineNumber())).append('\n');
                        }
                        stackBuilder.append("\n[1]").append(ex1.getMessage());
                        for (StackTraceElement exElTrace:ex1.getStackTrace()) {
                            stackBuilder.append(new StackTraceElement(exElTrace.getClassName(), exElTrace.getMethodName(), "[1] " + exElTrace.getFileName(), exElTrace.getLineNumber())).append('\n');
                        }
                        throw new ParsingException(stackBuilder.toString());
                    }
                }
                return numberLong.longValue();
            }
            throw new ParsingException(path+"."+Words.DURATION+" is not a number");
        }
        throw new ParsingException(path+" got no "+ Words.DURATION +" object");
    }

    @Override
    public long getViewCount() throws ParsingException {
        // NEED SUPPORT low priority on steemit https://github.com/steemit/condenser/issues/812
        return -1;
    }

    @Override
    public String getUploaderName() throws ParsingException {
        return DTubeParsingHelper.getStringFromMetaInfo(videoMetaData,"author").getData();
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        return service.getChannelUIHFactory().fromId("@"+getUploaderName()).getUrl();
    }

    @Override
    public String getUploadDate() throws ParsingException {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newDateFormat.format(new Date(DTubeParsingHelper.getLongTimeJson(rootObject, "root", "created", DATE_FORMAT, DATE_FORMATTER)));
    }

    @Override
    public String getName() throws ParsingException {
        return DTubeParsingHelper.getStringFromMetaInfo(videoMetaData,"title").getData();
    }

    @Override
    public String getUrl() throws ParsingException {
        return service.getStreamUIHFactory().fromId(getUploaderName()+"/"+getPermLink()).getUrl();
    }

    public String getPermLink() throws ParsingException {
        return DTubeParsingHelper.getStringFromMetaInfo(videoMetaData,"permlink").getData();
    }

    @Override
    public String getThumbnailUrl() throws ParsingException {
        return SteemitParsingHelper.getThumbnail(videoMetaData, service);
    }
}
