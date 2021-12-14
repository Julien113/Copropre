package com.copropre.main.house.tasks

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.copropre.R
import com.copropre.common.models.CPTask
import com.copropre.common.models.House
import com.copropre.common.services.common.TopBarService
import com.copropre.common.services.main.HouseService
import com.copropre.common.services.main.TaskService
import com.copropre.common.services.main.TaskService.getHouseTasks
import com.copropre.common.utils.Utils
import com.copropre.databinding.FragmentHouseTasksBinding
import com.copropre.databinding.FragmentNewHouseBinding
import com.copropre.databinding.FragmentNewTaskBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.Slider
import java.text.DateFormat
import java.util.*

class NewTaskFragment(private val house: House, private val houseTasksFragment: HouseTasksFragment) : Fragment(), View.OnClickListener {
    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!

    private var nameErrors = arrayListOf<String>()

    private var withDate = false
    private var dateFirst = Date()
    private var score = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_NEW_TASK)
        binding.bCreateTask.setOnClickListener(this)
        binding.sliderScore.addOnChangeListener { _: Slider, value: Float, _: Boolean ->
            binding.tScore.text = value.toInt().toString()
            score = value.toInt()
        }
        binding.etDate.setOnClickListener(this)
        binding.etDate.setText(Utils.dateFormatDdMMHH.format(dateFirst))
        binding.sDate.setOnCheckedChangeListener { _, isChecked ->
            this.withDate = isChecked
            binding.etDate.isEnabled = isChecked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bCreateTask -> {
                createTask()
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

    private fun createTask() {
        // check email
        val name = binding.etName.text.toString()
        nameErrors.clear()
        // check que le nom est rempli
        if (name.isBlank()) {
            nameErrors.add("Veuillez entrer un intitulé")
            setErrorTexts()
            return
        }

        val cpTask = CPTask(house.houseId, name, score, 0, if(withDate) dateFirst else null)
        TaskService.createTask(cpTask, {
            houseTasksFragment.getHouseTasks()
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
    }

}