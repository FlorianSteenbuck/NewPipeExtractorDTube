package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.dtube.linkHandler.DTubeLinkHandlerFactory;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link DTubeLinkHandlerFactory} (Kiosk Instance)
 */
public class DTubeKioskUrlIdHandlerTest {
    private static DTubeLinkHandlerFactory urlIdHandler;

    @BeforeClass
    public static void setUp() throws Exception {
        urlIdHandler = DTubeLinkHandlerFactory.getKioskInstance();
        NewPipe.init(Downloader.getInstance());
    }

    @Test
    public void getUrl() throws ParsingException {
        assertEquals(urlIdHandler.setId("trendingvideos").getUrl(), "https://d.tube/trendingvideos");
        assertEquals(urlIdHandler.setId("hotvideos").getUrl(), "https://d.tube/hotvideos");
        assertEquals(urlIdHandler.setId("newvideos").getUrl(), "https://d.tube/newvideos");
    }

    @Test
    public void getId() throws ParsingException {
        assertEquals(urlIdHandler.setUrl("https://d.tube/#!/trendingvideos").getId(), "trendingvideos");
        assertEquals(urlIdHandler.setUrl("https://d.tube/#!/hotvideos").getId(), "hotvideos");
        assertEquals(urlIdHandler.setUrl("https://d.tube/#!/newvideos").getId(), "newvideos");

        assertEquals(urlIdHandler.setUrl("https://d.tube/trendingvideos").getId(), "trendingvideos");
        assertEquals(urlIdHandler.setUrl("https://d.tube/hotvideos").getId(), "hotvideos");
        assertEquals(urlIdHandler.setUrl("https://d.tube/newvideos").getId(), "newvideos");
    }

    @Test
    public void acceptUrl() {
        assertTrue(urlIdHandler.acceptUrl("https:d.tube/#!/trendingvideos"));
        assertTrue(urlIdHandler.acceptUrl("https://d.tube/#!/hotvideos"));
        assertTrue(urlIdHandler.acceptUrl("HTTP://d.tube/#!/newvideos"));
        assertTrue(urlIdHandler.acceptUrl("HTTPS://d.tube/trendingvideos"));
        assertTrue(urlIdHandler.acceptUrl("hTTp://d.tube/#!/newvideos"));

        assertFalse(urlIdHandler.acceptUrl("udp://d.tube/#!/trendingvideos"));
        assertFalse(urlIdHandler.acceptUrl("d.tube tls is not safe enough"));
        assertFalse(urlIdHandler.acceptUrl("ht://d.tube"));
        assertFalse(urlIdHandler.acceptUrl("magnet:d.tu be"));
    }
}
