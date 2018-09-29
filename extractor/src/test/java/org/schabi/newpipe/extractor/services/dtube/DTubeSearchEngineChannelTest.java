package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.schabi.newpipe.extractor.ServiceList.DTube;

/**
 * Test for {@link SearchEngine}
 */
/*
public class DTubeSearchEngineChannelTest extends BaseDTubeSearchTest {

    @BeforeClass
    public static void setUp() throws Exception {
        NewPipe.init(Downloader.getInstance());
        SearchEngine engine = DTube.getSearchEngine();

        result = engine.search("sempervideo", 0, "de", SearchEngine.Filter.CHANNEL)
                .getSearchResult();
    }

    @Test
    public void testResultsItemType() {
        for (InfoItem infoItem : result.resultList) {
            if (infoItem.getInfoType() == InfoItem.InfoType.STREAM) {
                assertEquals("https://d.tube/c/sempervideo", ((StreamInfoItem) infoItem).getUploaderUrl());
            }
        }
    }

    @Ignore
    @Test
    public void testSuggestion() {
        //TODO write a real test
        assertTrue(result.suggestion != null);
    }
}*/
