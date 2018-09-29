package org.schabi.newpipe;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import com.github.FlorianSteenbuck.other.http.resp.headers.HttpHeadExecutionTyp;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * Created by Christian Schabesberger on 28.01.16.
 *
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * HttpDownloader.java is part of NewPipe.
 *
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Downloader implements HttpDownloader {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";
    private static String mCookies = "";

    private static Downloader instance = null;

    private Downloader() {
    }

    public static HttpDownloader getInstance() {
        if (instance == null) {
            synchronized (Downloader.class) {
                if (instance == null) {
                    instance = new Downloader();
                }
            }
        }
        return instance;
    }

    public static synchronized void setCookies(String cookies) {
        Downloader.mCookies = cookies;
    }

    public static synchronized String getCookies() {
        return Downloader.mCookies;
    }

    @Override
    public String download(String siteUrl, String language, byte[] body) throws IOException {
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Accept-Language", language);
        return download(siteUrl, requestProperties, body);
    }

    /**
     * Download the text file at the supplied URL as in download(String),
     * but set the HTTP header field "Accept-Language" to the supplied string.
     *
     * @param siteUrl  the URL of the text file to return the contents of
     * @param language the language (usually a 2-character code) to set as the preferred language
     * @return the contents of the specified text file
     */
    public String download(String siteUrl, String language) throws IOException {
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Accept-Language", language);
        return download(siteUrl, requestProperties);
    }


    /**
     * Download the text file at the supplied URL as in download(String),
     * but set the HTTP header field "Accept-Language" to the supplied string.
     *
     * @param siteUrl          the URL of the text file to return the contents of
     * @param customProperties set request header properties
     * @return the contents of the specified text file
     * @throws IOException
     */
    public String download(String siteUrl, Map<String, String> customProperties) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        for (Map.Entry<String, String> pair: customProperties.entrySet()) {
            con.setRequestProperty(pair.getKey(), pair.getValue());
        }
        return dl(con, true);
    }

    /**
     * Download the text file at the supplied URL as in download(String),
     * but custom body and headers.
     *
     * @param siteUrl          the URL of the text file to return the contents of
     * @param customProperties set request header properties
     * @param body set request body
     * @return the contents of the specified text file
     * @throws IOException
     */
    public String download(String siteUrl, Map<String, String> customProperties, byte[] body) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        setupConnection(con, "POST");
        for (Map.Entry<String, String> pair: customProperties.entrySet()) {
            con.setRequestProperty(pair.getKey(), pair.getValue());
        }
        con.setDoOutput(true);
        OutputStream outputStream = con.getOutputStream();
        outputStream.write(body);
        outputStream.close();
        return dl(con, false);
    }

    /**
     * Common functionality between download(String url) and download(String url, String language)
     */
    private static Object[] downloadConAndString(HttpsURLConnection con, boolean setup) throws IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader in = null;

        try {
            if (setup) {
                con = setupConnection(con, "GET");
            }

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (UnknownHostException uhe) {//thrown when there's no internet connection
            throw new IOException("unknown host or no network", uhe);
            //Toast.makeText(getActivity(), uhe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            /*
             * HTTP 429 == Too Many Request
             * Receive from Youtube.com = ReCaptcha challenge request
             * See : https://github.com/rg3/youtube-dl/issues/5138
             */
            if (con.getResponseCode() == 429) {
                // TODO pass a recaptcha handle trough the downloader to show it to the user
                // TODO let the user decide if they want to show it or break it with rucaptcha
                throw new IOException("reCaptcha Challenge requested");
            }

            throw new IOException(con.getResponseCode() + " " + con.getResponseMessage(), e);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return new Object[]{con, response.toString()};
    }

    private static HttpsURLConnection setupConnection(HttpsURLConnection con, String method) throws ProtocolException {
        con.setConnectTimeout(30 * 1000);// 30s
        con.setReadTimeout(30 * 1000);// 30s
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", USER_AGENT);

        if (getCookies().length() > 0) {
            con.setRequestProperty("Cookie", getCookies());
        }

        return con;
    }

    /**
     * Common functionality between download(String url) and download(String url, String language)
     */
    private static String dl(HttpsURLConnection con, boolean setup) throws IOException {
        return (String) downloadConAndString(con, setup)[1];
    }

    private Map<String, List<String>> headRange(HttpsURLConnection con) throws IOException {
        BufferedReader in = null;
        try {
            con.setRequestProperty("Range","bytes=0-0");
            con = setupConnection(con, "GET");

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            while (in.readLine() != null) { }
        } catch (UnknownHostException uhe) {
            //thrown when there's no internet connection
            throw new IOException("unknown host or no network", uhe);
        } catch (Exception e) {
            throw new IOException(con.getResponseCode() + " " + con.getResponseMessage(), e);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return con.getHeaderFields();
    }

    private Map<String, List<String>> headMethodic(HttpsURLConnection con) throws IOException {
        BufferedReader in = null;
        try {
            con = setupConnection(con, "HEAD");

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            while (in.readLine() != null) { }
        } catch (UnknownHostException uhe) {
            //thrown when there's no internet connection
            throw new IOException("unknown host or no network", uhe);
        } catch (Exception e) {
            throw new IOException(con.getResponseCode() + " " + con.getResponseMessage(), e);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return con.getHeaderFields();
    }

    private Map<String,List<String>> head(HttpsURLConnection con, HttpHeadExecutionTyp...typs) throws IOException {
        IOException lastIOException = null;

        typs = typs == null || typs.length <= 0 ? HttpHeadExecutionTyp.defaults() : typs;
        for (HttpHeadExecutionTyp typ:typs) {
            Map<String,List<String>> headers = null;
            try {
                switch (typ) {
                    case RANGE:
                        return headRange(con);
                    case METHOD:
                        return headMethodic(con);
                    case FULL_BODY:
                        con = (HttpsURLConnection) downloadConAndString(con, true)[0];
                        return con.getHeaderFields();
                }
            } catch (IOException e) {
                lastIOException = e;
            }
        }

        if (lastIOException != null) {
            throw lastIOException;
        }

        return new HashMap<String, List<String>>();
    }

    /**
     * Download (via HTTP) the text file located at the supplied URL, and return its contents.
     * Primarily intended for downloading web pages.
     *
     * @param siteUrl the URL of the text file to download
     * @return the contents of the specified text file
     */
    public String download(String siteUrl) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        //HttpsURLConnection con = NetCipher.getHttpsURLConnection(url);
        return dl(con, true);
    }

    @Override
    public String download(String siteUrl, byte[] body) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        setupConnection(con, "POST");
        con.setDoOutput(true);
        OutputStream outputStream = con.getOutputStream();
        outputStream.write(body);
        outputStream.close();
        return dl(con, false);
    }

    @Override
    public Map<String, List<String>> downloadHead(String siteUrl, String language, byte[] body, HttpHeadExecutionTyp...typs) throws IOException {
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Accept-Language", language);
        return downloadHead(siteUrl, requestProperties, body, typs);
    }

    @Override
    public Map<String, List<String>> downloadHead(String siteUrl, String language, HttpHeadExecutionTyp...typs) throws IOException {
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Accept-Language", language);
        return downloadHead(siteUrl, requestProperties, typs);
    }

    @Override
    public Map<String, List<String>> downloadHead(String siteUrl, Map<String, String> customProperties, HttpHeadExecutionTyp...typs) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        for (Map.Entry<String, String> pair: customProperties.entrySet()) {
            con.setRequestProperty(pair.getKey(), pair.getValue());
        }
        return head(con, typs);
    }

    @Override
    public Map<String, List<String>> downloadHead(String siteUrl, Map<String, String> customProperties, byte[] body, HttpHeadExecutionTyp...typs) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        for (Map.Entry<String, String> pair: customProperties.entrySet()) {
            con.setRequestProperty(pair.getKey(), pair.getValue());
        }
        OutputStream outputStream = con.getOutputStream();
        outputStream.write(body);
        outputStream.close();
        return head(con, typs);
    }

    @Override
    public Map<String, List<String>> downloadHead(String siteUrl, HttpHeadExecutionTyp...typs) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        //HttpsURLConnection con = NetCipher.getHttpsURLConnection(url);
        return head(con, typs);
    }

    @Override
    public Map<String, List<String>> downloadHead(String siteUrl, byte[] body, HttpHeadExecutionTyp...typs) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        OutputStream outputStream = con.getOutputStream();
        outputStream.write(body);
        outputStream.close();
        return head(con, typs);
    }
}