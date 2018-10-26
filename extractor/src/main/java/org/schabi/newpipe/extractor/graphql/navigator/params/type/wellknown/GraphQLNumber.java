package org.schabi.newpipe.extractor.graphql.navigator.params.type.wellknown;

import org.schabi.newpipe.extractor.exceptions.StackException;
import org.schabi.newpipe.extractor.graphql.navigator.params.type.GraphQLType;

import java.util.ArrayList;
import java.util.List;

public class GraphQLNumber implements GraphQLType<Number, StackException, Exception> {
    @Override
    public Number parse(String value) throws StackException {
        List<NumberFormatException> exceptions = new ArrayList<NumberFormatException>();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            exceptions.add(ex);
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex1) {
                exceptions.add(ex);
                throw new StackException(exceptions.toArray(new Exception[0]));
            }
        }
    }

    @Override
    public String raw(Number type) {
        if (type instanceof Integer) {
            return ((Integer) type).toString();
        } else if (type instanceof Double) {
            return ((Double) type).toString();
        }
        return null;
    }

    @Override
    public String[] names() {
        return new String[]{"Number"};
    }
}
