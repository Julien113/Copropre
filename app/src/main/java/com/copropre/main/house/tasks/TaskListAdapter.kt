package com.copropre.main.house.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.copropre.common.models.CPTask
import com.copropre.common.models.House
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.TaskService
import com.copropre.common.utils.Utils
import com.copropre.databinding.AdapterTaskListBinding

class TaskListAdapter (private val dataSet: MutableList<CPTask>, private val house: House, private val fragment: Fragment) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>(){
    private var _binding: AdapterTaskListBinding? = null
    private val binding get() = _binding!!

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, binding: AdapterTaskListBinding) : RecyclerView.ViewHolder(view) {
        var binding: AdapterTaskListBinding = binding
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        _binding = AdapterTaskListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding.root, _binding!!)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val task = dataSet[position]
        viewHolder.binding.tName.text = task.name
        if (task.nextDate !== null) {
            viewHolder.binding.tDate.text = Utils.dateFormatDdMMHH.format(task.nextDate)
        } else {
            if (task.lastDate !== null) {
                val nextDateText = "Fait la derniere fois le " + Utils.dateFormatDdMMHH.format(task.lastDate)
                viewHolder.binding.tDate.text = nextDateText
            } else {
                viewHolder.binding.tDate.text = ""
            }
        }
        viewHolder.binding.bDoTask.setOnClickListener {
            if (house.localMyParticipant !== null) {
                TaskService.completeTask(task, AuthService.getCurrentUser().userId, house.localMyParticipant.participantId, {
                    if (it.isSuccessful) {
                        Toast.makeText(fragment.context, "ok.", Toast.LENGTH_SHORT).show()
                    }

                }, {
                    it.printStackTrace()
                    Toast.makeText(fragment.context, "Could not do the Task. Please try again later.", Toast.LENGTH_LONG).show()
                })
            } else {
                Toast.makeText(fragment.context, "Could not do the Task. Please reload App.", Toast.LENGTH_LONG).show()
            }

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}