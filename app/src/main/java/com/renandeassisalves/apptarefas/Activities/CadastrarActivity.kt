package com.renandeassisalves.apptarefas.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseUser
import com.renandeassisalves.apptarefas.R
import com.renandeassisalves.apptarefas.Utils.FirebaseAuthManager

class CadastrarActivity : AppCompatActivity() {
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var inputEmail: EditText
    lateinit var inputSenha: EditText
    lateinit var btnCadastrar: MaterialButton
    lateinit var btnCancelar: MaterialButton
    lateinit var txtErro: TextView
    private lateinit var loginManager: FirebaseAuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputEmail = findViewById<EditText>(R.id.inputEmail)
        inputSenha = findViewById<EditText>(R.id.inputSenha)
        btnCadastrar = findViewById<MaterialButton>(R.id.btnCadastrar)
        btnCancelar = findViewById<MaterialButton>(R.id.btnCancelar)
        txtErro = findViewById<TextView>(R.id.txtErro)
        loginManager = FirebaseAuthManager(this)

        btnCadastrar.setOnClickListener {
            val email = inputEmail.text.toString()
            val senha = inputSenha.text.toString()
            if (senha.length < 6) {
                txtErro.text = "A senha deve conter mais que 6 caracteres"
                return@setOnClickListener
            }
            loginManager.cadastrarComEmailSenha(email, senha,
                object : FirebaseAuthManager.AuthListener {
                    override fun onSignInSuccess(user: FirebaseUser) {}

                    override fun onSignInFailure(error: String) {}

                    override fun onSignUpSuccess(user: FirebaseUser) {
                        logar(email, senha)
                    }

                    override fun onSignUpFailure(error: String) {
                        txtErro.text = error.toString()
                    }

                    override fun onSignOutSuccess() {}

                    override fun onSignOutFailure(error: String) {}
                })
        }
    }

    fun logar(email: String, senha: String) {
        loginManager.entrarComEmailSenha(email, senha,
            object : FirebaseAuthManager.AuthListener {
                override fun onSignInSuccess(user: FirebaseUser) {
                    val intent = Intent(this@CadastrarActivity, HomeActivity::class.java)
                    startActivity(intent)
                }

                override fun onSignInFailure(error: String) {
                    txtErro.text = error.toString()
                }

                override fun onSignUpSuccess(user: FirebaseUser) {}

                override fun onSignUpFailure(error: String) {}

                override fun onSignOutSuccess() {}

                override fun onSignOutFailure(error: String) {}
            })
    }
}