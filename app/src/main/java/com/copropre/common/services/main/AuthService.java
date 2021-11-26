package com.copropre.common.services.main;

import android.util.Log;

import com.copropre.common.services.AbstractService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public final class AuthService extends AbstractService {
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    public static FirebaseAuth getAuth() {
        return auth;
    }
}
