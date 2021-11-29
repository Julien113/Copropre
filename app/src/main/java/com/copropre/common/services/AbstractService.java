package com.copropre.common.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class AbstractService {
    public static final FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseDatabase databaseRealtime = FirebaseDatabase.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
}
