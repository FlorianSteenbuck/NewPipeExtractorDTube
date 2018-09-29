package org.schabi.newpipe.extractor.graphql.item;

import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

public class GraphQLInfoItemsCollector extends InfoItemsCollector<GraphQLInfoItem, GraphQLInfoItemExtractor> {
    public GraphQLInfoItemsCollector(int serviceId) {
        super(serviceId);
    }

    @Override
    public GraphQLInfoItem extract(GraphQLInfoItemExtractor extractor) throws ParsingException {
        return null;
    }
}
