package org.schabi.newpipe.extractor.graphql.navigator.params.type.wellknown;

import org.schabi.newpipe.extractor.graphql.navigator.params.type.GraphQLType;

public class GraphQLInt implements GraphQLType<Integer, NumberFormatException, Exception> {
    @Override
    public Integer parse(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    @Override
    public String raw(Integer type) {
        return type.toString();
    }

    @Override
    public String[] names() {
        return new String[]{"Int","Integer"};
    }
}
