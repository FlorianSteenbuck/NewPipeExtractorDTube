package org.schabi.newpipe.extractor.graphql;

import org.schabi.newpipe.extractor.graphql.item.GraphQLInfoItem;
import org.schabi.newpipe.extractor.graphql.item.GraphQLInfoItemsCollector;
import org.schabi.newpipe.extractor.graphql.param.GraphQLParam;
import org.schabi.newpipe.extractor.navigator.NavigatorPage;

public class GraphQLPage<P extends GraphQLParam, C extends GraphQLInfoItemsCollector, I extends GraphQLInfoItem, M extends GraphQLPageMethod<P, C, I>> extends NavigatorPage<P, C, I, M> {
    public GraphQLPage(P param, M method) {
        super(param, method);
    }
}
