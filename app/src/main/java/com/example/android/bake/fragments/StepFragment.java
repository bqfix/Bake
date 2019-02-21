package com.example.android.bake.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.bake.R;
import com.example.android.bake.recipes.StepInstruction;

public class StepFragment extends Fragment {
    private StepInstruction mStepInstruction;

    public StepFragment() {
        // Required empty public constructor
    }

    //Factory method to create a new instance of this fragment
    public static StepFragment newInstance(Context context, StepInstruction stepInstruction) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.step_parcelable_key), stepInstruction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieve StepInstruction
        if (getArguments() != null) {
            mStepInstruction = getArguments().getParcelable(getString(R.string.step_parcelable_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        //Get views
        FrameLayout exoPlayerContainer = (FrameLayout) rootView.findViewById(R.id.step_exoplayer_fragment_container);
        ScrollView stepDescripScrollView = (ScrollView) rootView.findViewById(R.id.step_descrip_sv);

        //Check screen orientation
        boolean isLandscape = container.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) { //Hide TextView
            stepDescripScrollView.setVisibility(View.GONE);
        } else { //Setup Description Text
            stepDescripScrollView.setVisibility(View.VISIBLE);
            TextView stepDescripText = (TextView) rootView.findViewById(R.id.media_step_descrip);
            stepDescripText.setText(mStepInstruction.getmFullDescription());
        }

        //Setup Exoplayer Fragment
        ExoPlayerFragment exoPlayerFragment = ExoPlayerFragment.newInstance(rootView.getContext(), mStepInstruction.getmVideoURL());
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.step_exoplayer_fragment_container, exoPlayerFragment).commit();

        return rootView;
    }

}
