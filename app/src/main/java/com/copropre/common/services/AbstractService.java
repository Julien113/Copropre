package com.copropre.common.services;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class AbstractService {
    public static FirebaseDatabase databaseRealtime = FirebaseDatabase.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
}
