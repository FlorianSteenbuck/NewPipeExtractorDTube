package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.BaseChannelExtractorTest;

import static org.junit.Assert.*;
import static org.schabi.newpipe.extractor.ExtractorAsserts.assertEmpty;
import static org.schabi.newpipe.extractor.ExtractorAsserts.assertIsSecureUrl;
import static org.schabi.newpipe.extractor.ServiceList.DTube;
import static org.schabi.newpipe.extractor.services.DefaultTests.*;

/**
 * Test for {@link DTubeChannelExtractor}
 */
public class DTubeChannelExtractorTest {
    public static class SemperVideo implements BaseChannelExtractorTest {
        private static DTubeChannelExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            NewPipe.init(Downloader.getInstance());
            extractor = (DTubeChannelExtractor) DTube
                    .getChannelExtractor("https://d.tube/#!/c/sempervideo");
            extractor.fetchPage();
        }

        /*//////////////////////////////////////////////////////////////////////////
        // Extractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testServiceId() {
            assertEquals(DTube.getServiceId(), extractor.getServiceId());
        }

        @Test
        public void testName() throws ParsingException {
            assertEquals("SemperVideo", extractor.getName());
        }

        @Test
        public void testId() throws ParsingException {
            assertEquals("sempervideo", extractor.getId());
        }

        @Test
        public void testUrl()  throws ParsingException {
            assertEquals("https://d.tube/c/sempervideo", extractor.getUrl());
        }

        @Test
        public void testOriginalUrl()  throws ParsingException {
            assertEquals("https://d.tube/#!/c/sempervideo", extractor.getOriginalUrl());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // ListExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor, DTube.getServiceId());
        }

        @Test
        public void testMoreRelatedItems() throws Exception {
            defaultTestMoreItems(extractor, DTube.getServiceId());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // ChannelExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testDescription() throws ParsingException {
            assertNotNull(extractor.getDescription());
        }

        @Test
        public void testAvatarUrl() throws ParsingException {
            assertIsSecureUrl(extractor.getAvatarUrl());
        }

        @Test
        public void testBannerUrl() throws ParsingException {
            assertIsSecureUrl(extractor.getBannerUrl());
        }

        @Test
        public void testFeedUrl() throws ParsingException {
            assertEmpty(extractor.getFeedUrl());
        }

        @Test
        public void testSubscriberCount() throws ParsingException {
            assertTrue("Wrong subscriber count", extractor.getSubscriberCount() >= 5000);
        }
    }
}
