package org.schabi.newpipe.extractor.graphql.navigator.params.type.wellknown;

import org.schabi.newpipe.extractor.graphql.navigator.params.type.GraphQLType;

public class GraphQLDouble implements GraphQLType<Double, NumberFormatException, Exception> {
    @Override
    public Double parse(String value) throws NumberFormatException {
        return Double.parseDouble(value);
    }

    @Override
    public String raw(Double type) {
        return type.toString();
    }

    @Override
    public String[] names() {
        return new String[]{"Double", "Float"};
    }
}
