package util;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

public class HttpsURLConnectionWrapper implements AutoCloseable {

    private final HttpsURLConnection connection;

    public HttpsURLConnectionWrapper(URL url) throws IOException {
        connection = (HttpsURLConnection) url.openConnection();
    }

    public HttpsURLConnection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        connection.disconnect();
    }
}