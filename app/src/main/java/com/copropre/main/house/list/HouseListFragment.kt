package com.copropre.main.house.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentHouseListBinding
import androidx.recyclerview.widget.DividerItemDecoration
import com.copropre.common.models.Participant
import com.copropre.common.services.common.TopBarService
import com.copropre.main.house.join.HouseJoinFragment
import com.copropre.main.house.NewHouseFragment


class HouseListFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseListBinding? = null
    private val binding get() = _binding!!

    private var fabExtended = false

    private var houseList = mutableListOf<House>()

    private var shortAnimationDuration: Int = 200

    private lateinit var houseListAdapter : HouseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fabExtended = false
        _binding = FragmentHouseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_HOUSE_LIST)
        binding.bAddJoinHouse.setOnClickListener(this)
        binding.bCreateHouse.setOnClickListener(this)
        binding.bJoinHouse.setOnClickListener(this)
        houseListAdapter = HouseListAdapter(houseList, this)
        binding.rvHouses.adapter = houseListAdapter
        var linearLayoutManager = LinearLayoutManager(context)
        binding.rvHouses.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation
        )
        binding.rvHouses.addItemDecoration(dividerItemDecoration)

        var myParticipations: ArrayList<Participant> = arrayListOf()
        HouseService.getMyHousesWithMyParticipants(AuthService.auth.uid, {
            if (it.isSuccessful) {
                houseList.clear()
                for (snapshot in it.result!!) {
                    var house = snapshot.toObject(House::class.java)
                    // récupère le participant
                    var optionalMParticipant = myParticipations.stream()
                        .filter { particip -> particip.houseId.equals(house.houseId) }.findFirst()
                    if (optionalMParticipant.isPresent)
                        house.localMyParticipant = optionalMParticipant.get()
                    houseList.add(house)
                }
                houseListAdapter.notifyDataSetChanged()
            } else {
                Log.e("HouseList", "Can get my House list")
                it.exception!!.printStackTrace()
            }

        }, myParticipations)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bAddJoinHouse -> {
                openHouseFab()

            }
            R.id.bCreateHouse -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, NewHouseFragment())
                    .addToBackStack("newHouse")
                    .commitAllowingStateLoss()
            }
            R.id.bJoinHouse -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, HouseJoinFragment())
                    .addToBackStack("joinHouse")
                    .commitAllowingStateLoss()
            }
        }
    }

    private fun openHouseFab() {
        fabExtended = !fabExtended

        binding.bCreateHouse.visibility = if (fabExtended)  View.VISIBLE else View.GONE
        binding.bJoinHouse.visibility = if (fabExtended)  View.VISIBLE else View.GONE
        binding.bAddJoinHouse.setImageResource(if (fabExtended) R.drawable.outline_close_24 else R.drawable.outline_add_24)

        if (fabExtended) {
            binding.bCreateHouse.apply {
                // Set the content view to 0% opacity but visible, so that it is visible
                // (but fully transparent) during the animation.
                alpha = 0f
                visibility = View.VISIBLE

                // Animate the content view to 100% opacity, and clear any animation
                // listener set on the view.
                animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration.toLong())
                    .setInterpolator(LinearOutSlowInInterpolator())
                    .setListener(null)
            }
            binding.bJoinHouse.apply {
                // Set the content view to 0% opacity but visible, so that it is visible
                // (but fully transparent) during the animation.
                alpha = 0f
                visibility = View.VISIBLE

                // Animate the content view to 100% opacity, and clear any animation
                // listener set on the view.
                animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration.toLong())
                    .setInterpolator(LinearOutSlowInInterpolator())
                    .setListener(null)
            }
        }

        houseListAdapter.notifyDataSetChanged()
    }

}