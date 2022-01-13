package com.copropre.main.house.history

import android.os.Bundle
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
import com.copropre.common.services.common.TopBarService
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.TaskService
import com.copropre.common.utils.Utils
import com.copropre.databinding.FragmentNewOccurenceBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.Slider
import java.util.*

class HouseHistoryNewOccurrenceFragment(private val house: House, private val houseHistoryFragment: HouseHistoryFragment) : Fragment(), View.OnClickListener {
    private var _binding: FragmentNewOccurenceBinding? = null
    private val binding get() = _binding!!

    private var nameErrors = arrayListOf<String>()
    private var participantErrors = arrayListOf<String>()

    private var dateFirst = Date()
    private var score = 3

    private lateinit var adapterParticipants: HouseHistoryNewOccurrenceParticipantsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewOccurenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_NEW_OCCURENCE)
        binding.bCreateOccurrence.setOnClickListener(this)
        binding.sliderScore.addOnChangeListener { _: Slider, value: Float, _: Boolean ->
            binding.tScore.text = value.toInt().toString()
            score = value.toInt()
        }
        binding.etDate.setOnClickListener(this)
        binding.etDate.setText(Utils.dateFormatDdMMHH.format(dateFirst))

        adapterParticipants =
            HouseHistoryNewOccurrenceParticipantsAdapter(house.participants)
        binding.rvParticipants.adapter = adapterParticipants
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvParticipants.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation
        )
        binding.rvParticipants.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bCreateOccurrence -> {
                createOccurrence()
            }
            R.id.etDate -> {
                val calendarConstraints = CalendarConstraints.Builder()
                val currentDate = Date()
                calendarConstraints.setStart(currentDate.time)

                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.task_new_select_date)
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(calendarConstraints.build())
                        .build()

                datePicker.show(parentFragmentManager,null)
                datePicker.addOnPositiveButtonClickListener {
                    dateFirst = Date(it)
                    binding.etDate.setText(Utils.dateFormatDdMMHH.format(dateFirst))
                }
            }
        }
    }

    private fun createOccurrence() {
        // check name
        val name = binding.etName.text.toString()

        nameErrors.clear()
        participantErrors.clear()
        // check que le nom est rempli
        if (name.isBlank()) {
            nameErrors.add("Veuillez entrer un intitulé")
            setErrorTexts()
            return
        }

        // check qu'au moins un participant est séléctionné
        if (adapterParticipants.selectedParticipant == null) {
            participantErrors.add("Veuillez selectionner un participant")
            setErrorTexts()
            return
        }

        TaskService.createOccurrenceNoTask(score, name, house.houseId, AuthService.getCurrentUser().userId, adapterParticipants.selectedParticipant!!.participantId, {
            houseHistoryFragment.getHouseOccurrences()
            parentFragmentManager.beginTransaction()
                .detach(this)
                .commit()
        }, {
            it.printStackTrace()
        })
    }

    private fun setErrorTexts() {
        binding.tErrorTaskName.visibility = if (nameErrors.isEmpty()) View.GONE else View.VISIBLE
        var textName = ""
        for ((index,error) in nameErrors.withIndex()){
            textName += if (index > 1) error else "\n"+error
        }
        binding.tErrorTaskName.text = textName

        binding.tErrorParticipant.visibility = if (participantErrors.isEmpty()) View.GONE else View.VISIBLE
        var textParticipant = ""
        for ((index,error) in participantErrors.withIndex()){
            textParticipant += if (index > 1) error else "\n"+error
        }
        binding.tErrorParticipant.text = textParticipant
    }

}