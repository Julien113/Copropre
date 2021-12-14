package com.copropre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import com.copropre.common.models.User
import com.copropre.common.services.common.TopBarService
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.UserService
import com.copropre.databinding.ActivityMainBinding
import com.copropre.main.house.list.HouseListFragment
import com.copropre.main.login.LogInFragment
import com.copropre.main.login.SignInFragment
import com.copropre.main.parameters.ProfileFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkVersion()
        initTopBar()

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val firebaseCurrentUser = AuthService.getAuth().currentUser
        if (firebaseCurrentUser !== null) {
            UserService.getUser(firebaseCurrentUser.uid).addOnCompleteListener {
                if (it.isSuccessful) {
                    AuthService.setCurrentUser(it.result!!.toObject(User::class.java))
                } else {
                    it.exception!!.printStackTrace()
                }
            }
        }
        setFragmentMain()
    }

    fun setFragmentMain() {
        val fragment = HouseListFragment();
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()

        /*
        val fragment = MainFragment();
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()*/
    }

    fun initTopBar() {
        TopBarService.topBar = binding.topAppBar
        TopBarService.initTopBar(supportFragmentManager)
        return

    }



    //Version https://developer.android.com/guide/playcore/in-app-updates/kotlin-java#kotlin
    private fun checkVersion() {
        /*val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
            }
        }*/

        /*VersionService.getAppVersion(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(Int::class.java)?.compareTo(VersionService.actualMajorVersion)!! > 0) {
                    Log.e("maj","une nouvelle maj est dispo" + dataSnapshot.getValue(Int::class.java))
                } else {
                    Log.e("eeuh","pas de nouvelle maj")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })*/


    }
}