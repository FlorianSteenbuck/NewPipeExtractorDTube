/*
 *MIT License
 *
 *Copyright (c) 2018 Florian Steenbuck
 *
 *Permission is hereby granted, free of charge, to any person obtaining a copy
 *of this software and associated documentation files (the "Software"), to deal
 *in the Software without restriction, including without limitation the rights
 *to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *copies of the Software, and to permit persons to whom the Software is
 *furnished to do so, subject to the following conditions:
 *
 *The above copyright notice and this permission notice shall be included in all
 *copies or substantial portions of the Software.
 *
 *THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *SOFTWARE.
 */
package org.schabi.newpipe.extractor.constants;

public class Words {
    public static final String DATA = "data";
    public static final String VOTE = "vote";
    public static final String LINK = "link";
    public static final String PERM = "perm";
    public static final String JSON = "json";
    public static final String RPC = "rpc";
    public static final String HASH = "hash";
    public static final String RESULT = "result";
    public static final String VIDEO = "video";
    public static final String DURATION = "duration";
    public static final String ACTIVE = "active";
    public static final String SNAP = "snap";
    public static final String INFO = "info";
    public static final String CONTENT = "content";
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String METHOD = "method";
    public static final String PARAM = "param";
    public static final String ANONYMOUS = "anonymous";
    public static final String META = "meta";
    public static final String PROFILE = "profile";

    public static final String PARAMS = PARAM+"s";
    public static final String RESULTS = RESULT+"s";
    public static final String VOTES = VOTE+"s";

    public static final String JSONRPC = JSON+RPC;
    public static final String PERMLINK = LINK+PERM;
    public static final String SNAPHASH = SNAP+HASH;
    public static final String METADATA = META+DATA;

    public static final String[] all = {
            // singular
            DATA,VOTE, LINK, PERM, JSON, RPC, HASH, RESULT, VIDEO,
            DURATION, ACTIVE, SNAP, INFO, CONTENT, TITLE,
            ID, METHOD, PARAM, ANONYMOUS, META, PROFILE,
            // plural
            PARAMS, RESULTS, VOTES,
            // connected
            JSONRPC, PERMLINK, SNAPHASH, METADATA
    };
}
