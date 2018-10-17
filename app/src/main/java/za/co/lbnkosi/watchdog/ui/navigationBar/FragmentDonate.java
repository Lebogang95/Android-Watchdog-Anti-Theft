package za.co.lbnkosi.watchdog.ui.navigationBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.lbnkosi.watchdog.R;

public class FragmentDonate extends Fragment {
    public static FragmentDonate newInstance() {
        FragmentDonate fragment = new FragmentDonate();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }
}