package abk.com.gap;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by user on 4/06/2016.
 */
public class FragmentInput extends Fragment {

    public interface FragmentInputCommunicator {
        void sendURLText(String url);
    }

    public static final String FRAGMENT_TAG = "FragmentInput";

    private EditText inputUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_input, container, false);
        inputUrl = (EditText)rootView.findViewById(R.id.input_url);
        final Button submitButton = (Button)rootView.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String http = "http://";
                String inputText = inputUrl.getText().toString().trim();
                if (!"".equals(inputText) && (!inputText.startsWith(http) && !inputText.startsWith("https://"))) {
                    inputText = http + inputText;
                }
                if (URLUtil.isValidUrl(inputText)) {
                    ((FragmentInputCommunicator)getActivity()).sendURLText(inputText);
                } else {
                    new AlertDialog.Builder(FragmentInput.this.getActivity())
                            .setTitle("Validation Error")
                            .setMessage("Please, provide valid URL address")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

        return rootView;
    }
}
