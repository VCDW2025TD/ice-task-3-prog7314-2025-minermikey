package com.example.memestream

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    // getting the google and firebase client
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Check if biometrics should auto-trigger
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        // this speficially checkes is user accepted the biometrics
        val bioEnabled = prefs.getBoolean("biometric_enabled", false)

// an if statement to check is its avaliable and enabled, if those are true then itll display the main activity
        if (bioEnabled && isBiometricAvailable()) {
            showBiometricPrompt(
                onSuccess = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onFailure = {
                    Log.d("BiometricAuth", "Biometric auto login failed")
                }
            )
        }

        // this next part is used for signing in to firebase

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

// this is all for the google button to ensure that it works
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // this is the biometric button that will allow for the biometrics to pop up
        val bioButton = findViewById<Button>(R.id.BioMetricsBtn)
        bioButton.setOnClickListener {
            if (isBiometricAvailable()) {
                showBiometricPrompt(
                    onSuccess = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onFailure = { Log.d("BiometricAuth", "Biometric login failed.") }
                )
            } else {
                Log.d("BiometricAuth", "Biometric not available or not enrolled.")
            }
        }
    }


    // --- Firebase Google sign in ---
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("LoginActivity", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    getSharedPreferences("app_prefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("biometric_enabled", true)
                        .apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("LoginActivity", "Firebase auth failed")
                }
            }
    }

    // --- Biometric utils ---
    private fun isBiometricAvailable(): Boolean {
        // Get an instance of BiometricManager to check the device's biometric capabilities
        val biometricManager = BiometricManager.from(this)

        // Returns true if the device supports strong biometrics (e.g., fingerprint, face, iris)
        // and is ready to use, otherwise returns false
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }


    // --- Show biometric prompt ---
    private fun showBiometricPrompt(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        // Create an executor to run the biometric callbacks on the main (UI) thread
        val executor = ContextCompat.getMainExecutor(this)

        // Create a BiometricPrompt instance, which handles showing the biometric dialog
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                // Called when authentication is successful
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                // Called when an unrecoverable error happens (e.g., user cancels, too many attempts)
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onFailure()
                }

                // Called when authentication fails (e.g., wrong fingerprint/face)
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailure()
                }
            })

        // Build the prompt dialog that will be shown to the user
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login") // Main title of the prompt
            .setSubtitle("Use your fingerprint or face to login") // Extra info shown below title
            .setNegativeButtonText("Cancel") // Text for the cancel button
            .build()

        // Finally, show the biometric prompt to the user
        biometricPrompt.authenticate(promptInfo)
    }

}
