package org.schabi.newpipe.extractor.services.steemit;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;
import com.grack.nanojson.*;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.constants.Keys;
import org.schabi.newpipe.extractor.constants.Versions;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.services.dtube.DTubeParsingHelper;
import org.schabi.newpipe.extractor.services.dtube.DTubeSettings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class SteemitParsingHelper {
    public static final String STEEMIT_ENDPOINT = "https://api.steemit.com";
    public static final String STEEMIT_IMG_ENDPOINT = "https://steemitimages.com";

    public static final String PROFIL_SIZE_STEEMIT = "/128x128/";
    public static final String BANNER_SIZE_STEEMIT = "/2048x512/";
    private static int id = -1;

    public static String getSteemitRequest(String method, Object[] params) {
        JsonStringWriter steemitRequestsWriter = JsonWriter.string().object();
        steemitRequestsWriter.value(Words.ID, getId());
        steemitRequestsWriter.value(Words.JSONRPC, Versions.SECOND_ONE_DOT);
        steemitRequestsWriter.value(Words.METHOD, method);
        steemitRequestsWriter.value(Words.PARAMS, params);
        return steemitRequestsWriter.end().done();
    }

    public static String getThumbnail(JsonObject meta, StreamingService service) throws ParsingException {
        DTubeParsingHelper.DTubePathReturnData<JsonObject> args = DTubeParsingHelper.getInfoObject(meta, null);
        JsonObject info = args.getData();
        String path = args.getPath();
        if (info.has(Words.SNAPHASH)) {
            path += "."+Words.SNAPHASH;
            if (info.isString(Words.SNAPHASH)) {
                String thumbnailIdHash = info.getString(Words.SNAPHASH);
                if (!thumbnailIdHash.trim().isEmpty()) {
                    Settings settings = service.getSettings();
                    if (settings.has(DTubeSettings.ID_IPFS_GATEWAYS)) {
                        List<String> gateways = (List<String>) settings.get(DTubeSettings.ID_IPFS_GATEWAYS);
                        if (gateways.size() > 0) {
                            return gateways.get(0) + "/" + thumbnailIdHash;
                        }
                    }
                    throw new ParsingException("Their is no gateway in the settings for resolve the thumbnail");
                }
                throw new ParsingException("Thumbnail id is not valid (no empty (spaces))");
            }
            throw new ParsingException("\""+ path +"\" is no String");
        }
        throw new ParsingException("\""+ path +"\" got no key \""+ Words.SNAPHASH +"\"");
    }

    public static DTubeParsingHelper.DTubeResultAndMeta getResultAndMetaFromSteemitContent(HttpDownloader downloader, String typeKey, Object[] params) throws ParsingException, UnsupportedEncodingException, IOException, ReCaptchaException {
        return getResultAndMetaFromSteemitContent(downloader, "get_content", typeKey, params);
    }

    public static DTubeParsingHelper.DTubeResultAndMeta getResultAndMetaFromSteemitContent(HttpDownloader downloader, String callName, String typeKey, Object[] params) throws ParsingException, UnsupportedEncodingException, IOException, ReCaptchaException {
        JsonObject metaData;
        JsonObject resultData = null;

        String strRequestBody = getSteemitRequest(callName, params);
        byte[] requestBody = strRequestBody.getBytes(Encodings.UTF_8);
        String strResponse = downloader.download(STEEMIT_ENDPOINT, requestBody);
        try {
            JsonObject response = JsonParser.object().from(strResponse);
            if (response.has(Words.RESULT)) {
                Object data = response.get(Words.RESULT);
                if (data instanceof JsonArray) {
                    JsonArray arr = (JsonArray) data;
                    if (arr.size() > 0) {
                        boolean gotJsonObject = false;
                        for (Object obj:arr) {
                            if (obj instanceof JsonObject) {
                                resultData = (JsonObject) obj;
                                gotJsonObject = true;
                                break;
                            }
                        }

                        if (!gotJsonObject) {
                            throw new ParsingException("none of the result array entries '"+arr.toString()+"' is a object");
                        }
                    } else {
                        throw new ParsingException("the result array '"+arr.toString()+"' is empty or broken");
                    }
                } else if (data instanceof JsonObject) {
                    resultData = (JsonObject) data;
                }

                if (resultData == null) {
                    throw new ParsingException("result '"+resultData.toString()+"' is not a object and not a array");
                }

                if (resultData.has(Keys.JSON_METADATA) && resultData.isString(Keys.JSON_METADATA)) {
                    try {
                        metaData = JsonParser.object().from(resultData.getString(Keys.JSON_METADATA));
                        if (metaData.has(typeKey)) {
                            if (metaData.get(typeKey) instanceof JsonObject) {
                                metaData = metaData.getObject(typeKey);
                            } else {
                                throw new ParsingException("\""+typeKey+"\" endpoint is not a object");
                            }
                        } else {
                            throw new ParsingException("The meta data need to got a \""+typeKey+"\" endpoint");
                        }
                    } catch (JsonParserException e) {
                        throw new ParsingException("Could not parse meta json", e);
                    }
                } else {
                    throw new ParsingException("result \""+resultData.toString()+"\" has no json_metadata");
                }
            } else {
                throw new ParsingException("response \""+response.toString()+"\" has no result");
            }
        } catch (JsonParserException e) {
            throw new ParsingException("Could not parse result json", e);
        }

        return new DTubeParsingHelper.DTubeResultAndMeta(resultData, metaData);
    }

    public synchronized static int getId() {
        id++;
        return id;
    }
}
