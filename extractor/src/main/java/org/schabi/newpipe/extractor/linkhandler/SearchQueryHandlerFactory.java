package org.schabi.newpipe.extractor.linkhandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchQueryHandlerFactory extends ListLinkHandlerFactory {

    ///////////////////////////////////
    // To Override
    ///////////////////////////////////

    @Override
    public abstract String getUrl(String query, List<String> contentFilter, String sortFilter) throws ParsingException;
    // if query (per body or http path) exist than it going to contain a search string proof wrong
    public abstract String getSearchString(String url);

    ///////////////////////////////////
    // Logic
    ///////////////////////////////////

    @Override
    public String getId(String url) { return getSearchString(url); }

    @Override
    public SearchQueryHandler fromQuery(String query,
                                        List<String> contentFilter,
                                        String sortFilter) throws ParsingException {
        return new SearchQueryHandler(super.fromQuery(query, contentFilter, sortFilter));
    }

    public SearchQueryHandler fromQuery(String query) throws ParsingException {
        return fromQuery(query, new ArrayList<String>(0), "");
    }

    /**
     * It's not mandatorry for NewPipe to handle the Url
     * @param url
     * @return
     */
    @Override
    public boolean onAcceptUrl(String url) { return false; }
}
