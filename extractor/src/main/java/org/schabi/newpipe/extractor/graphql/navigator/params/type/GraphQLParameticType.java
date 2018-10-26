package org.schabi.newpipe.extractor.graphql.navigator.params.type;

public abstract class GraphQLParameticType<T, PE extends Exception, RE extends Exception, VE extends Exception> implements GraphQLType<T, PE, RE> {
    protected Object[] parameters;

    public GraphQLParameticType(Object...parameters) throws VE {
        setParameters(parameters);
    }

    public abstract void validateParameters(Object...parameters) throws VE;

    public void setParameters(Object...parameters) throws VE {
        validateParameters(parameters);
        this.parameters = parameters;
    }
}
