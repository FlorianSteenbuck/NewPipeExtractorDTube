package org.schabi.newpipe.extractor.services.youtube;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.services.BasePlaylistExtractorTest;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.schabi.newpipe.extractor.ExtractorAsserts.assertIsSecureUrl;
import static org.schabi.newpipe.extractor.ServiceList.YouTube;
import static org.schabi.newpipe.extractor.services.DefaultTests.*;

/**
 * Test for {@link YoutubePlaylistExtractor}
 */
public class YoutubePlaylistExtractorTest {
    public static class TimelessPopHits implements BasePlaylistExtractorTest {
        private static YoutubePlaylistExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            NewPipe.init(Downloader.getInstance());
            extractor = (YoutubePlaylistExtractor) YouTube
                    .getPlaylistExtractor("http://www.youtube.com/watch?v=lp-EO5I60KA&list=PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj");
            extractor.fetchPage();
        }

        /*//////////////////////////////////////////////////////////////////////////
        // Extractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor.getServiceId());
        }

        @Test
        public void testName() throws Exception {
            String name = extractor.getName();
            assertTrue(name, name.startsWith("Pop Music Playlist"));
        }

        @Test
        public void testId() throws Exception {
            assertEquals("PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj", extractor.getId());
        }

        @Test
        public void testUrl() throws ParsingException {
            assertEquals("https://www.youtube.com/playlist?list=PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj", extractor.getUrl());
        }

        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals("http://www.youtube.com/watch?v=lp-EO5I60KA&list=PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj", extractor.getOriginalUrl());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // ListExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor, YouTube.getServiceId());
        }

        @Test
        public void testMoreRelatedItems() throws Exception {
            defaultTestMoreItems(extractor, ServiceList.YouTube.getServiceId());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // PlaylistExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testThumbnailUrl() throws Exception {
            final String thumbnailUrl = extractor.getThumbnailUrl();
            assertIsSecureUrl(thumbnailUrl);
            assertTrue(thumbnailUrl, thumbnailUrl.contains("yt"));
        }

        @Ignore
        @Test
        public void testBannerUrl() throws Exception {
            final String bannerUrl = extractor.getBannerUrl();
            assertIsSecureUrl(bannerUrl);
            assertTrue(bannerUrl, bannerUrl.contains("yt"));
        }

        @Test
        public void testUploaderUrl() throws Exception {
            assertTrue(extractor.getUploaderUrl().contains("youtube.com"));
        }

        @Test
        public void testUploaderName() throws Exception {
            final String uploaderName = extractor.getUploaderName();
            assertTrue(uploaderName, uploaderName.contains("Just Hits"));
        }

        @Test
        public void testUploaderAvatarUrl() throws Exception {
            final String uploaderAvatarUrl = extractor.getUploaderAvatarUrl();
            assertTrue(uploaderAvatarUrl, uploaderAvatarUrl.contains("yt"));
        }

        @Test
        public void testStreamCount() throws Exception {
            assertTrue("Error in the streams count", extractor.getStreamCount() > 100);
        }
    }

    public static class ImportantVideos implements BasePlaylistExtractorTest {
        private static YoutubePlaylistExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            NewPipe.init(Downloader.getInstance());
            extractor = (YoutubePlaylistExtractor) YouTube
                    .getPlaylistExtractor("https://www.youtube.com/watch?v=lH1caqoAGe0&list=PL45xb3ujEhqUexNt53jb9WT2mS-uUaUrn");
            extractor.fetchPage();
        }

        /*//////////////////////////////////////////////////////////////////////////
        // Additional Testing
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testGetPageInNewExtractor() throws Exception {
            final PlaylistExtractor newExtractor = YouTube.getPlaylistExtractor(extractor.getUrl());
            defaultTestGetPageInNewExtractor(extractor, newExtractor, YouTube.getServiceId());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // Extractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor.getServiceId());
        }

        @Test
        public void testName() throws Exception {
            String name = extractor.getName();
            assertTrue(name, name.contains("Korrekte Aussprache - Lektion 1"));
        }

        @Test
        public void testId() throws Exception {
            assertEquals("PL45xb3ujEhqUexNt53jb9WT2mS-uUaUrn", extractor.getId());
        }

        @Test
        public void testUrl() throws ParsingException {
            assertEquals("https://www.youtube.com/playlist?list=PL45xb3ujEhqUexNt53jb9WT2mS-uUaUrn", extractor.getUrl());
        }

        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals("https://www.youtube.com/watch?v=lH1caqoAGe0&list=PL45xb3ujEhqUexNt53jb9WT2mS-uUaUrn", extractor.getOriginalUrl());
        }

        /*//////////////////////////////////////////////////////////////////////////
        // ListExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor, YouTube.getServiceId());
        }

        @Test
        public void testMoreRelatedItems() throws Exception {
            ListExtractor.InfoItemsPage<StreamInfoItem> currentPage = defaultTestMoreItems(extractor, ServiceList.YouTube.getServiceId());
            // Test for 2 more levels
            for (int i = 0; i < 2; i++) {
                currentPage = extractor.getPage(currentPage.getNextPageUrl());
                defaultTestListOfItems(YouTube.getServiceId(), currentPage.getItems(), currentPage.getErrors());
            }
        }

        /*//////////////////////////////////////////////////////////////////////////
        // PlaylistExtractor
        //////////////////////////////////////////////////////////////////////////*/

        @Test
        public void testThumbnailUrl() throws Exception {
            final String thumbnailUrl = extractor.getThumbnailUrl();
            assertIsSecureUrl(thumbnailUrl);
            assertTrue(thumbnailUrl, thumbnailUrl.contains("yt"));
        }

        @Ignore
        @Test
        public void testBannerUrl() throws Exception {
            final String bannerUrl = extractor.getBannerUrl();
            assertIsSecureUrl(bannerUrl);
            assertTrue(bannerUrl, bannerUrl.contains("yt"));
        }

        @Test
        public void testUploaderUrl() throws Exception {
            assertTrue(extractor.getUploaderUrl().contains("youtube.com"));
        }

        @Test
        public void testUploaderName() throws Exception {
            assertEquals("Luksan Wunder", extractor.getUploaderName());
        }

        @Test
        public void testUploaderAvatarUrl() throws Exception {
            final String uploaderAvatarUrl = extractor.getUploaderAvatarUrl();
            assertTrue(uploaderAvatarUrl, uploaderAvatarUrl.contains("yt"));
        }

        @Test
        public void testStreamCount() throws Exception {
            assertTrue("Error in the streams count", extractor.getStreamCount() > 100);
        }
    }
}
