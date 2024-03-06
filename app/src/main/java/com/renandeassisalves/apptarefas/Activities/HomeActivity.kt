package com.renandeassisalves.apptarefas.Activities
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.renandeassisalves.apptarefas.Adapters.TarefaAdapter
import com.renandeassisalves.apptarefas.Models.Tarefa
import com.renandeassisalves.apptarefas.R
import com.renandeassisalves.apptarefas.Repository.TarefaRepository
import com.renandeassisalves.apptarefas.Utils.FirebaseAuthManager
import java.util.Date

class HomeActivity : AppCompatActivity() {
    var nomeUsuario:android.widget.TextView? = null
    var btnAdd: FloatingActionButton? = null
    var btnSair: ImageButton? = null
    var recyclerTarefas: RecyclerView? = null
    var switchNoturno: SwitchCompat? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var tarefaRepository: TarefaRepository
    private lateinit var tarefaAdapter: TarefaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iniciarLogin()

        tarefaRepository = TarefaRepository(this)
        tarefaAdapter = TarefaAdapter(mutableListOf())

        recyclerTarefas = findViewById<RecyclerView>(R.id.recyclerTarefas)
        recyclerTarefas?.adapter = tarefaAdapter
        recyclerTarefas?.layoutManager = LinearLayoutManager(this)

        carregarTarefas()

        btnAdd = findViewById<FloatingActionButton>(R.id.btnAdd)
        btnAdd?.setOnClickListener {
            val intent = Intent(this@HomeActivity, CriarTarefaActivity::class.java)
            startActivity(intent)
        }

        switchNoturno = findViewById<SwitchCompat>(R.id.switchNoturno)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchNoturno?.setChecked(false)
        } else {
            switchNoturno?.setChecked(true)
        }
        switchNoturno?.setOnClickListener {
            definirModoNoturno()
        }
    }

    private fun carregarTarefas() {
        tarefaAdapter.atualizarLista(tarefaRepository.lerTarefasLocais())
        tarefaRepository.lerTarefas { tarefas ->
            tarefaAdapter.atualizarLista(tarefas)
        }
    }

    fun iniciarLogin() {
        nomeUsuario = findViewById<TextView>(R.id.nomeUsuario)
        btnSair = findViewById<ImageButton>(R.id.btnSair)

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
            FirebaseAuthManager.deslogar(this)
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun definirModoNoturno() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        recreate()
    }
}