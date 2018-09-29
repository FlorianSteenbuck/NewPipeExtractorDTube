package org.schabi.newpipe.extractor.graphql.navigator;

import java.util.*;

import static java.util.Arrays.asList;

public class GraphQLNavigator extends GraphQLNavigateable {
    public static final char OPEN_MIXIN = '@';
    public static final char OPEN_PARAM = '(';
    public static final char CLOSE_PARAM = ')';
    public static final char CLOSE_BODY = '}';
    public static final char OPEN_BODY = '{';
    public static final char ALIAS_INDICATOR = ':';
    public static final char DOT_INDICATOR = '.';

    public static final char[] OPEN_AND_CLOSE_INDICS = {OPEN_MIXIN, OPEN_PARAM, CLOSE_BODY, CLOSE_PARAM, OPEN_BODY};
    public static final char[] DELIMITERS = {' ','\n','\t'};

    protected Map<GraphQLKey, GraphQLNavigateable> rootFragments;

    public GraphQLNavigator(GraphQLParameters parameters, Map<GraphQLKey, GraphQLNavigateable> rootFragments, Map<GraphQLKey, GraphQLNavigateable> rootQuerys, List<GraphQLKey> availData) {
        super(null, parameters, rootQuerys, availData);
        this.rootFragments = rootFragments;
    }

    public enum CollectTyp {
        FRAGMENT,
        QUERY,
        NAVI,
        NO
    }

    public enum CollectLevel {
        BODY,
        PARAM,
        META,
        ROOT
    }

    public class GraphQLNavigateableBuildItem {
        protected GraphQLNavigator navigator = null;
        protected GraphQLNavigateable navigateable = null;
        protected GraphQLKey key;

        public GraphQLNavigateableBuildItem(GraphQLKey key, GraphQLNavigator navigator) {
            this.key = key;
            this.navigateable = navigator;
            this.navigator = navigator;
        }

        public GraphQLNavigateableBuildItem(GraphQLNavigateable navigateable) {
            this.navigateable = navigateable;
        }

        public GraphQLNavigateable getNavigateable() {
            return navigateable;
        }

        public GraphQLNavigator getNavigator() {
            return navigator;
        }

        public GraphQLKey getKey() {
            if (navigateable.getClass() == GraphQLNavigateable.class) {
                return navigateable.getMeta().getKey();
            }
            return key;
        }
    }

    public static GraphQLNavigator from(String query) {
        return from(query, CollectTyp.NO, CollectLevel.ROOT).getNavigator();
    }

