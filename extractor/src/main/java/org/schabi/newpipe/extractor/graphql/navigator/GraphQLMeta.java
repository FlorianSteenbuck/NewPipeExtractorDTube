package org.schabi.newpipe.extractor.graphql.navigator;

import org.schabi.newpipe.extractor.graphql.navigator.mixin.GraphQLMixin;

public class GraphQLMeta {
    protected String on;
    protected GraphQLKey key;
    protected boolean inline;
    protected GraphQLMixin mixin;

    public GraphQLMeta(String on, GraphQLKey key, boolean inline, GraphQLMixin mixin) {
        this.on = on;
        this.key = key;
        this.inline = inline;
        this.mixin = mixin;
    }

    public boolean gotOn() {
        return on != null;
    }

    public String getOn() {
        return on;
    }

    public GraphQLKey getKey() {
        return key;
    }

    public boolean gotMixin() {
        return mixin != null;
    }

    public GraphQLMixin getMixin() {
        return mixin;
    }

    public boolean isInline() {
        return inline;
    }

    public static GraphQLMeta from(String meta) {
        String on = null;
        String key = "";
        boolean inline = false;
        GraphQLMixin mixin = null;

        boolean collectOn = false;
        String[] parts = GraphQLKey.split(meta, GraphQLNavigator.DELIMITERS, GraphQLNavigator.OPEN_AND_CLOSE_INDICS);
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            boolean last = (i+1) == parts.length;

            if (last && part.startsWith(new String(new char[]{GraphQLNavigator.OPEN_MIXIN}))) {
                mixin = GraphQLMixin.from(part.substring(1));
                continue;
            }

            if (part.startsWith("...")) {
                part = part.substring(part.length()-1);
                collectOn = true;
            }

            if (part.length() == 0) {
                continue;
            }

            if (part.equals("on")) {
                collectOn = true;
                continue;
            }

            if (collectOn) {
                collectOn = false;
                on = part;
            } else {
                key = part;
            }
        }

        return new GraphQLMeta(on, GraphQLKey.from(key), inline, mixin);
    }
}
