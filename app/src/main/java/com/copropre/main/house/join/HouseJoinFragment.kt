package com.copropre.main.house.join

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
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentHouseJoinBinding
import com.copropre.main.house.HouseFragment
import java.util.*
import kotlin.collections.ArrayList

class HouseJoinFragment() : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseJoinBinding? = null
    private val binding get() = _binding!!


    private val houseCodeErrors = ArrayList<String>()

    private lateinit var adapterPartifipantsFictif: HouseJoinPartifipantsFictifsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseJoinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_HOUSE_JOIN)
        binding.bJoin.setOnClickListener(this)
        binding.bJoin2.setOnClickListener(this)

        adapterPartifipantsFictif =
            HouseJoinPartifipantsFictifsAdapter(participantsFictifs, this)
        binding.rvParticipantsFictifs.adapter = adapterPartifipantsFictif
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvParticipantsFictifs.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation
        )
        binding.rvParticipantsFictifs.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bJoin -> {
                seeHouse()
            }
            R.id.bJoin2 -> {
                joinHouseWithSurname()
            }
        }
    }

    private var participantsFictifs = arrayListOf<Participant>()
    private lateinit var temporyHouse: House
    private var layoutSurnameIsOpen = false

    private fun seeHouse() {
        val houseCode = binding.etCode.text.toString()
        houseCodeErrors.clear()
        if (houseCode.length < 8) {
            houseCodeErrors.add("Veuillez entrer un code valide")
        }

        setHouseErrorText()
        if (houseCodeErrors.isNotEmpty()) {
            return
        }

        HouseService.getHouseWithInvitationCode(houseCode).addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result!!.isEmpty) {
                    houseCodeErrors.add("Ce code ne correspond à aucun foyer.")
                    setHouseErrorText()
                } else {
                    val house = it.result!!.first().toObject(House::class.java)
                    // Vérifie que le code n'est pas expiré
                    if (house.invitationCodeExpirationDate.before(Date())) {
                        houseCodeErrors.add("Ce code est trop ancien. Vous pouvez demander à un membre du foyer de vous en donner un nouveau.")
                        setHouseErrorText()
                    } else {
                        // Vérifie qu'on ne fait pas parti de la maison
                        HouseService.getParticipants(house.houseId)
                            .addOnSuccessListener { participants ->
                                var isInHouse = false
                                var fictifs = arrayListOf<Participant>()
                                for (participant in participants.toObjects(Participant::class.java)) {
                                    if (participant.userId == null)
                                        fictifs.add(participant)
                                    else {
                                        if (participant.userId.equals(AuthService.auth.uid)) {
                                            isInHouse = true;
                                        }
                                    }
                                }
                                if (!isInHouse) {
                                    // continue
                                    this.participantsFictifs.clear()
                                    Log.e("fictifs", "fict:" + fictifs)
                                    this.participantsFictifs.addAll(fictifs)
                                    this.temporyHouse = house
                                    this.adapterPartifipantsFictif.setHouse(house)
                                    openLayoutSurname()
                                } else {
                                    houseCodeErrors.add("Vous faites déjà parti de ce foyer.")
                                    setHouseErrorText()
                                }
                            }
                    }

                }
            } else {
                it.exception!!.printStackTrace()
                Log.e("HouseJoinFragment", "getHouseInvitationCode failed")
            }
        }

    }

    fun setHouseErrorText() {
        binding.tErrorHouseCode.visibility =
            if (houseCodeErrors.isEmpty()) View.GONE else View.VISIBLE
        var textCode = ""
        for ((index, error) in houseCodeErrors.withIndex()) {
            textCode += if (index > 1) error else "\n" + error
        }
        binding.tErrorHouseCode.text = textCode
    }


    fun openLayoutSurname() {
        binding.lSecond.visibility = View.VISIBLE
        binding.lFirst.visibility = View.GONE
        binding.lParticipants.visibility =
            if (participantsFictifs.isEmpty()) View.GONE else View.VISIBLE
        layoutSurnameIsOpen = true

        adapterPartifipantsFictif.notifyDataSetChanged()
    }

    fun joinHouseWithSurname() {
        var surname = binding.etSurname.text.toString()
        if (surname.isBlank()) {
            surname = AuthService.getCurrentUser().name
        }

        HouseService.addParticipant(
            temporyHouse.houseId,
            surname,
            AuthService.getCurrentUser().userId,
            {
                Log.e("oncomplete", "2")
                goToHouse()
            },
            {
                it.printStackTrace()
            })
    }

    fun joinHouseWithFictifParticipant() {
        //TODO
        var surname = binding.etSurname.text.toString()
        if (surname.isBlank()) {
            surname = AuthService.getCurrentUser().name
        }

        HouseService.addParticipant(
            temporyHouse.houseId,
            surname,
            AuthService.getCurrentUser().userId,
            {
                Log.e("oncomplete", "2")
                goToHouse()
            },
            {
                it.printStackTrace()
            })
    }

    fun goToHouse() {
        parentFragmentManager.beginTransaction()
            .add(R.id.container, HouseFragment(temporyHouse))
            .addToBackStack("house")
            .detach(this)
            .commit()

    }
}