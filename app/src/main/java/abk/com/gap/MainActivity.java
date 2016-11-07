package abk.com.gap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements FragmentInput.FragmentInputCommunicator, FragmentExecuteTest.FragmentExecuteCommunicator {

    private BroadcastReceiver broadcastReceiver;

    public static volatile List<Test> currTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FragmentExecuteTest fragmentExecService =
                        (FragmentExecuteTest)getSupportFragmentManager().findFragmentByTag(FragmentExecuteTest.FRAGMENT_TAG);
                if (fragmentExecService != null) {
                    fragmentExecService.startTest();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(DownloadTestService.SERVICE_RESPOND));

        if (null == savedInstanceState) {
            addFragment(FragmentInput.FRAGMENT_TAG);
        }
    }

    @Override
    public void onDestroy() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getNumberQuestions() {
        if (currTest != null) {
            return currTest.size();
        }

        return 0;
    }

    @Override
    public void sendURLText(String url) {
        final Intent intent = new Intent(this, DownloadTestService.class);
        intent.putExtra(DownloadTestService.URL_KEY, url);
        startService(intent);
        replaceFragment(FragmentExecuteTest.FRAGMENT_TAG);
    }

    @Override
    public void startAgain() {
        replaceFragment(FragmentInput.FRAGMENT_TAG);
    }

    private void addFragment(final String fragmentTag) {
        final FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.add(R.id.fragment_container, fragmentCreator(fragmentTag), FragmentInput.FRAGMENT_TAG);
        //localFragmentTransaction.addToBackStack(null);
        localFragmentTransaction.commit();
    }

    private void replaceFragment(final String fragmentTag) {
        final FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.fragment_container, fragmentCreator(fragmentTag), fragmentTag);
        localFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //localFragmentTransaction.addToBackStack(null);
        localFragmentTransaction.commit();
    }

    private Fragment fragmentCreator(final String fragmentTag) {
        switch(fragmentTag) {
            case FragmentExecuteTest.FRAGMENT_TAG:
                return new FragmentExecuteTest();
            case FragmentInput.FRAGMENT_TAG:
            default:
            return new FragmentInput();
        }
    }
}
