package com.copropre.main.parameters

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.AbsSavedState
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.copropre.R
import com.copropre.common.services.common.TopBarService
import com.copropre.databinding.FragmentHelpBinding



class HelpFragment: Fragment() {
    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar((TopBarService.FragmentName.FRAGMENT_HOUSE)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    private fun goBackFragmentMain() {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .commitAllowingStateLoss()
    }
}
