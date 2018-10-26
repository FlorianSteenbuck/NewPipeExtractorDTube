package org.schabi.newpipe.extractor.graphql.navigator.params.type;

public enum GraphQLTypes {
    DOUBLE(),
    NUMBER(),
    INT(),
    STRING(),
    CHARACTER();

    private GraphQLType type;

    GraphQLTypes(GraphQLType type){
        this.type = type;
    }

    public GraphQLType getType() {
        return type;
    }
}
