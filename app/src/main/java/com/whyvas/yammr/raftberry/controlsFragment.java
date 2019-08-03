package com.whyvas.yammr.raftberry;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class controlsFragment extends Fragment {
    public controlsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
              getActivity().setTitle(getClass().getSimpleName());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.controls, container, false);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof mapFragment.OnFragmentInteractionListener) {
          //  mListener = (mapFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
     }

    @Override
    public void onResume() {
        super.onResume();
      }

    @Override
    public void onPause() {
        super.onPause();
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
