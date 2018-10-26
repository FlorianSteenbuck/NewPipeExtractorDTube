package org.schabi.newpipe.extractor.graphql.navigator.params.type;

import org.schabi.newpipe.extractor.exceptions.ParsingException;

public class GraphQLUnknownType<T> implements GraphQLType<T, ParsingException, ParsingException> {
    protected String[] names;

    public GraphQLUnknownType(String...names) {
        this.names = names;
    }

    @Override
    public String[] names() {
        return names;
    }

    @Override
    public String raw(T type) throws ParsingException {
        throw new ParsingException("The type is currently unsupported");
    }

    @Override
    public T parse(String value) throws ParsingException {
        throw new ParsingException("The type is currently unsupported");
    }
}
