package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.NewPipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.schabi.newpipe.extractor.ServiceList.DTube;

/**
 * Test for {@link SearchEngine}
 */
/*
public class DTubeSearchEngineStreamTest extends BaseDTubeSearchTest {

    @BeforeClass
    public static void setUp() throws Exception {
        NewPipe.init(Downloader.getInstance());
        SearchEngine engine = DTube.getSearchEngine();
        // https://api.asksteem.com/search?q=meta.video.info.title%3A*%20AND%20lill%20uzi%20vert&include=meta%2Cpayout
        result = engine.search("lill uzi vert", 0, "de", SearchEngine.Filter.STREAM)
                .getSearchResult();
    }

    @Test
    public void testResultsItemType() {
        for (InfoItem infoItem : result.resultList) {
            assertEquals(InfoItem.InfoType.STREAM, infoItem.getInfoType());
        }
    }

    @Ignore
    @Test
    public void testSuggestion() {
        //todo write a real test
        assertTrue(result.suggestion != null);
    }
}*/
