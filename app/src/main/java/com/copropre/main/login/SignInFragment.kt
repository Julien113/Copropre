package com.copropre.main.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.copropre.R
import com.copropre.common.models.User
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.UserService
import com.copropre.common.utils.Utils
import com.copropre.databinding.FragmentSigninBinding

class SignInFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!


    private val emailErrors = ArrayList<String>()
    private val passErrors = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bSignIn.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bSignIn -> {
                signInWithEmail()
            }
        }
    }

    fun signInWithEmail() {
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
            passErrors.add("Le mot de passe doit contenir au moins 8 caractères")
        }


        setEmailErrorAndPasswordErrorText()
        if (passErrors.isNotEmpty() || emailErrors.isNotEmpty()) {
            return
        }

        AuthService.getAuth().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("createUser", "createUserWithEmail:success "+task.result)
                    val user = AuthService.getAuth().currentUser
                    val name = if (binding.etName.text.isNotEmpty()){
                        binding.etName.text.toString()
                    } else {
                        if (user?.displayName !== null) {
                            user.displayName!!
                        } else {
                            user?.email!!.split("@")[0]
                        }
                    }

                    createAccount(name, user?.email!!, user.uid)
                    goBackFragmentMain()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("createUser", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    emailErrors.add(task.exception!!.localizedMessage)
                    setEmailErrorAndPasswordErrorText()
                    //updateUI(null)
                }
            }
    }

    fun setEmailErrorAndPasswordErrorText() {
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
        parentFragmentManager.beginTransaction()
            .detach(this)
            .commitAllowingStateLoss()
    }

    fun createAccount(name: String?, email: String, uid: String) {
        val user = User(email, uid, name)
        UserService.createUser(user);
    }

}