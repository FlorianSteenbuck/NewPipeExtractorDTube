package org.schabi.newpipe.extractor.graphql.navigator.mixin;

import org.schabi.newpipe.extractor.graphql.item.GraphQLInfoItem;

import java.util.Map;

public class GraphQLExclude extends GraphQLMixin {
    private static final GraphQLMixin instance = new GraphQLExclude();

    public GraphQLExclude() {
        super("exclude");
    }

    public static GraphQLMixin getInstance() {
        return instance;
    }

    @Override
    public GraphQLInfoItem[] include(Map<String, Object> parameters, GraphQLInfoItem item) {
        if (parameters.containsKey("if") && parameters.get("if") instanceof Boolean && ((Boolean) parameters.get("if"))) {
            return new GraphQLInfoItem[]{};
        }
        return new GraphQLInfoItem[]{item};
    }
}
