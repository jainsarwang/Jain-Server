package com.Server.Http;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class HttpVersionTestClass {
    @Test
    public void getBestCompatibleVersionExactMatch() {
        try {
            HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");

            Assertions.assertNotNull(version);
            Assertions.assertEquals(version, HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void getBestCompatibleVersionBadMatch() {
        try {
            HttpVersion version = HttpVersion.getBestCompatibleVersion("http/1.1");

            Assertions.fail();
        } catch (BadHttpVersionException e) {
//            Assertions.assertEquals(e, HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    @Test
    public void getBestCompatibleVersionHigherMatch() {
        try {
            HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");

            Assertions.assertNotNull(version);
            Assertions.assertEquals(version, HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            Assertions.fail(e);
        }
    }
}
