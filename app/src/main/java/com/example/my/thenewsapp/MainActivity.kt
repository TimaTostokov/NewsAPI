package com.example.my.thenewsapp

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.my.thenewsapp.ui.FragmentArticle
//ОТПЕЧАТОК ПАЛЬЦА!!!!!!!!
class MainActivity : AppCompatActivity() {

    private var cancellationSignal: CancellationSignal? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBiometricSupport()

        findViewById<ImageView>(R.id.flFragment).setOnClickListener {
            val biometricPrompt = createBiometricPrompt()
            biometricPrompt.authenticate(
                getCancellationSignal(),
                mainExecutor,
                authenticationCallback
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createBiometricPrompt(): BiometricPrompt {
        return BiometricPrompt.Builder(this)
            .setTitle("App Money")
            .setSubtitle("Проверка")
            .setDescription("Приложите свой отпечаток пальца")
            .setNegativeButton("Отмена", mainExecutor) { _, _ ->
                notifyUser("Отменена")
            }
            .build()
    }

    private val authenticationCallback = @RequiresApi(Build.VERSION_CODES.P)
    object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
            notifyUser("ОШИБКА!: $errString")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            notifyUser("УСПЕШНО!")
            startNewActivity()
        }
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Отменена пользователем")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Отпечаток пальца не включен в настройках безопасности устройства")
            return
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Разрешение на использование биометрической аутентификации отпечатка пальца не предоставлено")
            return
        }
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            notifyUser("Устройство не поддерживает отпечаток пальца")
        }
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startNewActivity() {
        val intent = Intent(this, FragmentArticle::class.java)
        startActivity(intent)
    }

}
