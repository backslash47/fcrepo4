/**
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.integration;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.fcrepo.http.commons.test.util.SpringContextSingleton;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jcr.Repository;
import java.io.IOException;
import java.util.UUID;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.junit.Assert.assertEquals;

/**
 * <p>Abstract AbstractResourceIT class.</p>
 *
 * @author cbeer
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractResourceIT {

    protected Repository repo;

    protected Logger logger;

    @Before
    public void setRepo() {
        repo =
                SpringContextSingleton.getApplicationContext().getBean(
                        Repository.class);
    }

    @Before
    public void setLogger() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected static final int SERVER_PORT = parseInt(getProperty("fcrepo.dynamic.test.port",
                                                                         "8080"));

    protected static final String HOSTNAME = "localhost";

    protected static final String serverAddress = "http://" + HOSTNAME + ":"
            + SERVER_PORT;

    protected static HttpClient client;

    public AbstractResourceIT() {
        final HttpClientBuilder b =
            HttpClientBuilder.create().setMaxConnPerRoute(MAX_VALUE)
                    .setMaxConnTotal(MAX_VALUE);
        client = b.build();
    }

    protected int getStatus(final HttpUriRequest method)
            throws ClientProtocolException, IOException {
        logger.debug("Executing: " + method.getMethod() + " to " +
                             method.getURI());
        return client.execute(method).getStatusLine().getStatusCode();
    }

    /**
     * Gets a random (but valid) pid for use in testing.  This pid
     * is guaranteed to be unique within runs of this application.
     *
     * @return string containing new random unique Pid
     */
    protected static String getRandomUniquePid() {
        return UUID.randomUUID().toString();
    }


    protected static HttpPost postObjMethod(final String pid) {
        return new HttpPost(serverAddress + pid);
    }

    protected HttpResponse createObject(final String pid) throws IOException {
        final HttpPost httpPost = postObjMethod("/");
        if (pid.length() > 0) {
            httpPost.addHeader("Slug", pid);
        }
        final HttpResponse response = client.execute(httpPost);
        assertEquals(CREATED.getStatusCode(), response.getStatusLine().getStatusCode());
        return response;
    }
}
