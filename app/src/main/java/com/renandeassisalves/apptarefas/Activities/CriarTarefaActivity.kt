package com.renandeassisalves.apptarefas.Activities;
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.renandeassisalves.apptarefas.Models.Tarefa
import com.renandeassisalves.apptarefas.R
import com.renandeassisalves.apptarefas.Repository.TarefaRepository
import java.util.*

class CriarTarefaActivity : AppCompatActivity() {

    private lateinit var editTitulo: EditText
    private lateinit var editDescricao: EditText
    private lateinit var calendario: CalendarView
    private lateinit var btnCadastrar: Button
    private lateinit var btnCancelar: Button
    private lateinit var tarefaRepository: TarefaRepository
    private var dataHora: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_tarefa)

        editTitulo = findViewById(R.id.editTitulo)
        editDescricao = findViewById(R.id.editDescricao)
        calendario = findViewById(R.id.calendario)
        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnCancelar = findViewById(R.id.btnCancelar)

        tarefaRepository = TarefaRepository(this)

        btnCadastrar.setOnClickListener {
            cadastrarTarefa()
        }

        btnCancelar.setOnClickListener {
            val intent = Intent(this@CriarTarefaActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        calendario.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            dataHora = calendar.time
        }
    }

    private fun cadastrarTarefa() {
        val titulo = editTitulo.text.toString().trim()
        val descricao = editDescricao.text.toString().trim()

        if (titulo.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha o título e a descrição", Toast.LENGTH_SHORT).show()
        }

        val tarefa = Tarefa(
            userId = null,
            titulo = titulo,
            descricao = descricao,
            dataHora = dataHora
        )

        tarefaRepository.cadastrarTarefa(tarefa)

        val intent = Intent(this@CriarTarefaActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
