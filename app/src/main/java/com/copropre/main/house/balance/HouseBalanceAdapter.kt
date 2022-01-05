package com.copropre.main.house.balance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.models.Participant
import com.copropre.databinding.AdapterBalanceListBinding
import com.copropre.databinding.AdapterHouseListBinding
import com.copropre.main.house.HouseFragment
import kotlin.math.floor

class HouseBalanceAdapter (private val dataSet: List<Participant>, private val house: House, private val fragment: Fragment) :
    RecyclerView.Adapter<HouseBalanceAdapter.ViewHolder>(){
    private var _binding: AdapterBalanceListBinding? = null
    private val binding get() = _binding!!
    var average: Float = 0f
    var maxOffset: Float = 0f

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, binding: AdapterBalanceListBinding) : RecyclerView.ViewHolder(view) {
        var binding: AdapterBalanceListBinding = binding
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        _binding = AdapterBalanceListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding.root, _binding!!)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val participant = dataSet[position]
        binding.tName.text = participant.surname + " : " + participant.totalValue+"pts"
        binding.balanceView.average = average
        binding.balanceView.maxOffset = maxOffset
        binding.balanceView.value = participant.totalValue.toFloat()

        binding.tMax.text = floor(average+maxOffset).toInt().toString()
        binding.tMin.text = floor(average-maxOffset).toInt().toString()

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}