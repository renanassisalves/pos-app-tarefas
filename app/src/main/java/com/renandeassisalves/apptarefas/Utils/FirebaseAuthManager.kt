package com.renandeassisalves.apptarefas.Utils

import android.content.Context
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

    fun sair(listener: AuthListener) {
        firebaseAuth.signOut()
        listener.onSignOutSuccess()
    }
}
