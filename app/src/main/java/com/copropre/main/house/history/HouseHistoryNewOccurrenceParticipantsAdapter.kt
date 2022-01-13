package com.copropre.main.house.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.models.Participant
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.AdapterHistoryNewOccurrenceParticipantsBinding
import com.copropre.main.house.HouseFragment

class HouseHistoryNewOccurrenceParticipantsAdapter(
    private val dataSet: MutableList<Participant>
) :
    RecyclerView.Adapter<HouseHistoryNewOccurrenceParticipantsAdapter.ViewHolder>() {
    private var _binding: AdapterHistoryNewOccurrenceParticipantsBinding? = null
    private val binding get() = _binding!!

    private var lastRadioButton: RadioButton? = null

    public var selectedParticipant: Participant? = null

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, binding: AdapterHistoryNewOccurrenceParticipantsBinding) :
        RecyclerView.ViewHolder(view) {
        var binding: AdapterHistoryNewOccurrenceParticipantsBinding = binding
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        _binding = AdapterHistoryNewOccurrenceParticipantsBinding.inflate(
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
        viewHolder.binding.tName.text = dataSet[position].surname
        viewHolder.binding.cbSelectParticipant.setOnCheckedChangeListener {
                buttonView, isChecked ->
            if (isChecked) {
                if (lastRadioButton !== null) {
                    lastRadioButton!!.isChecked = false
                }
                lastRadioButton = viewHolder.binding.cbSelectParticipant
                selectedParticipant = dataSet[position]
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}