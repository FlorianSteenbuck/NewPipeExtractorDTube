package org.schabi.newpipe.extractor.graphql.navigator;

import java.util.HashMap;

public class GraphQLParameters extends HashMap<String, Object> {
    public static GraphQLParameters from(String parameterQuery) {
        GraphQLParameters parameters = new GraphQLParameters();

        return parameters;
    }
}
