package com.copropre.main.house.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.copropre.R
import com.copropre.common.models.CPTask
import com.copropre.common.models.House
import com.copropre.common.models.Occurrence
import com.copropre.common.models.Participant
import com.copropre.common.services.common.TopBarService
import com.copropre.common.services.main.HouseService
import com.copropre.common.services.main.TaskService
import com.copropre.common.utils.Utils
import com.copropre.databinding.FragmentHouseHistoryBinding

class HouseHistoryFragment(private val house: House) : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterHouseHistoryOccurrence: HouseHistoryOccurrenceAdapter
    private var occurrences = arrayListOf<Occurrence>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_HOUSE_HISTORY)


        adapterHouseHistoryOccurrence =
            HouseHistoryOccurrenceAdapter(occurrences, this)
        binding.rvHistory.adapter = adapterHouseHistoryOccurrence
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvHistory.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation
        )
        binding.rvHistory.addItemDecoration(dividerItemDecoration)

        getHouseOccurrences()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bCreate -> {
            }
        }
    }

    private fun getHouseOccurrences() {
        TaskService.getOccurrencesOfHouse(house.houseId).addOnCompleteListener{
            if (it.isSuccessful) {
                occurrences.clear()

                var associated = 0;
                val taskIds = arrayListOf<String>()
                val participantIds = arrayListOf<String>()
                for (snapshot in it.result!!) {
                    val occurrence = snapshot.toObject(Occurrence::class.java)
                    occurrences.add(occurrence)
                    taskIds.add(occurrence.taskId)
                    participantIds.add(occurrence.participant)
                }
                if (occurrences.isEmpty())
                    return@addOnCompleteListener
                occurrences.sortWith(Utils.sortOccurrenceByDate)

                // Get Associated Tasks
                TaskService.getMultipleTasks(taskIds).addOnCompleteListener { taskSnapshot ->
                    if (taskSnapshot.isSuccessful) {
                        val tasksMap = hashMapOf<String,CPTask>()
                        for (task in taskSnapshot.result!!.toObjects(CPTask::class.java)) {
                            tasksMap[task.taskId] = task
                        }
                        linkTaskWithOccurrence(tasksMap)

                        associated++
                        if (associated == 2)
                            adapterHouseHistoryOccurrence.notifyDataSetChanged()
                    } else {
                        Log.e("OccurrenceList", "Cant tasks associated with occurrence list")
                        taskSnapshot.exception!!.printStackTrace()
                    }
                }

                // Get Associated Participants
                HouseService.getMultipleParticipants(participantIds).addOnCompleteListener { taskSnapshot ->
                    if (taskSnapshot.isSuccessful) {
                        val participantMap = hashMapOf<String,Participant>()
                        for (participant in taskSnapshot.result!!.toObjects(Participant::class.java)) {
                            participantMap[participant.participantId] = participant
                        }
                        linkParticipantWithOccurrence(participantMap)

                        associated++
                        if (associated == 2)
                            adapterHouseHistoryOccurrence.notifyDataSetChanged()
                    } else {
                        Log.e("OccurrenceList", "Cant participant associated with occurrence list")
                        taskSnapshot.exception!!.printStackTrace()
                    }
                }
            } else {
                Log.e("OccurrenceList", "Cant get house occurrence list")
                it.exception!!.printStackTrace()
            }
        }
    }

    private fun linkTaskWithOccurrence(tasks: Map<String, CPTask>) {
        for (occurrence in occurrences) {
            occurrence.localTask = tasks[occurrence.taskId]
        }
    }

    private fun linkParticipantWithOccurrence(participants: Map<String, Participant>) {
        for (occurrence in occurrences) {
            occurrence.localParticipant = participants[occurrence.participant]
        }
    }

}