package com.copropre.main.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.copropre.R
import com.copropre.common.services.common.TopBarService
import com.copropre.common.models.User
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.UserService
import com.copropre.common.utils.Utils
import com.copropre.databinding.FragmentLoginBinding
import com.copropre.main.house.list.HouseListFragment

class LogInFragment: Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val emailErrors = ArrayList<String>()
    private val passErrors = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_LOGIN)
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
                val fragmentSignIn = SignInFragment(this)
                parentFragmentManager.beginTransaction()
                    .addToBackStack("SignIn")
                    .add(R.id.container, fragmentSignIn)
                    .commitAllowingStateLoss()

            }
            R.id.bFacebook -> {

            }
            R.id.bGoogle -> {

            }
        }
    }

    private fun loginWithEmail() {
        // check email
        val email = binding.etEmail.text.toString()
        emailErrors.clear()
        passErrors.clear()
        // check que c'est bien un email
        if (!Utils.isValidEmail(email)) {
            emailErrors.add("Email invalide")
        }
        // check password
        val password = binding.etPassword.text.toString()
        if (!Utils.isValidPassword(password)) {
            passErrors.add("Veuillez renseigner un mot de passe")
        }

        setEmailErrorAndPasswordErrorText()
        if (passErrors.isNotEmpty() || emailErrors.isNotEmpty()) {
            return
        }

        AuthService.getAuth().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginUser", "signInWithEmail:success")
                    //val user = auth.currentUser
                    //updateUI(user)
                    UserService.getUser(AuthService.getAuth().uid).addOnCompleteListener {
                        if (it.isSuccessful) {
                            AuthService.setCurrentUser(it.result!!.toObject(User::class.java))
                            goBackFragmentMain()
                        } else {
                            it.exception!!.printStackTrace()
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginUser", "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }

    }

    private fun setEmailErrorAndPasswordErrorText() {
        binding.tErrorEmail.visibility = if (emailErrors.isEmpty()) View.GONE else View.VISIBLE
        binding.tErrorPassword.visibility = if (passErrors.isEmpty()) View.GONE else View.VISIBLE
        var textEmail = ""
        for ((index,error) in emailErrors.withIndex()){
            textEmail += if (index > 1) error else "\n"+error
        }
        binding.tErrorEmail.text = textEmail
        var textPassword = ""
        for ((index,error) in passErrors.withIndex()){
            textPassword += if (index > 1) error else "\n"+error
        }
        binding.tErrorPassword.text = textPassword
    }


    fun goBackFragmentMain() {
        // ferme le keyboard
        view?.clearFocus()
        val fragment = HouseListFragment();
        parentFragmentManager.beginTransaction()
            .detach(this)
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }
}