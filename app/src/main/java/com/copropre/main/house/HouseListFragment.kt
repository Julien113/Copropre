package com.copropre.main.house

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.models.UserHouseLink
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentHouseListBinding
import com.google.firebase.firestore.ktx.toObjects
import java.util.*
import com.google.firebase.firestore.DocumentSnapshot




class HouseListFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseListBinding? = null
    private val binding get() = _binding!!

    private var fabExtended = false

    private var houseList = mutableListOf<House>()

    private lateinit var houseListAdapter : HouseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bAddJoinHouse.setOnClickListener(this)
        binding.bCreateHouse.setOnClickListener(this)
        binding.bJoinHouse.setOnClickListener(this)
        houseListAdapter = HouseListAdapter(houseList)
        binding.rvHouses.adapter = houseListAdapter
        binding.rvHouses.layoutManager = LinearLayoutManager(context)

        HouseService.getMyHouses(AuthService.auth.uid) {
            if (it.isSuccessful) {
                houseList.clear()
                for (snapshot in it.result!!)
                    houseList.add(snapshot.toObject(House::class.java))
                houseListAdapter.notifyDataSetChanged()
            } else {
                Log.e("HouseList", "Can get my House list")
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

            }
        }
    }

    private fun openHouseFab() {
        fabExtended = !fabExtended

        binding.bCreateHouse.visibility = if (fabExtended)  View.VISIBLE else View.GONE
        binding.bJoinHouse.visibility = if (fabExtended)  View.VISIBLE else View.GONE
        binding.bAddJoinHouse.setImageResource(if (fabExtended) R.drawable.outline_close_24 else R.drawable.outline_add_24)

        houseListAdapter.notifyDataSetChanged()
    }

}