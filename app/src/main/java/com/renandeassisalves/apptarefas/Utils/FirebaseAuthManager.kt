package com.renandeassisalves.apptarefas.Utils

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthManager(private val context: Context) {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    interface AuthListener {
        fun onSignInSuccess(user: FirebaseUser)
        fun onSignInFailure(error: String)
        fun onSignUpSuccess(user: FirebaseUser)
        fun onSignUpFailure(error: String)
        fun onSignOutSuccess()
        fun onSignOutFailure(error: String)
    }

    fun entrarComEmailSenha(email: String, senha: String, listener: AuthListener) {
        firebaseAuth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                listener.onSignInSuccess(user!!)
            }
            .addOnFailureListener { e ->
                listener.onSignInFailure(e.message ?: "Erro desconhecido ocorreu")
            }
    }

    fun cadastrarComEmailSenha(email: String, senha: String, listener: AuthListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                listener.onSignUpSuccess(user!!)
            }
            .addOnFailureListener { e ->
                listener.onSignUpFailure(e.message ?: "Erro desconhecido ocorreu")
            }
    }

    companion object {
        fun deslogar(context: Context) {
            var sharedPreferences : SharedPreferences = context.getSharedPreferences("authTokens", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("idToken").apply()

            if (GoogleSignIn.getLastSignedInAccount(context) != null) {
                val gso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
                val gsc = GoogleSignIn.getClient(context, gso!!)
                gsc.signOut()
            } else {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()
            }
        }
    }
}
