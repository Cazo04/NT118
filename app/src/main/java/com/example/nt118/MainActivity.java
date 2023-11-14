package com.example.nt118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.nt118.Adapter.EmployeeViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String manv = intent.getStringExtra("manv");
        String pass = intent.getStringExtra("pass");

        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);

        EmployeeViewPagerAdapter adapter = new EmployeeViewPagerAdapter(this, manv, pass);
        viewPager2.setAdapter(adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.menu_mess).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.menu_notify).setChecked(true);
                        break;
                    default:
                        navigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);
                }
            }
        });
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home)
                    viewPager2.setCurrentItem(0);
                if (item.getItemId() == R.id.menu_mess)
                    viewPager2.setCurrentItem(1);
                if (item.getItemId() == R.id.menu_notify)
                    viewPager2.setCurrentItem(2);
                if (item.getItemId() == R.id.menu_profile)
                    viewPager2.setCurrentItem(3);
                return true;
            }
        });
    }
}