    public static GraphQLNavigateableBuildItem from(String query, CollectTyp startTyp, CollectLevel startLevel) {
        boolean needFullNavi = startTyp != CollectTyp.NAVI;

        Map<GraphQLKey, GraphQLNavigateable> rootQuerys = new HashMap<GraphQLKey, GraphQLNavigateable>();
        Map<GraphQLKey, GraphQLNavigateable> rootFragments = new HashMap<GraphQLKey, GraphQLNavigateable>();
        List<GraphQLKey> availData = new ArrayList<GraphQLKey>();

        CollectTyp collectTyp = startTyp;
        CollectLevel collectLevel = startLevel;

        int inlineBodies = 0;
        int inlineBodyIndex = 0;
        List<StringBuilder> inlineBodyCollectors = new ArrayList<StringBuilder>();

        boolean comment = false;

        Map<CollectLevel, String> needParseCollector = new HashMap<CollectLevel, String>(3);
        StringBuilder stringCollect = new StringBuilder();
        List<GraphQLKey> collectAvailData = new ArrayList<GraphQLKey>();

        char[] queryChars = query.toCharArray();
        for (int i = 0; i < queryChars.length; i++) {
            char qchar = queryChars[i];

            boolean isDelimiter = asList(DELIMITERS).contains(qchar);
            boolean continueByNo = (!Character.isLetterOrDigit(qchar)) && collectTyp == CollectTyp.NO;
            boolean continueByOther = (!Character.isLetterOrDigit(qchar)) && (!isDelimiter) &&
                    (!asList(OPEN_AND_CLOSE_INDICS).contains(qchar)) && qchar != ALIAS_INDICATOR &&
                    collectTyp != CollectTyp.NO && qchar != DOT_INDICATOR;

            if (comment && qchar == '\n') {
                comment = false;
                continue;
            }

            if (comment || continueByNo || continueByOther) {
                stringCollect.setLength(0);
                continue;
            }

            if (qchar == '#') {
                comment = true;
                continue;
            }

            switch (collectLevel) {
                case ROOT:
                    stringCollect.append(qchar);
                    String currentStr = stringCollect.toString();

                    boolean isFragment = currentStr.equals("fragment");
                    if (isFragment || currentStr.equals("query") || currentStr.equals("mutation")) {
                        collectTyp = isFragment ? CollectTyp.FRAGMENT : CollectTyp.QUERY;
                        collectLevel = CollectLevel.META;
                        stringCollect.setLength(0);
                    }
                    break;
                case META:
                    boolean openParam = OPEN_PARAM == qchar;
                    if (openParam || OPEN_BODY == qchar) {
                        collectLevel = openParam ? CollectLevel.PARAM : CollectLevel.BODY;
                        if (!openParam) {
                            needParseCollector.put(CollectLevel.PARAM, "");
                        }
                        needParseCollector.put(CollectLevel.META, stringCollect.toString());
                    } else {
                        stringCollect.append(qchar);
                    }
                    break;
                case BODY:
                    if (inlineBodies > 0) {
                        StringBuilder builder = inlineBodyCollectors.get(inlineBodyIndex);
                        builder.append(qchar);
                        inlineBodyCollectors.set(inlineBodyIndex, builder);
                    } else if (isDelimiter) {
                        i = GraphQLKey.charsLenToAlias(query, i, OPEN_AND_CLOSE_INDICS);
                        if (i >= 0) {
                            while (i < queryChars.length || Arrays.binarySearch(DELIMITERS, queryChars[i]) >= 0 || Arrays.binarySearch(OPEN_AND_CLOSE_INDICS, queryChars[i]) >= 0) {
                                stringCollect.append(queryChars[i]);
                                i++;
                            }
                        }
                        availData.add(GraphQLKey.from(stringCollect.toString()));
                        stringCollect.setLength(0);
                        continue;
                    } else {
                        stringCollect.append(qchar);
                    }

                    if (qchar == OPEN_BODY) {
                        inlineBodies++;
                        if (inlineBodies == 1) {
                            inlineBodyCollectors.add(new StringBuilder());
                        }
                    } else if (qchar == CLOSE_BODY) {
                        if (inlineBodies > 0) {
                            if (inlineBodies == 1) {
                                inlineBodyIndex++;
                            }
                            inlineBodies--;
                        }

                        if (inlineBodies == 0) {
                            Map<GraphQLKey, GraphQLNavigateable> querys = new HashMap<GraphQLKey, GraphQLNavigateable>();
                            for (StringBuilder inlineBodyCollector : inlineBodyCollectors) {
                                inlineBodyCollector.setLength(inlineBodyCollector.length() - 1);
                                GraphQLNavigateableBuildItem item = from(inlineBodyCollector.toString(), CollectTyp.NAVI, CollectLevel.META);
                                querys.put(GraphQLKey.from(item.getKey()), item.getNavigateable());
                            }

                            GraphQLParameters parameters = GraphQLParameters.from(needParseCollector.get(CollectLevel.PARAM));
                            GraphQLMeta meta = GraphQLMeta.from(needParseCollector.get(CollectLevel.META));
                            if (collectTyp == CollectTyp.NAVI || collectTyp == CollectTyp.QUERY) {
                                rootQuerys.put(meta.getKey(), new GraphQLNavigateable(meta, parameters, querys, collectAvailData));
                            } else if (collectTyp == CollectTyp.FRAGMENT) {
                                rootFragments.put(meta.getKey(), new GraphQLNavigateable(meta, parameters, querys, collectAvailData));
                            }
                            collectAvailData = new ArrayList<GraphQLKey>();
                        }
                    }
                    break;
                case PARAM:
                    if (CLOSE_PARAM == qchar) {
                        collectLevel = CollectLevel.BODY;
                        needParseCollector.put(CollectLevel.PARAM, stringCollect.toString());
                    } else {
                        stringCollect.append(qchar);
                    }
                    break;
            }
        }

        if (needFullNavi) {
            return new GraphQLNavigateableBuildItem();
        }

        if (rootQuerys.size() > 0) {
            return new GraphQLNavigateableBuildItem();
        } else {
            return new GraphQLNavigateableBuildItem(null);
        }
    }

    public Map<GraphQLKey, GraphQLNavigateable> getRootFragments() {
        return rootFragments;
    }
}
