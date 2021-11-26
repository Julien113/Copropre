package com.copropre.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.copropre.R
import com.copropre.common.services.main.AuthService
import com.copropre.databinding.FragmentMainBinding
import com.copropre.main.login.LogInFragment

class MainFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bLogOut.setOnClickListener(this)
        binding.bLogIn.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bLogOut -> {
                AuthService.getAuth().signOut()
                val fragmentLogin = LogInFragment();
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, fragmentLogin)
                    .commitAllowingStateLoss()

            }
            R.id.bLogIn -> {
                val fragmentLogin = LogInFragment();
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, fragmentLogin)
                    .commitAllowingStateLoss()

            }
        }
    }
}