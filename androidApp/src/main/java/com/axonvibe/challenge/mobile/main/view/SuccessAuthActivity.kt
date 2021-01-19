package com.axonvibe.challenge.mobile.main.view

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.axonvibe.challenge.mobile.R
import com.axonvibe.challenge.mobile.main.util.setSafeOnClickListener
import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.domain.model.Player
import com.axonvibe.challenge.shared.viewModel.profile.ProfileViewModel
import com.axonvibe.challenge.shared.viewModel.profile.state.LogoutProfileState
import com.axonvibe.challenge.shared.viewModel.profile.state.ProfileState
import com.axonvibe.challenge.shared.viewModel.profile.state.SuccessGetProfileState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_success_auth.*
import com.axonvibe.challenge.mobile.main.view.AlertMessageDialogFragment.Companion.MESSAGE

class SuccessAuthActivity : AppCompatActivity() {
    lateinit var profileViewModel: ProfileViewModel
    lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_auth)
        initView()
        initViewModel()
    }

    private fun initView() {
        btn_logout_success_auth_screen.setSafeOnClickListener {
            profileViewModel.logoutProfile()
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
            returnToMainActivity()
        }
        btn_scan_success_auth_screen.setSafeOnClickListener {
            val intent = Intent(this, ScanQRActivity::class.java)
            startActivity(intent)
        }
        val message = intent.getStringExtra(MESSAGE)
        message?.let {
            showMessage(it)
        }
    }

    private fun initViewModel() {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        observerViewModel()
    }

    private fun observerViewModel() {
        profileViewModel.getSavedProfile()
        profileViewModel.profileLiveData.addObserver { getProfileState(it) }
        profileViewModel.actionIfUserInactive()
        profileViewModel.sessionStatus.addObserver { actionUserChange(it) }
    }

    fun actionUserChange(idSession: String) {
        if (idSession.isNotBlank()) {
            goToWaitingSceen(idSession)
        }
    }

    fun goToWaitingSceen(sessionId: String) {
        if (sessionId.isNotBlank()) {
            val intent = Intent(this, WaitingActivity::class.java)
            intent.putExtra(SESSION_ID, sessionId)
            startActivity(intent)
            finish()
        }
    }

    private fun getProfileState(state: ProfileState) {
        when (state) {
            is SuccessGetProfileState -> {
                processGetProfileSuccess(state)
            }
            is LogoutProfileState -> {
                finish()
            }
        }
    }

    private fun returnToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }

    private fun processGetProfileSuccess(state: ProfileState) {
        val response = state.response as Response.Success
        val progressBar = pb_loading_image_success_auth_screen
        tv_player_name_success_auth_screen.text = response.data.user.displayName

        Glide.with(this)
            .load(response.data.user.image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    p0: GlideException?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    p0: Drawable?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(img_avatar_success_auth_screen)
        player = Player(response.data.user.displayName, response.data.user.image)
    }

    fun showMessage(message: String) {
        val alertDialog = AlertMessageDialogFragment.newInstance(message)
        alertDialog.isCancelable = false
        alertDialog.show(supportFragmentManager, AlertMessageDialogFragment.TAG)
    }

    companion object {
        const val SESSION_ID = "sessionID"
    }
}