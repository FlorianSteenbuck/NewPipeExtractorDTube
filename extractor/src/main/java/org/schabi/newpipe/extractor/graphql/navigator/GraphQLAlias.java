package org.schabi.newpipe.extractor.graphql.navigator;

public class GraphQLAlias {
    protected String original;
    protected String alias;

    public GraphQLAlias(String original, String alias) {
        this.original = original;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public String getOriginal() {
        return original;
    }
}
