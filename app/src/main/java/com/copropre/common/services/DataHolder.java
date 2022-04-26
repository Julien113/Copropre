package com.copropre.common.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.copropre.common.models.House;
import com.copropre.common.models.Participant;
import com.copropre.common.services.main.AuthService;
import com.copropre.common.services.main.HouseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataHolder {

    private final static List<House> myHouseList = new ArrayList<>();
    private final static List<Participant> myParticipations = new ArrayList<>();
    private final static List<RecyclerView.Adapter> myParticipationsLinkedAdapters = new ArrayList<>();

    private static int nbHouseToRetrieve = 0;


    public static List<House> getMyHouseList() {
        return myHouseList;
    }

    public static List<RecyclerView.Adapter> getMyParticipationsLinkedAdapters() {
        return myParticipationsLinkedAdapters;
    }

    public static void loadAppDatas() {
        // load houses with participants
        myParticipations.clear();
        myHouseList.clear();
        HouseService.getMyParticipations(AuthService.auth.getUid(), getMyParticipationsListener);

    }
    private static OnCompleteListener<QuerySnapshot> getMyParticipationsListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                nbHouseToRetrieve = task.getResult().size();
                for (DocumentSnapshot participationSnapshot : task.getResult().getDocuments()) {
                    Participant participant = participationSnapshot.toObject(Participant.class);
                    myParticipations.add(participant);
                    HouseService.getHouse(participant.getHouseId()).addOnCompleteListener(getMyHouseListener);
                }

            } else {
                Log.e("DataHolder", "Can get my Participations");
                task.getException().printStackTrace();
            }
        }
    };

    private static OnCompleteListener<DocumentSnapshot> getMyHouseListener = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful() && task.getResult() != null) {
                House house = task.getResult().toObject(House.class);

                // récupère le participant correspondant à l'user
                Optional<Participant> optMParticipant = myParticipations.stream()
                        .filter(p -> {
                            return p.getHouseId().equals(house.getHouseId());
                        }).findFirst();
                optMParticipant.ifPresent(participant -> house.localMyParticipant = participant);
                myHouseList.add(house);
                // update adapters
                if (nbHouseToRetrieve == myHouseList.size()) {
                    myParticipationsLinkedAdapters.forEach(adapter -> {
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            } else {
                Log.e("DataHolder", "Can get my Participations");

                task.getException().printStackTrace();
            }
        }
    };

}
