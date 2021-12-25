package com.copropre.common.services.common;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.copropre.MainActivity;
import com.copropre.R;
import com.copropre.common.services.main.AuthService;
import com.copropre.main.login.LogInFragment;
import com.copropre.main.parameters.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class TopBarService {
    public static MaterialToolbar topBar;
    public enum FragmentName {FRAGMENT_LOGIN,FRAGMENT_NEW_TASK ,FRAGMENT_NEW_HOUSE,FRAGMENT_PROFILE ,FRAGMENT_SIGNIN, FRAGMENT_HOUSE_HISTORY, FRAGMENT_HOUSE, FRAGMENT_HOUSE_BALANCE, FRAGMENT_HOUSE_JOIN, FRAGMENT_HOUSE_LIST, FRAGMENT_HOUSE_TASK}

    public static void setFragmentLogin(FragmentManager supportFragmentManager){
        LogInFragment fragmentLogin = new LogInFragment();
        supportFragmentManager.beginTransaction()
                .add(R.id.container, fragmentLogin)
                .addToBackStack("login")
                .commitAllowingStateLoss();
    }

    public static void initTopBar(FragmentManager supportFragmentManager) {
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.mbLogin:
                        setFragmentLogin(supportFragmentManager);
                        break;
                    case R.id.mbLogout:
                        AuthService.getAuth().signOut();
                        setFragmentLogin(supportFragmentManager);
                        break;
                    case R.id.mbProfile:
                        ProfileFragment fragmentProfile = new ProfileFragment();
                        supportFragmentManager.beginTransaction()
                                .addToBackStack("Profile")
                                .add(R.id.container, fragmentProfile)
                                .commitAllowingStateLoss();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public static void changeTopBar(FragmentName fragmentName, String topBarTitle){

        changeTopBar(fragmentName);

        topBar.setTitle(topBarTitle);
        Log.d("poste appelage", "changeTopBar: ");

    }

    public static void changeTopBar(FragmentName fragmentName){
        Menu menu = topBar.getMenu();

        Log.d("appelage", "changeTopBar: ");


        // Option:
        // mbLogin
        // mbPrenium
        // mbLogout
        // mbProfile
        // mbShareApp
        // mbHelp
        //


        menu.setGroupVisible(0, false);
        switch (fragmentName) {
            case FRAGMENT_LOGIN:
            case FRAGMENT_HOUSE_LIST:
                topBar.setTitle(R.string.app_name);
                break;
        }
        switch (fragmentName) {
            case FRAGMENT_LOGIN:
                menu.findItem(R.id.mbRateApp).setVisible(true);
            case FRAGMENT_SIGNIN:
                break;
            case FRAGMENT_HOUSE:
            case FRAGMENT_HOUSE_JOIN:
            case FRAGMENT_HOUSE_LIST:
            case FRAGMENT_NEW_TASK:
            case FRAGMENT_HOUSE_TASK:
            case FRAGMENT_HOUSE_BALANCE:








































            case FRAGMENT_HOUSE_HISTORY:
            case FRAGMENT_NEW_HOUSE:
            case FRAGMENT_PROFILE:
                menu.findItem(R.id.mbPremium).setVisible(true);
                menu.findItem(R.id.mbLogout).setVisible(true);
                menu.findItem(R.id.mbProfile).setVisible(true);
                menu.findItem(R.id.mbShareApp).setVisible(true);
                menu.findItem(R.id.mbRateApp).setVisible(true);
                break;
        }
    }
}
