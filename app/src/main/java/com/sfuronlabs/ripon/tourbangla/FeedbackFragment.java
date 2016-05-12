package com.sfuronlabs.ripon.tourbangla;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sfuronlabs.ripon.tourbangla.view.cpb.CircularProgressButton;

/**
 * Created by Ripon on 9/27/15.
 */
public class FeedbackFragment extends Fragment {

    public FeedbackFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentfeedback, container, false);
        final EditText title = (EditText) rootView.findViewById(R.id.etFeedbackTitle);
        final EditText details = (EditText) rootView.findViewById(R.id.etFeedbackDetails);
        final CircularProgressButton send = (CircularProgressButton) rootView.findViewById(R.id.btnSend);
        send.setText("SEND");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        String t = title.getText().toString().trim();
                        String d = details.getText().toString().trim();
                        if (t.length()==0 || d.length()==0)
                        {
                            Toast.makeText(getActivity(),"Please give input correctly",Toast.LENGTH_LONG).show();
                            new FalseProgress(send).execute(-1);
                            return;
                        }
                        Toast.makeText(getActivity(),"Thank you for your feedback",Toast.LENGTH_LONG).show();
                        new FalseProgress(send).execute(100);
                        title.getText().clear();
                        details.getText().clear();
                        ParseObject parseObject = new ParseObject("Feedback");
                        //parseObject.put("user", ParseUser.getCurrentUser());
                        parseObject.put("title",t);
                        parseObject.put("details",d);
                        parseObject.saveInBackground();
                    }



        });
        return rootView;
    }

    private class FalseProgress extends AsyncTask<Integer, Integer, Integer> {

        private CircularProgressButton cpb;

        public FalseProgress(CircularProgressButton cpb) {
            this.cpb = cpb;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            for (int progress = 0; progress < 100; progress += 5) {
                publishProgress(progress);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            cpb.setProgress(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            cpb.setProgress(progress);
        }
    }
}
