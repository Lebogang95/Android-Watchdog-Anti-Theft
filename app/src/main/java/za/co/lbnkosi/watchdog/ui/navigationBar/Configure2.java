package za.co.lbnkosi.watchdog.ui.navigationBar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.lbnkosi.watchdog.R;

public class Configure2 extends Fragment {
    public static ConfigureActivity newInstance(){
        ConfigureActivity fragment = new ConfigureActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_account2, container, false);
    }
}
