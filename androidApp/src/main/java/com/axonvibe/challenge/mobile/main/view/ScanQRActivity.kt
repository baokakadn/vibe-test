package com.axonvibe.challenge.mobile.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.axonvibe.challenge.mobile.R
import com.axonvibe.challenge.mobile.main.view.AlertMessageDialogFragment.Companion.MESSAGE
import com.axonvibe.challenge.shared.data.enum.PlayerStatus
import com.axonvibe.challenge.shared.viewModel.playerInSession.PlayerInSessionViewModel
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

const val REQUEST_ID_MULTIPLE_PERMISSIONS = 7

class ScanQRActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    lateinit var playerInSession: PlayerInSessionViewModel
    private lateinit var mScannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndroidVersion()
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
        initViewModel()
    }

    private fun initViewModel() {
        playerInSession = ViewModelProviders.of(this).get(PlayerInSessionViewModel::class.java)
        observerViewModel()
    }

    private fun observerViewModel() {
        playerInSession.actionStatus.addObserver { getActionSessionID(it) }
    }

    private fun getActionSessionID(sessionID: String) {
        if (sessionID == PlayerStatus.HAVE_NOT_JOINED.status) return
        if (sessionID.isNotBlank() && sessionID != PlayerStatus.PLAYER_ALREADY_IN_SESSION.status) {
            if (sessionID == PlayerStatus.JOINED_INVALID_SESSION.status) {
                returnToSuccessActivity(PlayerStatus.JOINED_INVALID_SESSION.status)
            } else {
                val intent = Intent(this, WaitingActivity::class.java)
                intent.putExtra(SuccessAuthActivity.SESSION_ID, sessionID)
                startActivity(intent)
                finish()
            }
        } else if (sessionID == PlayerStatus.PLAYER_ALREADY_IN_SESSION.status) {
            returnToSuccessActivity(PlayerStatus.PLAYER_ALREADY_IN_SESSION.status)
        }
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    private fun processCode(resultContent: String) {
        playerInSession.playerJoinSession(
            resultContent
        )
    }

    override fun handleResult(result: Result?) {
        if (result != null) {
            processCode(result.text)
        }
    }

    private fun checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camera: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    if (perms[Manifest.permission.CAMERA] != PackageManager.PERMISSION_GRANTED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            )
                        ) {
                            returnToSuccessActivity(getString(R.string.scan_screen_clear_memories_message))
                        }
                    }
                }
            }
        }
    }

    private fun returnToSuccessActivity(message: String) {
        val intent = Intent(this, SuccessAuthActivity::class.java)
            .apply {
                putExtra(MESSAGE, message)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        startActivity(intent)
        finish()
    }
}