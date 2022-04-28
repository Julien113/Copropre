package com.copropre.common.services.common;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.copropre.R;
import com.copropre.common.services.main.AuthService;
import com.copropre.common.services.main.HouseService;
import com.copropre.main.house.share.HouseShareFragment;
import com.copropre.main.login.LogInFragment;
import com.copropre.main.parameters.HelpFragment;
import com.copropre.main.parameters.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class TopBarService {
    public static MaterialToolbar topBar;
    public enum FragmentName {FRAGMENT_LOGIN,FRAGMENT_NEW_TASK ,FRAGMENT_NEW_OCCURENCE ,FRAGMENT_NEW_HOUSE,FRAGMENT_PROFILE ,FRAGMENT_SIGNIN, FRAGMENT_HOUSE_HISTORY, FRAGMENT_HOUSE, FRAGMENT_SHARE, FRAGMENT_HOUSE_BALANCE, FRAGMENT_HOUSE_JOIN, FRAGMENT_HOUSE_LIST, FRAGMENT_HOUSE_TASK}

    public static void setFragmentLogin(FragmentManager supportFragmentManager){
        LogInFragment fragmentLogin = new LogInFragment();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        for (Fragment fragment: supportFragmentManager.getFragments()) {
            transaction.remove(fragment);
        }
        transaction.add(R.id.container, fragmentLogin)
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
                                .replace(R.id.container, fragmentProfile)
                                .commitAllowingStateLoss();
                        break;
                    case R.id.mbHelp:
                        HelpFragment fragmentHelp = new HelpFragment();
                        supportFragmentManager.beginTransaction()
                                .addToBackStack("Help")
                                .replace(R.id.container, fragmentHelp)
                                .commitAllowingStateLoss();

                    case R.id.mbInvitation:
                        HouseShareFragment shareFragment = new HouseShareFragment(HouseService.selectedHouse);
                        supportFragmentManager.beginTransaction()
                                .addToBackStack("InviteToHouse")
                                .replace(R.id.container, shareFragment)
                                .commitAllowingStateLoss();

                    default:
                        break;
                }
                return false;
            }
        });
        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.popBackStack();
            }
        });
    }

    public static void changeTopBar(FragmentName fragmentName, String topBarTitle){
        topBar.setTitle(topBarTitle);

        changeTopBarMenuItems(fragmentName);
    }

    public static void changeTopBar(FragmentName fragmentName) {
        changeTopBarTitle(fragmentName);

        changeTopBarMenuItems(fragmentName);
    }


    public static void changeTopBarTitle(FragmentName fragmentName){
        Menu menu = topBar.getMenu();

        menu.setGroupVisible(0, false);
        switch (fragmentName) {
            case FRAGMENT_SHARE:
                topBar.setTitle("Inviter des participants");
                break;
            case FRAGMENT_NEW_HOUSE:
                topBar.setTitle("Créer un foyer");
                break;
            case FRAGMENT_HOUSE_JOIN:
                topBar.setTitle("Rejoindre un foyer");
                break;
            case FRAGMENT_NEW_TASK:
                topBar.setTitle("Nouvelle tâche");
                break;
            case FRAGMENT_SIGNIN:
                topBar.setTitle("Inscription");
                break;
            case FRAGMENT_LOGIN:
            case FRAGMENT_HOUSE_LIST:
                topBar.setTitle(R.string.app_name);
                break;
        }
    }

    public static void changeTopBarMenuItems(FragmentName fragmentName){
        Menu menu = topBar.getMenu();

        setMenuItem(false, R.id.mbShareApp, R.id.mbRateApp, R.id.mbChangeHouse,
                R.id.mbHelp, R.id.mbLogin, R.id.mbLogout, R.id.mbInvitation, R.id.mbPremium,
                R.id.mbProfile);

        switch (fragmentName) {
            case FRAGMENT_LOGIN:
                setMenuItem(true, R.id.mbRateApp);
            case FRAGMENT_SIGNIN:
                break;
            case FRAGMENT_HOUSE_LIST:
            case FRAGMENT_HOUSE_JOIN:

            case FRAGMENT_NEW_HOUSE:
            case FRAGMENT_NEW_OCCURENCE:
            case FRAGMENT_PROFILE:
                setMenuItem(true, R.id.mbPremium, R.id.mbLogout, R.id.mbProfile, R.id.mbShareApp, R.id.mbRateApp);
                break;
            // HOUSE BLOCK
            case FRAGMENT_SHARE:
                break;
            case FRAGMENT_NEW_TASK:
            case FRAGMENT_HOUSE_TASK:
            case FRAGMENT_HOUSE_BALANCE:
            case FRAGMENT_HOUSE_HISTORY:
            case FRAGMENT_HOUSE:
                menu.findItem(R.id.mbPremium).setVisible(true);
                menu.findItem(R.id.mbInvitation).setVisible(true);
                menu.findItem(R.id.mbChangeHouse).setVisible(true);
                menu.findItem(R.id.mbHelp).setVisible(true);
                setMenuItem(true, R.id.mbPremium, R.id.mbInvitation, R.id.mbChangeHouse, R.id.mbHelp);
                break;
        }

        switch (fragmentName) {
            case FRAGMENT_HOUSE_LIST:
                topBar.setNavigationIcon(null);
                break;
            default:
                topBar.setNavigationIcon(R.drawable.arrow_back);

        }
    }

    private static void setMenuItem(boolean visible, int... items) {
        Menu menu = topBar.getMenu();
        for (int item: items) {
            menu.findItem(item).setVisible(visible);
        }

    }
}
