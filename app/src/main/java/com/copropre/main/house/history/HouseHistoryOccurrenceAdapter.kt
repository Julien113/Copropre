package com.copropre.main.house.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.copropre.common.models.Occurrence
import com.copropre.common.utils.Utils
import com.copropre.databinding.AdapterHistoryOccurenceBinding

class HouseHistoryOccurrenceAdapter(
    private val dataSet: MutableList<Occurrence>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<HouseHistoryOccurrenceAdapter.ViewHolder>() {
    private var _binding: AdapterHistoryOccurenceBinding? = null
    private val binding get() = _binding!!
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, binding: AdapterHistoryOccurenceBinding) :
        RecyclerView.ViewHolder(view) {
        var binding: AdapterHistoryOccurenceBinding = binding
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        _binding = AdapterHistoryOccurenceBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding.root, _binding!!)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val occurrence = dataSet[position]
        viewHolder.binding.tDate.text = Utils.dateFormatDdMMHH.format(occurrence.creationDate)
        viewHolder.binding.tNameUser.text = occurrence.localParticipant?.surname
        viewHolder.binding.tPoints.text = occurrence.value.toString()
        if (occurrence.noTaskName !== null && occurrence.noTaskName.isNotBlank()) {
            viewHolder.binding.tTaskName.text = occurrence.noTaskName
        } else {
            viewHolder.binding.tTaskName.text = occurrence.localTask?.name
        }
        viewHolder.binding.tTxtPoints.text = if (occurrence.value > 1) " points" else " point"

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}