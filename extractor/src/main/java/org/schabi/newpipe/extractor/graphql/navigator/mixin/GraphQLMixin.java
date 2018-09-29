package org.schabi.newpipe.extractor.graphql.navigator.mixin;


import org.schabi.newpipe.extractor.graphql.item.GraphQLInfoItem;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class GraphQLMixin {
    private static final Class<? extends GraphQLMixin>[] mixins = new Class[]{GraphQLExclude.class, GraphQLInclude.class};
    protected String name;

    public GraphQLMixin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract GraphQLInfoItem[] include(Map<String, Object> parameters, GraphQLInfoItem item);

    public static GraphQLMixin from(String name) {
        for (Class<? extends GraphQLMixin> mixin:mixins) {
            try {
                ((GraphQLMixin)mixin.getDeclaredMethod("getInstance").invoke(null)).getName().equals(name);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                // TODO better error handling
            }
        }
        return null;
    }
}
