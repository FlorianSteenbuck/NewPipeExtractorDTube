package org.schabi.newpipe.extractor.graphql;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.graphql.item.GraphQLInfoItem;
import org.schabi.newpipe.extractor.graphql.item.GraphQLInfoItemsCollector;
import org.schabi.newpipe.extractor.graphql.param.GraphQLParam;
import org.schabi.newpipe.extractor.navigator.method.NavigatorPageRequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GraphQLPageMethod<P extends GraphQLParam, C extends GraphQLInfoItemsCollector, I extends GraphQLInfoItem> extends NavigatorPageRequestMethod<P, C, I> {
    @Override
    public String request(P param, Map<String, Object> options) throws IOException {
        return null;
    }

    @Override
    public C getCollector(P param, String body) throws ParsingException {
        return null;
    }

    @Override
    public String nextPage(P param, List<I> items) throws ParsingException {
        return null;
    }
}
