package org.schabi.newpipe.extractor.graphql.navigator;


public class GraphQLMixin {
    protected String name;
    protected GraphQLParameters parameters;

    public GraphQLMixin(String name, GraphQLParameters parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public GraphQLParameters getParameters() {
        return parameters;
    }
}
