package org.schabi.newpipe.extractor.graphql.navigator.params.type;

public interface GraphQLType<T, PE extends Exception, RE extends Exception> {
    T parse(String value) throws PE;
    String raw(T type) throws RE;
    String[] names();
}
