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
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentNewHouseBinding
import com.google.android.gms.tasks.OnFailureListener

class NewHouseFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentNewHouseBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewHouseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_NEW_HOUSE)
        binding.bCreate.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bCreate -> {
                createHouse()
            }
        }
    }

    private fun createHouse() {
        // get surname
        var surname = binding.etSurname.text.toString()
        if (surname.isBlank()) {
            surname = AuthService.getCurrentUser().name
        }
        // Check name is ok
        binding.tErrorName.visibility = View.GONE
        if (binding.etName.text.toString().isEmpty()) {
            binding.tErrorName.text = "Le nom du foyer est obligatoire"
            binding.tErrorName.visibility = View.VISIBLE
            return
        }

        // Create house
        val house = House(binding.etName.text.toString(), binding.etDescription.text.toString())
        HouseService.createHouse(house, surname, {
            if (it.isSuccessful) {
                goToNewHouseFragment(house);
            } else {
                Log.e("HOUSE", "Fail")

            }
        }, OnFailureListener {
            Log.e("HOUSEERROR", it.stackTrace.toString())
            it.printStackTrace()
        })

    }

    fun goToNewHouseFragment(house: House) {
        parentFragmentManager.beginTransaction()
            .add(R.id.container, HouseFragment(house))
            .addToBackStack("house")
            .detach(this)
            .commitAllowingStateLoss()
    }
}