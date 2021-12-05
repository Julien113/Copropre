package com.copropre.common.services.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.copropre.common.models.House;
import com.copropre.common.models.Participant;
import com.copropre.common.models.UserHouseLink;
import com.copropre.common.services.AbstractService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HouseService extends AbstractService {
    private static final String HOUSE_COLLECTION = "houses";
    private static final String PARTICIPANT_COLLECTION = "participants";
    private static final String USER_HOUSE_LINK_COLLECTION = "user_house_link";


    public static void createHouse(House house, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener) {
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
                    addParticipant(houseId, auth.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                setUserHouseLink(houseId, auth.getUid()).addOnCompleteListener(onCompleteListener);
                            } else {
                                onFailureListener.onFailure(task.getException());
                            }
                        }
                    });
                } else {
                    onFailureListener.onFailure(task.getException());
                }
            }
        });
    }


    public static Task<DocumentReference> addParticipant(String houseId, String participantId) {
        Participant participant = new Participant(houseId, participantId, auth.getUid(), 0);
        participant.setCreationDate(new Date());
        return db.collection(PARTICIPANT_COLLECTION).add(participant).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    //TODO peut etre faire ça en firebasefunction?
                    String participantId = task.getResult().getId();
                    participant.setParticipantId(participantId);
                    db.collection(PARTICIPANT_COLLECTION).document(participantId).set(participant);
                }
            }
        });

    }

    public static Task<DocumentReference> setUserHouseLink(String houseId, String userId) {

        UserHouseLink userHouseLink = new UserHouseLink(userId, houseId, new Date(), true);
        return db.collection(USER_HOUSE_LINK_COLLECTION).add(userHouseLink);
    }


    public static Task<QuerySnapshot> getMyHouses(String userId, OnCompleteListener<QuerySnapshot> listener) {
        return db.collection(USER_HOUSE_LINK_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("active", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<UserHouseLink> userHouseLinks = queryDocumentSnapshots.toObjects(UserHouseLink.class);
                        if (!userHouseLinks.isEmpty()) {
                            List<String> houseIds = userHouseLinks
                                    .stream()
                                    .map(UserHouseLink::getHouseId)
                                    .collect(Collectors.toList());
                            db.collection(HOUSE_COLLECTION).whereIn("houseId", houseIds).get().addOnCompleteListener(listener);
                        }
                    }
                });
    }
}
