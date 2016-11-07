package abk.com.gap;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by user on 4/06/2016.
 */
public class HttpClient {
    private static final int CONNECTION_TIMEOUT = 1000 * 30;
    private static final int SOCKET_TIMEOUT = 1000 * 60 * 10;
    private static final int MIN_ZIP_SIZE = 3 * 1024;

    private static HttpClient instance;

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = new HttpClient();
        }

        return instance;
    }

    private HttpClient() {}

    public void doStreamingPost(String postUrl, String urlString, HttpResponseHandler handler) throws IOException {
        URL url = new URL(postUrl);

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(SOCKET_TIMEOUT);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("url", urlString);

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ObjectMapper mapper = new ObjectMapper();
                List listTest = mapper.readValue(in, List.class);
                handler.onResult(listTest);
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}