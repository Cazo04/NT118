package com.example.nt118.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nt118.HomeEmployee;
import com.example.nt118.Messages;
import com.example.nt118.Notification;
import com.example.nt118.Profile;

public class EmployeeViewPagerAdapter extends FragmentStateAdapter {
    private String manv;
    private String pass;

    public EmployeeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String manv, String pass) {
        super(fragmentActivity);
        this.manv = manv;
        this.pass = pass;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeEmployee();
            case 1:
                return new Messages();
            case 2:
                return new Notification();
            default:
                return Profile.newInstance(manv,pass);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
