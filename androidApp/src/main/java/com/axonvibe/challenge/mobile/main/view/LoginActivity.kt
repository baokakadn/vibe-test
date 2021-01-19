package com.axonvibe.challenge.mobile.main.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.axonvibe.challenge.mobile.R
import com.axonvibe.challenge.mobile.main.util.getCodeFromUrl
import com.axonvibe.challenge.mobile.main.view.AlertMessageDialogFragment.Companion.MESSAGE
import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.configutation.AXON_ID
import com.axonvibe.challenge.shared.configutation.LOGIN_URL
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.viewModel.profile.ProfileViewModel
import com.axonvibe.challenge.shared.viewModel.profile.state.ProfileState
import com.axonvibe.challenge.shared.viewModel.profile.state.SuccessGetProfileState
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    lateinit var profileViewModel: ProfileViewModel
    var isLoginWebView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configView()
        initViewModel()
    }

    private fun initViewModel() {
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
        val intent = Intent(this@LoginActivity, SuccessAuthActivity::class.java).apply {
            putExtra(MainActivity.TOKEN_KEY, profile.user.accessToken)
            putExtra(MainActivity.AVATAR_KEY, profile.user.image)
            putExtra(MainActivity.NAME_KEY, profile.user.displayName)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }

    private fun configView() {
        webView = login_webview
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            var isCancelAction = false

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    if (url.contains(MainActivity.CODE_KEY) && !isLoginWebView) {
                        val code = url.getCodeFromUrl(MainActivity.CODE_KEY)
                        isLoginWebView = true
                        profileViewModel.getProfile(code)
                    } else if ((!url.contains(AXON_ID) || url.contains(MainActivity.DENY_KEY) && !isLoginWebView && !isCancelAction)) {
                        isCancelAction = true
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            .apply {
                                putExtra(MESSAGE, getString(R.string.login_failed))
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        startActivity(intent)
                        CookieManager.getInstance().removeAllCookies(null)
                        CookieManager.getInstance().flush()
                        finish()
                    }
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                activity_login_progressBar.visibility = View.GONE
            }

        }
        webView.loadUrl(LOGIN_URL)
    }

    override fun onDestroy() {
        Log.i("AAAAAAAAAAAAAAAA", "Login destroy")
        super.onDestroy()
    }
}
