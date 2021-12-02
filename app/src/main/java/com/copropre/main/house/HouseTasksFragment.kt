package com.copropre.main.house

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.copropre.R
import com.copropre.common.models.House
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentHouseTasksBinding
import com.copropre.databinding.FragmentNewHouseBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener

class HouseTasksFragment(private val house: House) : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseTasksBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding.bCreate.setOnClickListener(this)
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