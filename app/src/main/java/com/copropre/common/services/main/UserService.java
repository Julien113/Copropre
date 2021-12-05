package com.copropre.common.services.main;

import com.copropre.common.models.User;
import com.copropre.common.services.AbstractService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class UserService extends AbstractService {
    private static final String USER_COLLECTION = "users";


    public static Task<Void> createUser(User user) {
        user.setActive(true);
        user.setSudoer(false);
        user.setCreationDate(new Date());
        return db.collection(USER_COLLECTION).document(user.getUserId()).set(user);
    }

    public static Task<DocumentSnapshot> getUser(String uid) {
        return db.collection(USER_COLLECTION).document(uid).get();
    }

}
