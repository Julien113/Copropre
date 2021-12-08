package com.copropre.common.services.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.copropre.common.models.CPTask;
import com.copropre.common.models.House;
import com.copropre.common.models.Participant;
import com.copropre.common.services.AbstractService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService extends AbstractService {
    private static final String TASK_COLLECTION = "tasks";


    public static void createTask(CPTask cpTask, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        // vérifie que l'user est bien co
        if (auth.getCurrentUser() == null) {
            Log.e("HouseService", "createHouse: User isn't logged in");
            return;
        }
        // créé la tache en db
        cpTask.setCreationDate(new Date());
        cpTask.setUserCreation(auth.getUid());
        db.collection(TASK_COLLECTION).add(cpTask).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String taskId = task.getResult().getId();
                    cpTask.setTaskId(taskId);
                    //TODO peut etre faire ça en firebasefunction?
                    db.collection(TASK_COLLECTION).document(taskId).set(cpTask).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);

                } else {
                    onFailureListener.onFailure(task.getException());
                }
            }
        });
    }

    public static Task<QuerySnapshot> getHouseTasks(String houseId) {
        return db.collection(TASK_COLLECTION)
                .whereEqualTo("houseId", houseId)
                .get();
    }


}
