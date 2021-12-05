package com.copropre.main.house.join

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.models.Participant
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.AdapterHouseListBinding
import com.copropre.databinding.AdapterJoinHouseParticipantsFictifsBinding
import com.copropre.main.house.HouseFragment

class HouseJoinPartifipantsFictifsAdapter(
    private val dataSet: MutableList<Participant>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<HouseJoinPartifipantsFictifsAdapter.ViewHolder>() {
    private var _binding: AdapterJoinHouseParticipantsFictifsBinding? = null
    private val binding get() = _binding!!
    private lateinit var house: House

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, binding: AdapterJoinHouseParticipantsFictifsBinding) :
        RecyclerView.ViewHolder(view) {
        var binding: AdapterJoinHouseParticipantsFictifsBinding = binding
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        _binding = AdapterJoinHouseParticipantsFictifsBinding.inflate(
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
        viewHolder.binding.bUseParticipantFictif.setOnClickListener {
            HouseService.addParticipantFromFictif(dataSet[position],AuthService.auth.uid).addOnCompleteListener {
                if (it.isSuccessful) {
                    goToHouse()
                } else {
                    it.exception!!.printStackTrace()
                }
            }

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setHouse(house: House) {
        this.house = house
    }

    fun goToHouse() {
        fragment.parentFragmentManager.beginTransaction()
            .add(R.id.container, HouseFragment(house))
            .addToBackStack("house")
            .detach(fragment)
            .commit()

    }
}