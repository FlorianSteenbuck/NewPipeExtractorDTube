package org.schabi.newpipe.extractor.graphql.navigator;

import java.util.List;
import java.util.Map;

public class GraphQLNavigateable {
    protected Map<GraphQLKey, GraphQLNavigateable> rootQuerys;
    protected List<GraphQLKey> availAt;
    protected GraphQLParameters parameters;
    protected GraphQLMeta meta;

    public GraphQLNavigateable(GraphQLMeta meta, GraphQLParameters parameters, Map<GraphQLKey, GraphQLNavigateable> rootQuerys, List<GraphQLKey> availAt) {
        this.meta = meta;
        this.parameters = parameters;
        this.rootQuerys = rootQuerys;
        this.availAt = availAt;
    }

    public GraphQLMeta getMeta() {
        return meta;
    }

    public GraphQLParameters getParameters() {
        return parameters;
    }

    public Map<GraphQLKey, GraphQLNavigateable> getRootQuerys() {
        return rootQuerys;
    }

    public List<GraphQLKey> getAvailAt() {
        return availAt;
    }

    public static GraphQLNavigateable from(String query) {
        return GraphQLNavigator.from(query, GraphQLNavigator.CollectTyp.NO, GraphQLNavigator.CollectLevel.BODY).getNavigateable();
    }

}
