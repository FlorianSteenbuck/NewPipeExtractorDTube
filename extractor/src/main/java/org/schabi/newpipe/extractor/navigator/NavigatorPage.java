package org.schabi.newpipe.extractor.navigator;

import org.schabi.newpipe.extractor.*;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.navigator.method.NavigatorPageRequestMethod;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import com.github.FlorianSteenbuck.other.url.helper.UrlParsingHelper;
import com.github.FlorianSteenbuck.other.url.model.UrlParsingFeature;
import com.github.FlorianSteenbuck.other.url.model.UrlQuery;
import com.github.FlorianSteenbuck.other.url.navigator.UrlNavigator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigatorPage<P, C extends InfoItemsCollector, I extends InfoItem, M extends NavigatorPageRequestMethod<P, C, I>> {
    protected C currentCollector;
    protected P param;
    protected M method;

    public NavigatorPage(P param, M method) {
        this.param = param;
        this.method = method;
    }

    protected C getCollectorWithParams(Map<String, Object> options) throws IOException, ExtractionException {
        return method.getCollector(param, method.request(param, options));
    }

    protected void ensureCurrentCollectorIsLoaded() throws IOException, ExtractionException {
        if (currentCollector == null) {
            currentCollector = getCollectorWithParams(null);
        }
    }

    public C getInitialPageCollector() throws IOException, ExtractionException {
        ensureCurrentCollectorIsLoaded();
        return currentCollector;
    }

    public ListExtractor.InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        return new ListExtractor.InfoItemsPage<StreamInfoItem>(getInitialPageCollector(), getNextPageUrl());
    }

    public String getNextPageUrl() throws IOException, ExtractionException {
        ensureCurrentCollectorIsLoaded();
        return method.nextPage(param, currentCollector.getItems());
    }

    public ListExtractor.InfoItemsPage<I> getPage(String nextPageUrl) throws IOException, ExtractionException {
        UrlNavigator navi = UrlParsingHelper.parse(
                nextPageUrl,
                Encodings.UTF_8,
                UrlParsingFeature.values()
        );

        List<UrlQuery> publicParams = navi.getPublicParams();
        Map<String, Object> params = new HashMap<String, Object>();
        // TODO move to params package
        if (publicParams.size() > 0) {
            Map<String, List<String>> rawParams = (Map<String, List<String>>) publicParams.get(publicParams.size() - 1);
            for (Map.Entry<String, List<String>> rawParam:rawParams.entrySet()) {
                String key = rawParam.getKey();
                List<String> value = rawParam.getValue();
                int valueSize = value.size();
                if (valueSize > 0) {
                    params.put(key, value.get(valueSize - 1));
                }
            }
        }
        currentCollector = getCollectorWithParams(params);

        return new ListExtractor.InfoItemsPage<I>(currentCollector, getNextPageUrl());
    }
}
