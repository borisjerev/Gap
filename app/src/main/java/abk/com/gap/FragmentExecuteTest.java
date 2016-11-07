package abk.com.gap;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by user on 4/06/2016.
 */
public class FragmentExecuteTest extends Fragment {
    public static final String FRAGMENT_TAG = "FragmentExecuteTest";

    public interface FragmentExecuteCommunicator {
        int getNumberQuestions();
        void startAgain();
    }

    private int correstNum = 0;
    private int errorNum = 0;

    private View testLayout;

    private View results_layout;
    private TextView correctView;
    private TextView errorView;
    private Button startAgain;

    private ProgressBar progressBar;
    private ProgressBar loadingView;
    private Button button;

    private TextView questionView;
    private EditText questionAnswerView;

    private int currQuestion = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        testLayout = rootView.findViewById(R.id.test_layout);

        results_layout = rootView.findViewById(R.id.results_layout);
        correctView = (TextView)rootView.findViewById(R.id.number_corrects);
        errorView = (TextView)rootView.findViewById(R.id.number_errors);
        startAgain = (Button)rootView.findViewById(R.id.start_again);
        startAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startAgain();
            }
        });

        progressBar = (ProgressBar)rootView.findViewById(R.id.progress);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_drawable));
        loadingView = (ProgressBar)rootView.findViewById(R.id.loading);
        button = (Button)rootView.findViewById(R.id.next);
        questionView = (TextView)rootView.findViewById(R.id.question);
        questionAnswerView = (EditText)rootView.findViewById(R.id.question_answer);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener();
            }
        });

        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }

    public void startTest() {
        loadingView.setVisibility(View.GONE);
        testLayout.setVisibility(View.VISIBLE);
        currQuestion = 0;
        correstNum = 0;
        errorNum = 0;
        progressBar.setMax(getNumberQuestions());
        if (MainActivity.currTest != null && getNumberQuestions() > 0) {
            questionView.setText(MainActivity.currTest.get(0).text);
            button.setText(getResources().getString(R.string.submit));
        }
    }

    private int getNumberQuestions() {
        return ((FragmentExecuteCommunicator)getActivity()).getNumberQuestions();
    }

    private void buttonClickListener() {
        final Resources res = getResources();
        final String submitText = res.getString(R.string.submit);
        final String nextText = res.getString(R.string.next);

        if (button.getText().toString().equals(submitText)) {
            boolean correctAnswer = false;
            final String correctAnswerTest = MainActivity.currTest.get(currQuestion).answer;
            if (questionAnswerView.getText().toString().trim().equalsIgnoreCase(correctAnswerTest)) {
                correctAnswer = true;
                correstNum++;
            } else {
                errorNum++;
            }

            progressBar.setProgress(currQuestion + 1);
            button.setText(nextText);

            String showMess;
            if (correctAnswer) {
                showMess = "Correct!";
            } else {
                showMess = "Incorrect!";
            }

            new AlertDialog.Builder(getActivity())
                    .setTitle(showMess)
                    .setMessage("Correct Answer: " + correctAnswerTest)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();


        } else if (button.getText().toString().equals(nextText)) {
            if (currQuestion < getNumberQuestions() - 1) {
                currQuestion++;
                button.setText(submitText);
                questionView.setText(MainActivity.currTest.get(currQuestion).text);
                questionAnswerView.setText(null);
            } else {
                testLayout.setVisibility(View.GONE);
                results_layout.setVisibility(View.VISIBLE);
                correctView.setText(correstNum + "");
                errorView.setText(errorNum+"");
            }

        }
    }



}
