package com.copropre.common.services.main;

import android.util.Log;

import com.copropre.common.models.User;
import com.copropre.common.services.AbstractService;
import com.copropre.common.services.DataHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public final class AuthService extends AbstractService {

    private static User currentUser;

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        AuthService.currentUser = currentUser;
        DataHolder.loadAppDatas();
    }
}
