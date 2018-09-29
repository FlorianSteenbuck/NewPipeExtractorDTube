package org.schabi.newpipe.extractor.navigator.method;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/*
 * NavigatorPageRequestMethod - a custom request method for parsing navigator sites
 */
public interface NavigatorPageRequestMethod<P, C extends InfoItemsCollector, I extends InfoItem> {
    String request(P param, Map<String, Object> options) throws IOException;
    C getCollector(P param, String body) throws ParsingException;
    String nextPage(P param, List<I> items) throws ParsingException;
}
