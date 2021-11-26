package com.copropre.common.services.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.copropre.common.services.AbstractService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public final class VersionService extends AbstractService {

    // TODO DELETE CLASS, sera géré par https://developer.android.com/guide/playcore/in-app-updates/kotlin-java#kotlin
    public static String actualVersionText = "0.0.1";
    public static int actualMajorVersion = 1;


    private static String majorVersionURL = "majorVersion";


    public static void getAppVersion(ValueEventListener listener){
        databaseRealtime.getReference(majorVersionURL).addListenerForSingleValueEvent(listener);
    }

    public static void fait() {
        Log.e("c la ", "yo");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

}
