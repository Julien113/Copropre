package com.copropre.common.services.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.copropre.common.models.House;
import com.copropre.common.models.Participant;
import com.copropre.common.services.AbstractService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HouseService extends AbstractService {
    private static final String HOUSE_COLLECTION = "houses";
    public static final String PARTICIPANT_COLLECTION = "participants";

    public static House selectedHouse;


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

    public static void getMyParticipations(String userId, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection(PARTICIPANT_COLLECTION)
                .whereEqualTo("userId", userId)
                //.whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(onCompleteListener);
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

    public static Task<QuerySnapshot> getMultipleParticipants(List<String> participantsIds) {
        return db.collection(PARTICIPANT_COLLECTION).whereIn("participantId",participantsIds).get();
    }

    public static Task<Void> addParticipantFromFictif(Participant participant, String userId) {
        participant.setUserId(userId);
        participant.setUpdateDate(new Date());
        return db.collection(PARTICIPANT_COLLECTION).document(participant.getParticipantId()).set(participant);
    }

    public static void saveHouse(House house, OnCompleteListener<Void> listener) {
        db.collection(HOUSE_COLLECTION).document(house.getHouseId()).set(house).addOnCompleteListener(listener);
    }

    public static Task<QuerySnapshot> getHouseWithInvitationCode(String invitationCode) {
        return  db.collection(HOUSE_COLLECTION).whereEqualTo("invitationCode", invitationCode).get();
    }

    public static void createNewInvitationCode(House house, OnCompleteListener<Void> listener) {
        // code
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String newInvitationCode = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        house.setInvitationCode(newInvitationCode);

        // date
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 7);
        Date expirationDate = c.getTime();

        house.setInvitationCodeExpirationDate(expirationDate);

        saveHouse(house, listener);
    }
}
