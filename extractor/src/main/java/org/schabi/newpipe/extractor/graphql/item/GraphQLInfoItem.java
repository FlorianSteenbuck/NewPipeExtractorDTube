package org.schabi.newpipe.extractor.graphql.item;

import org.schabi.newpipe.extractor.InfoItem;

public class GraphQLInfoItem extends InfoItem {
    public GraphQLInfoItem(InfoType infoType, int serviceId, String url, String name) {
        super(infoType, serviceId, url, name);
    }
}
