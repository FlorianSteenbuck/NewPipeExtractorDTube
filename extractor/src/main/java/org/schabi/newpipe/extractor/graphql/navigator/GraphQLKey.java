package org.schabi.newpipe.extractor.graphql.navigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphQLKey {
    protected GraphQLAlias alias = null;
    protected String name = null;

    public GraphQLKey(GraphQLAlias alias) {
        this.alias = alias;
    }

    public GraphQLKey(String name) {
        this.name = name;
    }

    public boolean isAlias() {
        return alias != null;
    }

    public GraphQLAlias getAlias() {
        return alias;
    }

    public String getName() {
        return isAlias() ? alias.getOriginal() : name;
    }

    public static GraphQLKey from(String key) {
        String[] aliasSplit = key.split(":", 1);
        if (aliasSplit.length == 2) {
            String alias = aliasSplit[0].trim();
            String original = aliasSplit[1].trim();
            return new GraphQLKey(new GraphQLAlias(original, alias));
        }
        return new GraphQLKey(key);
    }

    public static String[] split(String toSplit, char[] delimiters, char[] stops) {
        List<String> result = new ArrayList<String>();
        StringBuilder currentResult = new StringBuilder();
        char[] chars = toSplit.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char _char = chars[i];
            boolean isDelimiter = Arrays.binarySearch(delimiters, _char) >= 0;

            if (isDelimiter) {
                i = charsLenToAlias(toSplit, i, stops);
                if (i >= 0) {
                    while (i < chars.length || Arrays.binarySearch(delimiters, chars[i]) >= 0 || Arrays.binarySearch(stops, chars[i]) >= 0) {
                        currentResult.append(chars[i]);
                        i++;
                    }
                }
                result.add(currentResult.toString());
                continue;
            }

            currentResult.append(_char);
        }
        return result.toArray(new String[0]);
    }

    public static int charsLenToAlias(String completeStr, int start, char[] stops) {
        char[] chars = completeStr.toCharArray();
        for (int i = start; i < chars.length; i++) {
            char _char = chars[i];
            boolean isStop = Arrays.binarySearch(stops, _char) >= 0;
            if (_char == ':') {
                return i;
            }
            if (isStop) {
                return -1;
            }
        }
        return -1;
    }
}
