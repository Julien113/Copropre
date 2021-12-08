package com.copropre.main.house.tasks

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
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.common.services.main.TaskService
import com.copropre.common.utils.Utils
import com.copropre.databinding.FragmentHouseTasksBinding
import com.copropre.databinding.FragmentNewHouseBinding
import com.copropre.main.house.list.HouseListAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener

class HouseTasksFragment(private val house: House) : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseTasksBinding? = null
    private val binding get() = _binding!!

    private var taskList = mutableListOf<CPTask>()

    private lateinit var taskListAdapter : TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bNewTask.setOnClickListener(this)

        taskListAdapter = TaskListAdapter(taskList, this)
        binding.rvTasks.adapter = taskListAdapter
        var linearLayoutManager = LinearLayoutManager(context)
        binding.rvTasks.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation
        )
        binding.rvTasks.addItemDecoration(dividerItemDecoration)

        getHouseTasks()

    }

    fun getHouseTasks() {
        TaskService.getHouseTasks(house.houseId).addOnCompleteListener{
            if (it.isSuccessful) {
                taskList.clear()
                for (snapshot in it.result!!)
                    taskList.add(snapshot.toObject(CPTask::class.java))
                taskList.sortWith(Utils.sortTaskByDate)
                taskListAdapter.notifyDataSetChanged()
            } else {
                Log.e("TaskList", "Can get house Tasks list")
                it.exception!!.printStackTrace()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bNewTask -> {

                requireParentFragment().parentFragmentManager
                    .beginTransaction()
                    .add(R.id.container, NewTaskFragment(house,this))
                    .addToBackStack("newTask")
                    .commit()
            }
        }
    }
}