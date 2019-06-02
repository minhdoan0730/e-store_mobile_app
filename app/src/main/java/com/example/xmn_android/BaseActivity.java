package com.example.xmn_android;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.RelativeLayout;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseActivity extends AppCompatActivity implements ProductAdapter.AdapterCallback{
    protected TextView mCartCounter;
    protected int badgeCount;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected Toolbar mToolbar;
    protected Boolean loginState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Paper.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupNavDrawer();
        setupBadge();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }

        return mToolbar;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
    }

    private void setupNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (mDrawerLayout == null) {
            return;
        }
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        if (header != null){
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
        }
        navigationView.getMenu().setGroupVisible(0, false);
        User user_token = Paper.book().read("user_info", null);
        if (user_token != null) {
            final View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_authen);
            navigationView.inflateMenu(R.menu.drawer_menu_authen);
            Menu menu = navigationView.getMenu();
            TextView txtUserName = (TextView) headerLayout.findViewById(R.id.txt_user_name);
            TextView txtUserEmail = (TextView) headerLayout.findViewById(R.id.txt_user_email);
            txtUserName.setText(user_token.getName());
            txtUserEmail.setText(user_token.getEmail());
        }
        else {
            setupNavDrawerToMain();
//            navigationView.inflateHeaderView(R.layout.nav_header_main);
//            navigationView.inflateMenu(R.menu.drawer_menu);
        }

        navigationView.setNavigationItemSelectedListener(navigationViewListener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupNavDrawerToAuthen() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView.getHeaderView(0) != null){
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
        }
        navigationView.getMenu().setGroupVisible(0, false);

        navigationView.inflateHeaderView(R.layout.nav_header_authen);
        navigationView.inflateMenu(R.menu.drawer_menu_authen);
        navigationView.setNavigationItemSelectedListener(navigationViewListener);
    }

    private void setupNavDrawerToMain() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView.getHeaderView(0) != null){
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
        }
        navigationView.getMenu().setGroupVisible(0, false);

        navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.drawer_menu);
        navigationView.setNavigationItemSelectedListener(navigationViewListener);
    }

    private NavigationView.OnNavigationItemSelectedListener navigationViewListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_login_item:
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            break;
                        case R.id.nav_signup_item:
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("SIGNUP", 1);
                            startActivity(intent);
                            break;
                        case R.id.nav_authen_tracking_item:
                            startActivity(new Intent(getApplicationContext(), TrackingOrderActivity.class));
                            break;
                        case R.id.nav_authen_logout_item:
                            Paper.book().delete("user_info");
                            Toast.makeText(getApplicationContext(), "Logout successfully!", Toast.LENGTH_SHORT).show();
                            setupNavDrawerToMain();
                            break;
                    }

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.shopping_cart).getActionView();

        mCartCounter = (TextView) badgeLayout.findViewById(R.id.cart_badge);
        setupBadge();

        final MenuItem shoppingCartAction = menu.findItem(R.id.shopping_cart);
        shoppingCartAction.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.search:{
                return true;
            }
            case R.id.shopping_cart:{
                item.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                        getApplicationContext().startActivity(intent);
                    }
                });
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
            else {
                super.onBackPressed();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    protected void setupBadge() {
        ShoppingCartHelper.saveBadgeCount();
        if (mCartCounter != null) {
            badgeCount = Paper.book().read("badge_count", 0);
            if (badgeCount == 0) {
                if (mCartCounter.getVisibility() != View.GONE) {
                    mCartCounter.setVisibility(View.GONE);
                }
            } else {
                mCartCounter.setText(String.valueOf(Math.min(badgeCount, 99)));
                if (mCartCounter.getVisibility() != View.VISIBLE) {
                    mCartCounter.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @NonNull
    protected ProgressDialog getProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.isIndeterminate();
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public  void updateBadgeCount() {
        setupBadge();
    }

    protected Boolean isAuthen() {
        User user_info = Paper.book().read("user_info", null);
        if (user_info != null) {
            return true;
        }
        return false;
    };
}
