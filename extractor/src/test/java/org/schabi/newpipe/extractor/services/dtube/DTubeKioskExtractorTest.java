package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import java.util.List;

import static org.junit.Assert.*;
import static org.schabi.newpipe.extractor.ServiceList.DTube;

/**
 * Test for {@link DTubeKioskExtractor}
 */
public class DTubeKioskExtractorTest {

    static KioskExtractor extractor;

    @BeforeClass
    public static void setUp() throws Exception {
        NewPipe.init(Downloader.getInstance());
        extractor = DTube
                .getKioskList()
                .getExtractorById("hotvideos", null);
        extractor.fetchPage();
    }

    @Test
    public void testGetDownloader() throws Exception {
        assertNotNull(NewPipe.getDownloader());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(extractor.getName(), "Hot");
    }

    @Test
    public void testId() {
        assertEquals(extractor.getId(), "hotvideos");
    }

    @Test
    public void testGetStreams() throws Exception {
        ListExtractor.InfoItemsPage<StreamInfoItem> page = extractor.getInitialPage();
        if(!page.getErrors().isEmpty()) {
            System.err.println("----------");
            List<Throwable> errors = page.getErrors();
            for(Throwable e: errors) {
                e.printStackTrace();
                System.err.println("----------");
            }
        }
        assertTrue("no streams are received",
                !page.getItems().isEmpty()
                        && page.getErrors().isEmpty());
    }

    @Test
    public void testGetStreamsErrors() throws Exception {
        assertTrue("errors during stream list extraction", extractor.getInitialPage().getErrors().isEmpty());
    }

    @Test
    public void testHasMoreStreams() throws Exception {
        // Setup the streams
        extractor.getInitialPage();
        assertTrue("has more streams", extractor.hasNextPage());
    }

    @Test
    public void testGetNextPageUrl() throws Exception {
        assertTrue(extractor.hasNextPage());
    }

    @Test
    public void testGetNextPage() throws Exception {
        assertFalse("extractor has next streams", extractor.getPage(extractor.getNextPageUrl()) == null
                || extractor.getPage(extractor.getNextPageUrl()).getItems().isEmpty());
    }
}
