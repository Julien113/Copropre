package com.copropre.main.login

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.copropre.R
import com.copropre.common.services.main.AuthService
import com.copropre.databinding.FragmentLoginBinding

class LogInFragment: Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bLogIn.setOnClickListener(this)
        binding.bSignIn.setOnClickListener(this)
        binding.bGoogle.setOnClickListener(this)
        binding.bFacebook.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bLogIn -> {
                loginWithEmail()

            }
            R.id.bSignIn -> {
                // Go to sign in
                val fragmentSignIn = SignInFragment()
                parentFragmentManager.beginTransaction()
                    .addToBackStack("SignIn")
                    .replace(R.id.container, fragmentSignIn)
                    .commitAllowingStateLoss()

            }
            R.id.bFacebook -> {

            }
            R.id.bGoogle -> {

            }
        }
    }

    fun loginWithEmail() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        AuthService.getAuth().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginUser", "signInWithEmail:success")
                    //val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginUser", "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }

    }

    fun goBackFragmentMain() {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .commitAllowingStateLoss()
    }
}