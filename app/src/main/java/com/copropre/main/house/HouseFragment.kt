package com.copropre.main.house

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.services.common.TopBarService
import com.copropre.common.models.Participant
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentHouseBinding
import com.google.android.material.tabs.TabLayoutMediator

class HouseFragment(private val house: House): Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_HOUSE, house.name)


        val pagerAdapter = HousePageAdapter(this, house)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tableLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Taches"
                    //tab.icon = resources.getDrawable(R.drawable.outline_close_24)
                }
                1 -> {
                    tab.text = "Historique"
                    //tab.icon = resources.getDrawable(R.drawable.outline_close_24)
                }
                2 -> {
                    tab.text = "Equilibre"
                    //tab.icon = resources.getDrawable(R.drawable.outline_close_24)
                }
            }
        }.attach()

        HouseService.getParticipant(house.houseId, AuthService.getCurrentUser().userId).addOnSuccessListener {
            if (!it.isEmpty) {
                house.myParticipant = it.documents[0].toObject(Participant::class.java)
            } else {
                Log.e("HouseFragment", "Error while finding mParticipant")
            }
        }

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
}