package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;
import org.schabi.newpipe.extractor.stream.StreamType;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.schabi.newpipe.extractor.ExtractorAsserts.assertIsSecureUrl;
import static org.schabi.newpipe.extractor.ServiceList.DTube;

/**
 * Test for {@link StreamExtractor}
 */
public class DTubeStreamExtractorDefaultTest {
    private static DTubeStreamExtractor extractor;

    @BeforeClass
    public static void setUp() throws Exception {
        NewPipe.init(Downloader.getInstance());
        extractor = (DTubeStreamExtractor) DTube.getStreamExtractor("https://d.tube/#!/v/sempervideo/mq3ykiml");
        extractor.fetchPage();
    }

    @Test
    public void testGetTitle() throws ParsingException {
        assertEquals(extractor.getName().trim(), "100 Tage Steem - Ein Rückblick (Teil 1)");
    }

    @Test
    public void testGetDescription() throws ParsingException {
        assertEquals(extractor.getDescription().trim(), "Vote for Witness steemconnect.com/sign/account_witness_vote?account=&approve=1&witness=sempervideo\n" +
                "\n" +
                "https://steemit.com/witness/@sempervideo/der-naechste-logische-schritt\n" +
                "https://steemit.com/witness/@semperenglish/the-next-logical-step\n" +
                "\n" +
                "Vielen Dank für Ihre Unterstützung.\n" +
                "Thank you for your support.\n" +
                "\n" +
                "https://paypal.me/SemperVideo\n" +
                "\n" +
                "http://www.patreon.com/sempervideo?ty=c\n" +
                "\n" +
                "http://amzn.to/28YoGFh");
    }

    @Test
    public void testGetUploaderName() throws ParsingException {
        assertEquals(extractor.getUploaderName(), "sempervideo");
    }

    @Test
    public void testGetLength() throws ParsingException {
        assertEquals(extractor.getLength(), 852);
    }

    @Ignore
    @Test
    public void testGetViewCount() throws ParsingException {
        assertTrue(Long.toString(extractor.getViewCount()),
                extractor.getViewCount() > 300);
    }

    @Test
    public void testGetUploadDate() throws ParsingException {
        assertEquals("2018-04-09", extractor.getUploadDate());
    }

    @Test
    public void testGetUploaderUrl() throws ParsingException {
        assertIsSecureUrl(extractor.getUploaderUrl());
        assertEquals("https://d.tube/c/sempervideo", extractor.getUploaderUrl());
    }

    @Test
    public void testGetThumbnailUrl() throws ParsingException {
        assertIsSecureUrl(extractor.getThumbnailUrl());
    }

    @Test
    public void testGetUploaderAvatarUrl() throws ParsingException {
        assertIsSecureUrl(extractor.getUploaderAvatarUrl());
    }

    @Test
    public void testGetAudioStreams() throws IOException, ExtractionException {
        assertTrue(extractor.getAudioStreams().isEmpty());
    }

    @Test
    public void testStreamType() throws ParsingException {
        assertEquals(extractor.getStreamType(), StreamType.VIDEO_STREAM);
    }

    @Test
    public void testGetRelatedVideos() throws ExtractionException, IOException {
        StreamInfoItemsCollector relatedVideos = extractor.getRelatedVideos();
        assertFalse(relatedVideos.getItems().isEmpty());
        assertTrue(relatedVideos.getErrors().isEmpty());
    }

    @Test
    public void testGetSubtitlesListDefault() throws IOException, ExtractionException {
        assertTrue(extractor.getSubtitlesDefault().isEmpty());
    }

    @Test
    public void testGetSubtitlesList() throws IOException, ExtractionException {
        assertTrue(extractor.getSubtitlesDefault().isEmpty());
    }
}
