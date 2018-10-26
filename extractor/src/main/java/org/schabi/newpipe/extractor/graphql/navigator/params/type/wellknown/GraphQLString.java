package org.schabi.newpipe.extractor.graphql.navigator.params.type.wellknown;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.graphql.navigator.params.type.GraphQLParameticType;

public class GraphQLString extends GraphQLParameticType<String, ParsingException, Exception, Exception> {
    public GraphQLString(Object... parameters) throws Exception {
        super(parameters);
    }

    @Override
    public String parse(String value) throws ParsingException {
        return null;
    }

    @Override
    public String raw(String type) throws Exception {
        return null;
    }

    @Override
    public String[] names() {
        return new String[]{"Str","String"};
    }

    @Override
    public void validateParameters(Object... parameters) throws Exception {
        if (parameters == null || (!(parameters.length > 0))) {
            return;
        }
        if (parameters[0] instanceof Character) {

        }
    }
}
