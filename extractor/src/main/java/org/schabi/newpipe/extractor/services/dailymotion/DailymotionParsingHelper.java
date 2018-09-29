package org.schabi.newpipe.extractor.services.dailymotion;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.Locale;
import org.schabi.newpipe.extractor.NewPipe;

import java.io.IOException;

public class DailymotionParsingHelper {
    private static DailymotionRequestCredentials requestCredentials = null;

    public class DailymotionRequestCredentials {
        protected Locale.DefinedLocal local;
        protected String clientId = null;
        protected String clientSecret = null;
        protected String trafficSegment = null;
        protected String visitorId = null;

        public DailymotionRequestCredentials(Locale.DefinedLocal local, String clientId, String clientSecret, String trafficSegment, String visitorId) {
            this.local = local;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.trafficSegment = trafficSegment;
            this.visitorId = visitorId;
        }

        public String getClientId() {
            return clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public String getTrafficSegment() {
            return trafficSegment;
        }

        public String getVisitorId() {
            return visitorId;
        }

        public Locale.DefinedLocal getLocal() {
            return local;
        }
    }

    public static DailymotionRequestCredentials getRequestCredentials(Locale.DefinedLocal local) throws IOException {
        if (requestCredentials != null && local.equals(local)) {
            return requestCredentials;
        }

        HttpDownloader dl = NewPipe.getDownloader();
        String mainSite = dl.download("https://www.dailymotion.com/"+local.getShortName());
        Document doc = Jsoup.parse(mainSite);
        for (Element script:doc.getElementsByTag("script")) {
            String scriptContent = script.text();
            if (script.hasAttr("src")) {
                String url = script.attr("src");

                // TODO move feature to OtherUrl
                if (!(url.startsWith("https://") || url.startsWith("http://") || url.startsWith("//"))) {
                    url = "//"+url;
                }
                if (url.startsWith("//")) {
                    url = "https:"+url;
                }

                scriptContent += dl.download(url);
            }
        }
    }
}
