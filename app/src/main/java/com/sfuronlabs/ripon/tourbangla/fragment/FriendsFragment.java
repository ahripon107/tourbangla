package com.sfuronlabs.ripon.tourbangla.fragment;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sfuronlabs.ripon.tourbangla.activities.LoginActivity;
import com.sfuronlabs.ripon.tourbangla.activities.NewTourToTimelineActivity;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.model.TimelineDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 6/11/15.
 */
public class FriendsFragment extends Fragment{
    ArrayList<TimelineDetails> list1;
    ContactAdapter adapter;
    public static ArrayList<ParseObject> allTours;
    public static ArrayList<ArrayList<ParseObject>> allTourPictures;
    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allTours = new ArrayList<>();
        allTourPictures = new ArrayList<>();

    }


    @Override
    public void onResume() {
        super.onResume();
        int done=0,p;
        if (list1.size()>allTours.size())
        {
            for (p=0;p<allTours.size();p++)
            {
                if (!list1.get(p).placename.equals((String)allTours.get(p).get("tourPlace")))
                {
                    done = 1;
                    list1.remove(p);
                    break;
                }
            }
            if (done==0) list1.remove(p);
            adapter.notifyDataSetChanged();
        }
        int size = allTours.size()-1;
        if (list1.size()<allTours.size())
        {
            list1.add(new TimelineDetails((String) allTours.get(size).get("tourPlace"), (String) allTours.get(size).get("startdate"),
                    (String) allTours.get(size).get("enddate")));
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        list1 = new ArrayList<>();

        adapter = new ContactAdapter(list1,getActivity());

        ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("Tour");
        parseObjectParseQuery.whereEqualTo("userinfo", ParseUser.getCurrentUser());
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
        dialog.setCancelable(true);
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                dialog.dismiss();
                if (e == null) {
                    allTours = (ArrayList) list;
                    for (int i = 0; i < list.size(); i++) {
                        list1.add(new TimelineDetails((String) list.get(i).get("tourPlace"), (String) list.get(i).get("startdate"),
                                (String) list.get(i).get("enddate")));
                        allTourPictures.add(new ArrayList<ParseObject>());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fabAddNewTour);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser()))
                {
                    Toast.makeText(getActivity(),"Please login first",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(),LoginActivity.class);
                    startActivity(i);

                }
                else {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser == null) {
                        Toast.makeText(getActivity(),"Please login first",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);

                    } else {
                        Intent i = new Intent(getActivity(), NewTourToTimelineActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
        recList.setHasFixedSize(true);
        recList.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}





class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<TimelineDetails> contactList;
    Context context;

    public ContactAdapter(List<TimelineDetails> contactList,Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        TimelineDetails ci = contactList.get(i);
        contactViewHolder.startDate.setText("Tour start date: "+ci.startdate);
        contactViewHolder.endDate.setText("Tour end date: "+ci.enddate);
        contactViewHolder.vTitle.setText(ci.placename);
        contactViewHolder.vTitle.setTextColor(Color.BLUE);
        contactViewHolder.startDate.setTextColor(Color.RED);
        contactViewHolder.endDate.setTextColor(Color.GREEN);
        //Toast.makeText(context,"size of "+i+ " is: "+FriendsFragment.allTourPictures.get(i).size(),Toast.LENGTH_LONG).show();



    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview, viewGroup, false);

        return new ContactViewHolder(itemView, new ContactViewHolder.IMyViewHolderClicks() {
            @Override
            public void onText(View caller,int index) {
                Log.d("image","image clicked at "+index);
                Intent i = new Intent("android.intent.action.TOURALLPHOTOS");
                i.putExtra("index",index);
                context.startActivity(i);
            }

            @Override
            public void onImage(ImageView callerImage,int index) {

            }
        });
    }

    /*public static class ContactViewHolder extends RecyclerView.ViewHolder {

        public ContactViewHolder(View ItemView)
        {
            super(ItemView);
        }
    }*/

    static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView startDate;
        protected TextView endDate;
        protected TextView vTitle;
        public IMyViewHolderClicks mListener;

        public ContactViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            startDate =  (TextView) v.findViewById(R.id.txtStartDate);
            mListener = listener;
            endDate = (TextView)  v.findViewById(R.id.txtEndDate);
            vTitle = (TextView) v.findViewById(R.id.title);
            startDate.setOnClickListener(this);
            endDate.setOnClickListener(this);
            vTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView){
                mListener.onImage((ImageView)v,getPosition());
            } else {
                mListener.onText(v,getPosition());
            }
        }

        public static interface IMyViewHolderClicks {
            public void onText(View caller,int index);
            public void onImage(ImageView callerImage,int index);
        }
    }
}