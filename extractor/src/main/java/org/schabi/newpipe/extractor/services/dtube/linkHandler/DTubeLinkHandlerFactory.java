package org.schabi.newpipe.extractor.services.dtube.linkHandler;

import com.github.FlorianSteenbuck.other.feature.Features;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import com.github.FlorianSteenbuck.other.url.helper.UrlParsingHelper;
import com.github.FlorianSteenbuck.other.url.model.UrlParsingFeature;
import com.github.FlorianSteenbuck.other.url.model.UrlQuery;
import com.github.FlorianSteenbuck.other.url.model.list.UrlPseudoQueryList;
import com.github.FlorianSteenbuck.other.url.model.protocol.wellknown.WellKnownProtocolHelper;
import com.github.FlorianSteenbuck.other.url.navigator.UrlNavigator;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandlerFactory;
import org.schabi.newpipe.extractor.services.dtube.DTubeKiosk;
import org.schabi.newpipe.extractor.services.dtube.DTubeParsingHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class DTubeLinkHandlerFactory extends SearchQueryHandlerFactory {
    private static final DTubeLinkHandlerFactory channelInstance = new DTubeLinkHandlerFactory(DTubeLinkFeatures.CHANNEL);
    private static final DTubeLinkHandlerFactory videoInstance = new DTubeLinkHandlerFactory(DTubeLinkFeatures.VIDEO);
    private static final DTubeLinkHandlerFactory kioskInstance = new DTubeLinkHandlerFactory(DTubeLinkFeatures.KIOSK);
    private static final DTubeLinkHandlerFactory searchInstance = new DTubeLinkHandlerFactory(DTubeLinkFeatures.SEARCH);

    private static final String VIDEO = "v";
    private static final String SEARCH = "s";
    private static final String CHANNEL = "c";

    private boolean acceptVideo;
    private boolean acceptChannel;
    private boolean acceptKiosk;
    private boolean acceptSearch;

    protected interface DTubeDataHunter<H, P, E extends Exception> {
        // return null if no  data is given
        H get(P...params) throws E;
    }


    public DTubeLinkHandlerFactory(DTubeLinkFeatures...support) {
        Features<DTubeLinkFeatures> supportedFeatures = new Features<DTubeLinkFeatures>(support);
        this.acceptVideo = supportedFeatures.got(DTubeLinkFeatures.VIDEO);
        this.acceptChannel = supportedFeatures.got(DTubeLinkFeatures.CHANNEL);
        this.acceptKiosk = supportedFeatures.got(DTubeLinkFeatures.KIOSK);
        this.acceptSearch = supportedFeatures.got(DTubeLinkFeatures.SEARCH);
    }

    public static DTubeLinkHandlerFactory getVideoInstance() {
        return videoInstance;
    }

    public static DTubeLinkHandlerFactory getChannelInstance() {
        return channelInstance;
    }

    public static DTubeLinkHandlerFactory getKioskInstance() {
        return kioskInstance;
    }

    public static DTubeLinkHandlerFactory getSearchInstance() {
        return searchInstance;
    }

    private String acceptString() {
        return "accept { "+
                (acceptVideo ? "VIDEO" : "")+
                (acceptChannel ? "CHANNEL" : "")+
                (acceptKiosk ? "KIOSK" : "")+
                (acceptSearch ? "SEARCH" : "")
                + " }";
    }

    @Override
    public String toString() {
        return "DTubeLinkHandlerFactory { "+
                acceptString()
                + " }";
    }

    // TODO generalize for Data Hunting for other services, with a HunterBuilder
    // TODO move to OtherUrl
    protected Object getDataFromUrlParse(String url, String huntedName, DTubeDataHunter<?, UrlPseudoQueryList, ParsingException> hunter) throws ParsingException {
        if (url == null) {
            throw new IllegalArgumentException("url shouldn't be null");
        }
        try {
            UrlNavigator navi = UrlParsingHelper.parse(
                    url,
                    Encodings.UTF_8,
                    UrlParsingFeature.values()
            );

            for (UrlQuery queryPath : navi.getFilepaths()) {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) queryPath;
                Object result = hunter.get(filepath);
                if (result != null) {
                    return result;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
        throw new ParsingException("The dtube url do not contains a "+huntedName+" (" + acceptString() + ")");
    }

    public String getPermId(String originalUrl) throws ParsingException {
        return (String) getDataFromUrlParse(originalUrl, "perm id", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
            @Override
            public String get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];

                if (filepath.size() > 2 && VIDEO.equals(filepath.get(0)) && acceptVideo) {
                    return filepath.get(2);
                }
                return null;
            }
        });
    }

    public String getAuthor(String originalUrl) throws ParsingException {
        return (String) getDataFromUrlParse(originalUrl, "author", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
            @Override
            public String get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                int size = filepath.size();
                if (size > 0) {
                    String indicator = filepath.get(0);
                    if ((size > 1 && CHANNEL.equals(indicator) && acceptChannel) ||
                        (size > 2 && VIDEO.equals(indicator) && acceptVideo)) {
                        return filepath.get(1);
                    }
                }

                return null;
            }
        });
    }

    public Object[] getSteemitParams(String originalUrl) throws ParsingException {
        return (Object[]) getDataFromUrlParse(originalUrl, "ids that can be casted to steemit params", new DTubeDataHunter<Object[], UrlPseudoQueryList, ParsingException>() {
            @Override
            public Object[] get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                int size = filepath.size();
                if (size > 0) {
                    String indicator = filepath.get(0);
                    if (size > 1 && CHANNEL.equals(indicator) && acceptChannel) {
                        return new Object[][]{new String[]{filepath.get(1)}};
                    } else if (size > 2 && VIDEO.equals(indicator) && acceptVideo) {
                        return new String[]{filepath.get(1), filepath.get(2)};
                    }
                }
                return null;
            }
        });
    }

    public String cleanUrl(String complexUrl) throws ParsingException {
        return (String) getDataFromUrlParse(complexUrl, "clean url", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
            @Override
            public String get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                int size = filepath.size();
                if (size > 0) {
                    String indicator = filepath.get(0);
                    if (DTubeKiosk.getKioskById(indicator) != null && acceptKiosk) {
                        return DTubeParsingHelper.DOMAIN_ENDPOINT + "/" + indicator;
                    } else if (size > 1 && CHANNEL.equals(indicator) && acceptChannel) {
                        return DTubeParsingHelper.DOMAIN_ENDPOINT + "/" + CHANNEL + "/" + filepath.get(1);
                    } else if (size > 2 && VIDEO.equals(indicator) && acceptVideo) {
                        String author = filepath.get(1);
                        String video = filepath.get(2);
                        return DTubeParsingHelper.DOMAIN_ENDPOINT + "/" + VIDEO + "/" + author + "/" + video;
                    }
                }
                return null;
            }
        });
    }

    @Override
    public String getId(String url) {
        try {
            return (String) getDataFromUrlParse(url, "complete id", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
                @Override
                public String get(UrlPseudoQueryList... params) throws ParsingException {
                    UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                    int size = filepath.size();
                    if (size > 0) {
                        String indicator = filepath.get(0);
                        if (DTubeKiosk.getKioskById(indicator) != null && acceptKiosk) {
                            return indicator;
                        } else if (size > 1 && CHANNEL.equals(indicator) && acceptChannel) {
                            return "@" + filepath.get(1);
                        } else if (size > 1 && SEARCH.equals(indicator) && acceptSearch) {
                            try {
                                return URLDecoder.decode(filepath.get(1), Encodings.UTF_8);
                            } catch (UnsupportedEncodingException e) {
                                throw new ParsingException(e.getMessage(), e.getCause());
                            }
                        } else if (size > 2 && VIDEO.equals(indicator) && acceptVideo) {
                            String author = filepath.get(1);
                            String video = filepath.get(2);
                            return "@" + author + "/" + video;
                        }
                    }
                    return null;
                }
            });
        } catch (ParsingException ex) {
            // TODO better error handling
            // TODO let the search query handler factory only depend on a new interface that we should also use for the other factories
            return null;
        }
    }

    @Override
    public String getUrl(String id, List<String> contentFilter, String sortFilter) throws ParsingException {
        return getUrl(id);
    }

    @Override
    public String getSearchString(String url) {
        return getId(url);
    }

    @Override
    public String getUrl(String id) throws ParsingException {
        try {
            List<String> partsFromPath = UrlParsingHelper.getPartsFromPath(
                    id,
                    true,
                    Encodings.UTF_8,
                    UrlParsingHelper.FILE_PATH_DELIMITER
            );
            if (partsFromPath.size() > 0 && partsFromPath.size() < 3) {
                String firstPart = partsFromPath.get(0);
                String author = firstPart.startsWith("@") ? firstPart.substring(1) : firstPart;

                if (partsFromPath.size() == 1 && DTubeKiosk.getKioskById(firstPart) != null && acceptKiosk) {
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/" + firstPart;
                } else if (partsFromPath.size() == 1 && acceptSearch) {
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/"+SEARCH+"/" + author;
                } else if (partsFromPath.size() == 1 && acceptChannel) {
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/"+CHANNEL+"/" + author;
                } else if (partsFromPath.size() == 2 && acceptVideo) {
                    String video = partsFromPath.get(1);
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/"+VIDEO+"/" + author + "/" + video;
                }
            } else {
                throw new ParsingException("The dtube id need to contains one or two arguments ("+acceptString()+")");
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
        throw new ParsingException("The id do not match the requirements of an id ("+acceptString()+")");
    }

    @Override
    public boolean onAcceptUrl(String url) {
        try {
            UrlNavigator navi = UrlParsingHelper.parse(
                    url,
                    Encodings.UTF_8,
                    UrlParsingFeature.values()
            );
            boolean noWrongProtocol = navi.gotProtocol(WellKnownProtocolHelper.HTTP.getProtocol()) || navi.gotProtocol(WellKnownProtocolHelper.HTTPS.getProtocol());
            if (noWrongProtocol && navi.gotDomain("d.tube")) {
                for (UrlQuery queryPath:navi.getFilepaths()) {
                    UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) queryPath;
                    int size = filepath.size();
                    if (size > 0) {
                        String indicator = filepath.get(0);
                        if ((DTubeKiosk.getKioskById(indicator) != null && acceptKiosk) ||
                               (size > 1
                                &&
                                (
                                 (indicator.equals(VIDEO) && acceptVideo) ||
                                 (indicator.equals(SEARCH) && acceptSearch) ||
                                 (indicator.equals(CHANNEL) && acceptChannel)
                                )
                               )
                           ) {
                            return true;
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            // TODO better error handling
            // TODO let the search query handler factory only depend on a new interface that we should also use for the other factories
            return false;
        }
        return false;
    }
}
