package com.renandeassisalves.apptarefas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseUser
import com.renandeassisalves.apptarefas.R
import com.renandeassisalves.apptarefas.Utils.FirebaseAuthManager


class LoginActivity : AppCompatActivity() {
    lateinit var gso:GoogleSignInOptions
    lateinit var gsc:GoogleSignInClient
    lateinit var inputEmail: EditText
    lateinit var inputSenha: EditText
    lateinit var btnLogarGoogle: SignInButton
    lateinit var btnLogin: MaterialButton
    lateinit var btnCadastrar: MaterialButton
    private lateinit var loginManager: FirebaseAuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputEmail = findViewById<EditText>(R.id.inputEmail)
        inputSenha = findViewById<EditText>(R.id.inputSenha)
        btnLogarGoogle = findViewById<SignInButton>(R.id.btnEntrarGoogle)
        btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        btnCadastrar = findViewById<MaterialButton>(R.id.btnCadastrar)
        loginManager = FirebaseAuthManager(this)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso);

        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val senha = inputSenha.text.toString()
            loginManager.entrarComEmailSenha(email, senha,
                object : FirebaseAuthManager.AuthListener {
                    override fun onSignInSuccess(user: FirebaseUser) {
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onSignInFailure(error: String) {
                        Toast.makeText(this@LoginActivity, "Erro ao entrar: "+error.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onSignUpSuccess(user: FirebaseUser) {}

                    override fun onSignUpFailure(error: String) {}

                    override fun onSignOutSuccess() {}

                    override fun onSignOutFailure(error: String) {}
                })
        }

        btnCadastrar.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CadastrarActivity::class.java))
        }

        btnLogarGoogle.setOnClickListener {
            entrar()
        }
    }

    fun entrar() {
        val entrarIntent: Intent = gsc.signInIntent
        startActivityForResult(entrarIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                val intent: Intent = Intent(
                    this@LoginActivity,
                    HomeActivity::class.java
                )
                startActivity(intent)

            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Algo deu errado", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}