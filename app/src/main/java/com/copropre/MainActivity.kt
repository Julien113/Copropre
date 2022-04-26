package com.copropre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.copropre.common.models.User
import com.copropre.common.services.DataHolder
import com.copropre.common.services.common.TopBarService
import com.copropre.common.services.main.AuthService
import com.copropre.common.services.main.UserService
import com.copropre.databinding.ActivityMainBinding
import com.copropre.main.house.list.HouseListFragment
import com.copropre.main.login.LogInFragment
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var UPDATE_INTENT_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Check for update
        checkUpdate()

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
                    setFragmentMain()
                } else {
                    it.exception!!.printStackTrace()
                }
            }
        } else {
            setFragmentLogin()

        }
    }

    fun checkUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.updatePriority() >= 4 /* high priority */
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    UPDATE_INTENT_CODE)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_INTENT_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
                checkUpdate()
            }
        }
    }

    fun setFragmentMain() {
        val fragment = HouseListFragment();
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
        // Hide loading screen
        binding.loadingScreen.loadingScreen.visibility = View.GONE

    }

    fun setFragmentLogin() {
        val fragment = LogInFragment();
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
        // Hide loading screen
        binding.loadingScreen.loadingScreen.visibility = View.GONE
    }

    fun initTopBar() {
        TopBarService.topBar = binding.topAppBar
        TopBarService.initTopBar(supportFragmentManager)
        binding.appBarLayout.visibility = View.VISIBLE
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