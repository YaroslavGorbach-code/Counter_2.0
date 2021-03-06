package com.yaroslavgorbachh.counter.Activityes;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Fragments.CountersFragment;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.CreateCounterDialog;
import com.yaroslavgorbachh.counter.RecyclerViews.GroupList_rv;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CountersViewModel;
import com.yaroslavgorbachh.counter.ViewModels.MainActivityViewModel;


public class MainActivity extends AppCompatActivity{

    private GroupList_rv mGroupsList;
    private MainActivityViewModel mViewModel;
    private DrawerLayout mDrawer;
    private LinearLayout mAllCounters_navigationItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*initialize fields*/
        mDrawer = findViewById(R.id.drawer);
        mAllCounters_navigationItem = findViewById(R.id.AllCounters);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setUpNavControllers();

        /*adding toggle*/
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawer.addDrawerListener(toggle);
//        toggle.syncState();


        /*setting the fragment with all the counters*/
        mAllCounters_navigationItem.setOnClickListener(i->{
//            CountersFragment fragment = new CountersFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.hostFragment, fragment).commit();
//            mToolbar.setTitle(R.string.AllCountersItem);
            mDrawer.closeDrawer(GravityCompat.START);
        });

        /*initialize RecyclerView and it listener for groups*/
        mGroupsList = new GroupList_rv(findViewById(R.id.groupsList_rv), new GroupList_rv.Listener() {

            /*setting the fragment with all the counters which belong to a certain group*/
            @Override
            public void onOpen(String string) {
//                Bundle arg = new Bundle();
//                arg.putString("group_title", string);
//                CountersFragment fragment = new CountersFragment();
//                fragment.setArguments(arg);
//                getSupportFragmentManager().beginTransaction().replace(R.id.hostFragment, fragment).commit();
//                mToolbar.setTitle(string);
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });

        mViewModel.getGroups().observe(this, groups -> {
             mGroupsList.setGroups(Utility.deleteTheSameGroups(groups));
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setUpNavControllers() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_mainActivity);
        toolbar.inflateMenu(R.menu.menu_counter_main_activity);
        toolbar.setOnMenuItemClickListener(i->{
            if (i.getItemId() == R.id.counterAdd) {
                new CreateCounterDialog().show(getSupportFragmentManager(), "Add Counter");
            }
            return true;
        });

        NavController navController = Navigation.findNavController(this, R.id.hostFragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.counterFragment:
                    toolbar.setVisibility(View.GONE);
                    break;
                case R.id.createEditCounterFragment:
                    toolbar.setVisibility(View.GONE);
                    break;
                case R.id.countersFragment:
                    toolbar.setVisibility(View.VISIBLE);
                    break;
                case R.id.counterHistoryFragment:
                    toolbar.setVisibility(View.GONE);
                    break;
            }
        });
    }
}
