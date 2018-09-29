package org.schabi.newpipe.extractor.services.dtube;

import com.grack.nanojson.*;
import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.constants.Keys;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.services.steemit.SteemitParsingHelper;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DTubeParsingHelper {
    public static final String DOMAIN_ENDPOINT = "https://d.tube";
    public static final String DIRECT_FILES_ENDPOINT = DOMAIN_ENDPOINT+"/DTube_files";

    public static class DTubeResultAndMeta {
        private JsonObject result;
        private JsonObject meta;

        public DTubeResultAndMeta(JsonObject result, JsonObject meta) {
            this.result = result;
            this.meta = meta;
        }

        public JsonObject getResult() {
            return result;
        }

        public JsonObject getMeta() {
            return meta;
        }
    }

    public static class DTubePathReturnData<D> {
        private String path;
        private D data;

        public DTubePathReturnData(String path, D data) {
            this.path = path;
            this.data = data;
        }

        public D getData() {
            return data;
        }

        public String getPath() {
            return path;
        }
    }

    public static long getLongTimeJson(JsonObject json, String path, String stringedDataKey, String dateFormat, SimpleDateFormat dateFormatter) throws ParsingException {
        if (json.has(stringedDataKey)) {
            if (json.isString(stringedDataKey)) {
                try {
                    return dateFormatter.parse(json.getString(stringedDataKey)).getTime();
                } catch (ParseException e) {
                    throw new ParsingException("Can not parse date pattern \""+dateFormat+"\"", e);
                }
            } else {
                throw new ParsingException(path+"."+ stringedDataKey +" is not a number");
            }
        }
        throw new ParsingException(path+" got no "+ stringedDataKey +" object");
    }

    public static DTubePathReturnData<JsonObject> getInfoObject(JsonObject meta, String rootPath) throws ParsingException {
        String failSafeRootPath = rootPath == null ? Words.ANONYMOUS : rootPath;
        String path = failSafeRootPath+"."+ Words.INFO;
        if (meta.has(Words.INFO)) {
            return new DTubePathReturnData<JsonObject>(path, meta.getObject(Words.INFO));
        }
        throw new ParsingException(path+" do not exists");
    }

    public static DTubePathReturnData<JsonObject> getContentObject(JsonObject meta, String rootPath) throws ParsingException {
        String failSafeRootPath = rootPath == null ? Words.ANONYMOUS : rootPath;
        String path = failSafeRootPath+"."+Words.CONTENT;
        if (meta.has(Words.CONTENT)) {
            return new DTubePathReturnData<JsonObject>(path, meta.getObject(Words.CONTENT));
        }
        throw new ParsingException(path+" do not exists");
    }

    public static DTubePathReturnData<String> getStringFromMetaInfo(JsonObject meta, String key) throws ParsingException {
        DTubePathReturnData<JsonObject> args = getInfoObject(meta,null);
        return getStringFromJson(args.getPath(), args.getData(), key);
    }

    public static DTubePathReturnData<String> getStringFromJson(String path, JsonObject json, String key) throws ParsingException {
        if (json.has(key)) {
            path += "."+key;
            if (json.isString(key)) {
                return new DTubePathReturnData<String>(path, json.getString(key));
            } else {
                throw new ParsingException(path+" is not string");
            }
        }
        throw new ParsingException(path+" got no "+key+" object");
    }
}
