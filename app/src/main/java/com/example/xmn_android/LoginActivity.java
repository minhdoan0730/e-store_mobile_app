package com.example.xmn_android;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final TabItem tabLogin = findViewById(R.id.tab_login);
        TabItem tabSignup = findViewById(R.id.tab_signup);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.login_view_pager);
        FragmentManager fragmentManager = getFragmentManager();

        LoginPageAdapter pageAdapter = new LoginPageAdapter(this,
                getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

        int tabIndex = getIntent().getIntExtra("SIGNUP", 0);
        viewPager.setCurrentItem(tabIndex, false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
