package com.copropre.main.house.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.databinding.AdapterHouseListBinding
import com.copropre.main.house.HouseFragment

class HouseListAdapter (private val dataSet: MutableList<House>, private val fragment: Fragment) :
    RecyclerView.Adapter<HouseListAdapter.ViewHolder>(){
    private var _binding: AdapterHouseListBinding? = null
    private val binding get() = _binding!!

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, binding: AdapterHouseListBinding) : RecyclerView.ViewHolder(view) {
        var binding: AdapterHouseListBinding = binding
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        _binding = AdapterHouseListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding.root, _binding!!)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.binding.tHouseTitle.setText(dataSet[position].name)
        viewHolder.binding.layout.setOnClickListener {
            fragment.parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, HouseFragment(dataSet.get(position)))
                .addToBackStack("house")
                .commitAllowingStateLoss()

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}