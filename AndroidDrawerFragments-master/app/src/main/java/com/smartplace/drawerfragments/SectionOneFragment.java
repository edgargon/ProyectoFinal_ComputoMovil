package com.smartplace.drawerfragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplace.drawerfragments.Data.CleanMyHouseArray;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionOneFragment extends Fragment {


    public static CleanMyHouseArray mNamesArray;
    public ListAdapter mNameListAdapter;
    public ListView listView;
    public Button btnLoad;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //These are the arguments passed through the constructors
    private static final String ARG_SECTION_NUMBER = "section_number";

    //Variable to save the argument
    private int mSectionNumber;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter 1.
     * @return A new instance of fragment SectionOneFragment.
     */
    public static SectionOneFragment newInstance(int sectionNumber) {
        SectionOneFragment fragment = new SectionOneFragment();
        Bundle args = new Bundle();
        //set arguments into bundle
        //This is done to avoid null pointer exceptions
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SectionOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get arguments from bundle
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_section_one, container, false);


        btnLoad = (Button) fragmentView.findViewById(R.id.btn_load);
        listView = (ListView) fragmentView.findViewById(R.id.list_view);



        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServices.getNames(mNamesHandler);
                btnLoad.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        });


        //fragment view shall be returned
        return fragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //This method is called when the fragment is attached into the activity

        //In order to set the title, call function from holder activity
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private Handler mNamesHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            //get response
            String response = bundle != null ? bundle.getString("response") : "";

            //Check response
            if ((response.equals("")) || response.equals("no connection")) {
                //No internet access
                //Toast.makeText(getActivity().getBaseContext(),getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }

            else {

                try {
                    //get json response into object
                    //mNamesArray = new Gson().fromJson(response, NamesArray.class);
                    mNamesArray = new Gson().fromJson(response, CleanMyHouseArray.class);
                    mNameListAdapter = new ListAdapter(getActivity().getBaseContext(),mNamesArray.getNamesActores());
                    //mNameListAdapter = new NameListAdapter(getBaseContext(),mNamesArray.getNames());

                    listView.setAdapter(mNameListAdapter);
                    btnLoad.setBackgroundColor(getResources().getColor(android.R.color.black));

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    //invalid json response from the server
                    //Toast.makeText(getActivity().getBaseContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        }

    };
}
