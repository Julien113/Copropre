package com.copropre.main.house.balance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.models.Participant
import com.copropre.common.services.common.TopBarService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentHouseBalanceBinding
import com.copropre.databinding.FragmentNewHouseBinding
import com.copropre.main.house.list.HouseListAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener

class HouseBalanceFragment(private val house: House) : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseBalanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var houseBalanceAdapter: HouseBalanceAdapter
    private var praticipantList = mutableListOf<Participant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_HOUSE_BALANCE)

        houseBalanceAdapter = HouseBalanceAdapter(praticipantList, house, this)
        binding.rvBalance.adapter = houseBalanceAdapter
        var linearLayoutManager = LinearLayoutManager(context)
        binding.rvBalance.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation
        )
        binding.rvBalance.addItemDecoration(dividerItemDecoration)

        HouseService.getParticipants(house.houseId).addOnSuccessListener { participants ->
            praticipantList.clear()
            praticipantList.addAll(participants.toObjects(Participant::class.java))
            if (house.participants == null)
                house.participants = arrayListOf()
            house.participants.clear()
            house.participants.addAll(participants.toObjects(Participant::class.java))

            //calcul average
            var average = 0f
            for (participant in house.participants) {
                average += participant.totalValue
            }
            if (house.participants.isNotEmpty())
                average /= house.participants.size
            houseBalanceAdapter.average = average
            //calcul max offset
            var maxOffset = 0f
            for (participant in house.participants) {
                if (Math.abs(average - participant.totalValue) > maxOffset)
                    maxOffset = Math.abs(average - participant.totalValue)
            }
            houseBalanceAdapter.maxOffset = maxOffset

            Log.e("data","setchan" + average +"   "+maxOffset+ " eu"+house.participants.toString())
            houseBalanceAdapter.notifyDataSetChanged()

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