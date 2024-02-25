package com.renandeassisalves.apptarefas.Activities

import android.R.attr.name
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.renandeassisalves.apptarefas.R


class HomeActivity : AppCompatActivity() {
    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null
    var nomeUsuario:android.widget.TextView? = null
    var btnSair: ImageView? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nomeUsuario = findViewById<TextView>(R.id.nomeUsuario)
        btnSair = findViewById<ImageView>(R.id.btnSair)

        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso!!)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            nomeUsuario?.setText(acct.displayName)
        }

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val splitEmail = currentUser.email?.split("@")?.get(0)
            nomeUsuario?.text = splitEmail
        } else {
            nomeUsuario?.text = "ERRO"
        }


        btnSair?.setOnClickListener {
            if (GoogleSignIn.getLastSignedInAccount(this) != null) {
                gsc?.signOut()
            }
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}