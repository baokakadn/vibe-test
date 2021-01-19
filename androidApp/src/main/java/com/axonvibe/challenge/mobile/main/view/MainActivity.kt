package com.axonvibe.challenge.mobile.main.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.axonvibe.challenge.mobile.R
import com.axonvibe.challenge.mobile.main.util.setSafeOnClickListener
import com.axonvibe.challenge.mobile.main.view.AlertMessageDialogFragment.Companion.MESSAGE
import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.viewModel.profile.ProfileViewModel
import com.axonvibe.challenge.shared.viewModel.profile.state.ProfileState
import com.axonvibe.challenge.shared.viewModel.profile.state.SuccessGetProfileState
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {
    lateinit var profileViewModel: ProfileViewModel
    var activitiesLaunched: AtomicInteger = AtomicInteger(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (activitiesLaunched.incrementAndGet() > 1) { finish() }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configView()
        configObserver()
    }

    private fun configObserver() {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        observerViewModel()
    }

    private fun observerViewModel() {
        profileViewModel.getSavedProfile()
        profileViewModel.profileLiveData.addObserver { getProfileState(it) }
    }

    private fun getProfileState(state: ProfileState) {
        when (state) {
            is SuccessGetProfileState -> {
                val response = state.response as Response.Success
                onGetProfileSuccess(response.data)
            }
        }
    }

    private fun onGetProfileSuccess(profile: Info) {
        val intent = Intent(this@MainActivity, SuccessAuthActivity::class.java).apply {
            putExtra(TOKEN_KEY, profile.user.accessToken)
            putExtra(AVATAR_KEY, profile.user.image)
            putExtra(NAME_KEY, profile.user.displayName)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }

    private fun configView() {
        btn_login_main_screen.setSafeOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val message = intent.getStringExtra(MESSAGE)
        message?.let {
            showMessage(it)
        }
    }

    fun showMessage(message: String) {
        val alertDialog = AlertMessageDialogFragment.newInstance(message)
        alertDialog.isCancelable = false
        alertDialog.show(supportFragmentManager, AlertMessageDialogFragment.TAG)
    }

    override fun onDestroy() {
        activitiesLaunched.getAndDecrement()
        super.onDestroy()
    }

    companion object {
        const val CODE_KEY = "code="
        const val DENY_KEY = "deny="
        const val TOKEN_KEY = "token"
        const val AVATAR_KEY = "avatar"
        const val NAME_KEY = "name"
    }
}