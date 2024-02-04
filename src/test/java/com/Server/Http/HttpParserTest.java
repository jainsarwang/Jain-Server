package com.Server.Http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
    private HttpParser httpParser;

    @BeforeAll
    public void beforeAll() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidTestCase());
        } catch (HttpParsingException e) {
            Assertions.fail(e);
        }

        Assertions.assertEquals(request.getMethod(), HttpMethod.POST);
        Assertions.assertEquals(request.getRequestTarget(), "/");
        Assertions.assertEquals(request.getOriginalHttpVersion(), "HTTP/1.1");
        Assertions.assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);
    }
    private InputStream generateValidTestCase() {
        String rawData = "POST /testing-file/filename.php?this=dasjls&one=two HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 240\r\n" +
                "sec-ch-ua: \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"\r\n" +
                "Content-Type: application/json\r\n" +
                "Cache-Control: no-cache\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Accept: */*\r\n" +
                "Origin: chrome-extension://coohjcphdfgbiolnekdpbcijmhambjff\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: cors\r\n" +
                "Sec-Fetch-Dest: empty\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"dsdad\"\r\n" +
                "\r\n" +
                "dsadasd\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseHttpRequestBadMethodName () {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateBadTestCaseMethodName());
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }
    private InputStream generateBadTestCaseMethodName() {
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseHttpRequestBadMethodName2 () {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateBadTestCaseMethodName2());
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }
    private InputStream generateBadTestCaseMethodName2() {
        String rawData = "GETPOSTHEAD / HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseHttpRequestLineInvalidNumItems () {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(requestLineInvalidNumItems());
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    private InputStream requestLineInvalidNumItems() {
        String rawData = "GET /dsad/  HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseHttpEmptyRequestLine() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(emptyRequestLine());
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    private InputStream emptyRequestLine() {
        String rawData = "\r\n" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseHttpRequestWithoutLR() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateBadRequestLineWithCRNotLR());
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    private InputStream generateBadRequestLineWithCRNotLR() {
        String rawData = "GET / HTTP/1.1\r" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseBadHttpVersionRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateBadHttpVersionRequest());
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    private InputStream generateBadHttpVersionRequest() {
        String rawData = "GET / HTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseUnsupportedHttpVersionRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateUnsupportedHttpVersionRequest());
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }
    private InputStream generateUnsupportedHttpVersionRequest() {
        String rawData = "GET / HTTP/2.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }

    @Test
    void parseSupportedHttpVersionRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateSupportedHttpVersionRequest());
            Assertions.assertNotNull(request);
            Assertions.assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);
            Assertions.assertEquals(request.getOriginalHttpVersion(), "HTTP/1.2");
        } catch (HttpParsingException e) {
            Assertions.fail(e);
        }
    }
    private InputStream generateSupportedHttpVersionRequest() {
        String rawData = "GET / HTTP/1.2\r\n" +
                "Host: localhost\r\n" +
                "\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D\r\n" +
                "Content-Disposition: form-data; name=\"test\"\r\n" +
                "\r\n" +
                "dsjadk\r\n" +
                "------WebKitFormBoundaryq7t13s1lvlNFCe0D--\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return input;
    }
}