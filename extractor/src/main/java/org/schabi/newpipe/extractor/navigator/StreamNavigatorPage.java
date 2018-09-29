package org.schabi.newpipe.extractor.navigator;

import org.schabi.newpipe.extractor.navigator.method.NavigatorPageRequestMethod;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;

public class StreamNavigatorPage<P, M extends NavigatorPageRequestMethod<P, StreamInfoItemsCollector, StreamInfoItem>> extends NavigatorPage<P, StreamInfoItemsCollector, StreamInfoItem, M> {
    public StreamNavigatorPage(P param, M method) {
        super(param, method);
    }
}
