package com.renandeassisalves.apptarefas.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.renandeassisalves.apptarefas.Models.Tarefa
import com.renandeassisalves.apptarefas.R
import com.renandeassisalves.apptarefas.Repository.TarefaRepository
import java.util.Calendar
import java.util.Date

class EditarTarefaActivity : AppCompatActivity() {
    private lateinit var tarefaRepository:TarefaRepository
    private lateinit var editTitulo: EditText
    private lateinit var editDescricao: EditText
    private lateinit var calendar: CalendarView
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnDeletar: CardView
    private lateinit var tarefaEncontrada : Tarefa
    private var dataHora: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_tarefa)

        val keyTarefa = intent.getStringExtra("tarefaKey")

        editTitulo = findViewById(R.id.editTitulo)
        editDescricao = findViewById(R.id.editDescricao)
        calendar = findViewById(R.id.calendario)

        tarefaRepository = TarefaRepository(this)
        tarefaRepository.encontrarTarefaPorKey(keyTarefa!!) { tarefa ->
            if (tarefa != null) {
                tarefaEncontrada = tarefa

                editTitulo.setText(tarefaEncontrada.titulo)
                editDescricao.setText(tarefaEncontrada.descricao)
                tarefaEncontrada.dataHora?.let { calendar.setDate(it.time) }
            } else {
                Toast.makeText(this, "Tarefa não encontrada no banco", Toast.LENGTH_SHORT).show()
            }
        }

        btnSalvar = findViewById(R.id.btnSalvar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnDeletar = findViewById(R.id.btnDeletar)

        btnSalvar.setOnClickListener {
            atualizarTarefa(tarefaEncontrada)
        }

        btnCancelar.setOnClickListener {
            voltarHome()
        }

        btnDeletar.setOnClickListener {
            deletarTarefa(tarefaEncontrada)
        }

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            dataHora = calendar.time
        }
    }

    private fun atualizarTarefa(tarefa:Tarefa) {
        val titulo = editTitulo.text.toString().trim()
        val descricao = editDescricao.text.toString().trim()

        if (titulo.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha o título e a descrição", Toast.LENGTH_SHORT).show()
        }

        tarefa.titulo = titulo
        tarefa.descricao = descricao
        tarefa.dataHora = dataHora

        tarefaRepository.atualizarTarefa(tarefa) {
            resultado ->
            if (resultado) {
                voltarHome()
            } else {
                Toast.makeText(this, "Erro ao atualizar tarefa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deletarTarefa(tarefa: Tarefa) {
        //tarefaRepository.deletarTarefa(tarefa)
        tarefaRepository.deletarTarefa(tarefa) {
                resultado ->
            if (resultado) {
                voltarHome()
            } else {
                Toast.makeText(this, "Erro ao deletar tarefa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun voltarHome() {
        val intent = Intent(this@EditarTarefaActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}