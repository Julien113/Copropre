package com.copropre.common.services.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.copropre.common.models.CPTask;
import com.copropre.common.models.House;
import com.copropre.common.models.Occurence;
import com.copropre.common.models.Participant;
import com.copropre.common.services.AbstractService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService extends AbstractService {
    private static final String TASK_COLLECTION = "tasks";
    private static final String OCCURENCE_COLLECTION = "occurences";


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

    public static void completeTask(CPTask cpTask, String uid, String participantId, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        Occurence occurence = new Occurence(cpTask.getTaskId(),cpTask.getValue(), uid, participantId);

        db.collection(OCCURENCE_COLLECTION)
                .add(occurence)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {

                            db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    occurence.setOccurenceId(task.getResult().getId());

                                    // prepare
                                    DocumentSnapshot participantSnap = transaction.get(db.collection(HouseService.PARTICIPANT_COLLECTION).document(participantId));
                                    long participantTotalValue = participantSnap.getLong("totalValue") + cpTask.getValue();

                                    transaction.update(db.collection(OCCURENCE_COLLECTION).document(occurence.getOccurenceId()), "occurenceId", task.getResult().getId());

                                    // met a jour la prochaine date de la tache
                                    Date currentDate = new Date();
                                    if (!(cpTask.getPeriodicity() == null || cpTask.getPeriodicity() == 0)) {
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(currentDate);
                                        c.add(Calendar.DATE, cpTask.getPeriodicity());
                                        cpTask.setNextDate(c.getTime());
                                    }
                                    // met a jour la derniere date de la tache
                                    cpTask.setLastDate(currentDate);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("nextDate", cpTask.getNextDate());
                                    map.put("lastDate", cpTask.getLastDate());
                                    map.put("updateDate", currentDate);
                                    map.put("userUpdate", uid);
                                    transaction.update(db.collection(TASK_COLLECTION).document(cpTask.getTaskId()), map);

                                    // modifie la value du participant
                                    try {
                                        transaction.update(db.collection(HouseService.PARTICIPANT_COLLECTION).document(participantId),"totalValue",participantTotalValue);
                                    } catch (NullPointerException e) {
                                        throw new FirebaseFirestoreException("Could not retrieve value of Participant", FirebaseFirestoreException.Code.ABORTED);
                                    }

                                    return null;
                                }
                            }).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);

                        } else {
                            onFailureListener.onFailure(task.getException());
                        }
                    }
                });
    }


}
