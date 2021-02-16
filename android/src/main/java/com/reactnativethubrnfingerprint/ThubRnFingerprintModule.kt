package com.reactnativethubrnfingerprint

import android.os.Handler
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.*
import java.util.concurrent.Executor

class ThubRnFingerprintModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  private var fingerPrintSuccessCallback: Callback? = null
  private var fingerPrintFailureCallback: Callback? = null
  private var executor: Executor? = null
  private var biometricPrompt: BiometricPrompt? = null
  private var promptInfo: BiometricPrompt.PromptInfo? = null

  override fun getName(): String {
    return "ThubRnFingerprint"
  }

  @ReactMethod
  fun authenticateLocally(successCallback: Callback, failureCallback: Callback) {
    fingerPrintSuccessCallback = successCallback
    fingerPrintFailureCallback = failureCallback
    canAuthenticate()
  }

  private fun canAuthenticate() {
    val handler = Handler(reactApplicationContext.mainLooper);
    handler.post {
      val bioMetricManager = BiometricManager.from(reactApplicationContext)
      when (bioMetricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
          login()
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
          fingerPrintFailureCallback?.invoke("No biometric features available on this device.")
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
          fingerPrintFailureCallback?.invoke("Biometric features are currently unavailable.")
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
          fingerPrintFailureCallback?.invoke("The user hasn't associated any biometric credentials with their account.")
        }
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
          fingerPrintFailureCallback?.invoke("Biometric error,security update required.")
        }
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
          fingerPrintFailureCallback?.invoke("Biometric error not supported.")
        }
        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
          fingerPrintFailureCallback?.invoke("Biometric error status is not known.")
        }
      }
    }

  }

  private fun login() {
    executor = ContextCompat.getMainExecutor(currentActivity)

    promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for leaao")
      .setSubtitle("Log in using biometric credentials").setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL).build()

    biometricPrompt = BiometricPrompt(currentActivity as FragmentActivity,
      executor!!, object : BiometricPrompt.AuthenticationCallback() {
      override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        fingerPrintFailureCallback?.invoke("Authentication error: $errString")
      }

      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        fingerPrintSuccessCallback?.invoke("Success")
      }

      override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        fingerPrintFailureCallback?.invoke("Authentication failed: ")
      }
    })

    biometricPrompt?.authenticate(promptInfo!!)
  }


}



