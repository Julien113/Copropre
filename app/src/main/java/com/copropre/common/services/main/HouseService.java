package com.copropre.common.services.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.copropre.common.models.House;
import com.copropre.common.models.Participant;
import com.copropre.common.services.AbstractService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HouseService extends AbstractService {
    private static final String HOUSE_COLLECTION = "houses";
    public static final String PARTICIPANT_COLLECTION = "participants";


    public static void createHouse(House house, String participantSurname, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        // vérifie que l'user est bien co
        if (auth.getCurrentUser() == null) {
            Log.e("HouseService", "createHouse: User isn't logged in");
            return;
        }
        // créé la house en db
        house.setCreationDate(new Date());
        house.setUserCreation(auth.getUid());
        db.collection(HOUSE_COLLECTION).add(house).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String houseId = task.getResult().getId();
                    house.setHouseId(houseId);
                    //TODO peut etre faire ça en firebasefunction?
                    db.collection(HOUSE_COLLECTION).document(houseId).set(house).addOnFailureListener(onFailureListener);

                    // creer le participant
                    addParticipant(houseId, auth.getUid(), participantSurname, onCompleteListener, onFailureListener);
                } else {
                    onFailureListener.onFailure(task.getException());
                }
            }
        });
    }


    public static Task<DocumentReference> addParticipant(String houseId, String participantId, String surname, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        Participant participant = new Participant(houseId, participantId, surname, auth.getUid(), 0);
        participant.setCreationDate(new Date());
        return db.collection(PARTICIPANT_COLLECTION).add(participant).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    //TODO peut etre faire ça en firebasefunction?
                    Log.e("oncomplete","1");
                    String participantId = task.getResult().getId();
                    participant.setParticipantId(participantId);
                    db.collection(PARTICIPANT_COLLECTION).document(participantId).set(participant).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
                }
            }
        });
    }

    public static void getMyHouses(String userId, OnCompleteListener<QuerySnapshot> listener) {
        db.collection(PARTICIPANT_COLLECTION)
                .whereEqualTo("userId", userId)
                //.whereEqualTo("active", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Participant> participations = queryDocumentSnapshots.toObjects(Participant.class);
                        if (!participations.isEmpty()) {
                            List<String> houseIds = participations
                                    .stream()
                                    .map(Participant::getHouseId)
                                    .collect(Collectors.toList());
                            db.collection(HOUSE_COLLECTION).whereIn("houseId", houseIds).get().addOnCompleteListener(listener);
                        }
                    }
                });
    }


    public static Task<DocumentSnapshot> getHouse(String houseId) {
        return  db.collection(HOUSE_COLLECTION).document(houseId).get();
    }

    public static Task<QuerySnapshot> getParticipant(String houseId, String userId) {
        return  db.collection(PARTICIPANT_COLLECTION).whereEqualTo("houseId",houseId).whereEqualTo("userId",userId).get();
    }

    public static Task<QuerySnapshot> getParticipants(String houseId) {
        return  db.collection(PARTICIPANT_COLLECTION).whereEqualTo("houseId",houseId).get();
    }

    public static Task<Void> addParticipantFromFictif(Participant participant, String userId) {
        participant.setUserId(userId);
        participant.setUpdateDate(new Date());
        return db.collection(PARTICIPANT_COLLECTION).document(participant.getParticipantId()).set(participant);
    }
}
