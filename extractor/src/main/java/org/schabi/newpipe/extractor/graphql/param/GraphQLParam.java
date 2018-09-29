package org.schabi.newpipe.extractor.graphql.param;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class GraphQLParam {
    protected String query;
    protected String operationName;
    protected Map<String, Object> variables;
    protected Collection<GraphQLParam> extensions;

    public GraphQLParam(String query, String operationName, Map<String, Object> variables, Collection<GraphQLParam> extensions) {
        this.query = query;
        this.operationName = operationName;
        this.variables = variables;
        if (extensions == null) {
            this.extensions = new LinkedList<>();
            return;
        }
        this.extensions = extensions;
    }

    public Collection<GraphQLParam> getExtensions() {
        return extensions;
    }

    public String getOperationName() {
        return operationName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
