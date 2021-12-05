package com.copropre.main.parameters

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.copropre.R
import com.copropre.common.services.main.AuthService
import com.copropre.common.utils.Utils
import com.copropre.databinding.FragmentLoginBinding
import com.copropre.databinding.FragmentProfileBinding
import com.copropre.main.login.LogInFragment

class ProfileFragment: Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tUserName.text = AuthService.getCurrentUser().name
        binding.tUserMail.text = AuthService.getCurrentUser().mail
        binding.bLogOut.setOnClickListener(this)
        binding.bUserName.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bLogOut -> {
                AuthService.getAuth().signOut()
                AuthService.setCurrentUser(null)
                val fm: FragmentManager = parentFragmentManager
                for (i in 0 until fm.getBackStackEntryCount()) {
                    fm.popBackStack()
                }
                val fragmentLogin = LogInFragment()
                fm.beginTransaction()
                    .replace(R.id.container, fragmentLogin)
                    .commitAllowingStateLoss()
            }
            R.id.bUserName -> {//A rajouter

            }
        }
    }


    private fun goBackFragmentMain() {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .commitAllowingStateLoss()
    }
}