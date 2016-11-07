package abk.com.gap;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by user on 4/06/2016.
 */
public class DownloadTestService extends IntentService {

    public static final String URL_KEY = "DownloadTestService.url";

    public static final String SEND_URL = "http://192.168.220.90:8080/HackHack/webapi/questions";

    public static final String SERVICE_RESPOND =
            "abk.com.gap.APPINFO_UPDATE_SERVICE_RESPOND";

    private Intent respIntent;
    private Context context;
    private LocalBroadcastManager localBroadcastManager;

    public DownloadTestService() {
        super("DownloadTestService");
    }

    public DownloadTestService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        respIntent = new Intent(SERVICE_RESPOND);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url;
        url = intent.getStringExtra(URL_KEY);

        Request request = new Request();
        request.setUrl(url);

        try {
            HttpClient.getInstance().doStreamingPost(SEND_URL, url, new DownLoadHandler());
        } catch(Exception ex) {}

        localBroadcastManager.sendBroadcast(respIntent);
    }

    public class DownLoadHandler implements HttpResponseHandler {
        public void onResult(List tests) {
            final int size = tests.size();
            // TODO use CouchDB
            MainActivity.currTest = new ArrayList<>(size);
            Test test;
            LinkedHashMap t;
            final String answer = "answer";
            final String text = "text";
            for (int i = 0; i < size; i++) {
                t = (LinkedHashMap) tests.get(i);

                test = new Test();
                test.answer = (String) t.get(answer);
                test.text = (String) t.get(text);
                MainActivity.currTest.add(test);
            }
        }
    }
}
