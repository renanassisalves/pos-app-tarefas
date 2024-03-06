package com.renandeassisalves.apptarefas.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.renandeassisalves.apptarefas.Activities.EditarTarefaActivity
import com.renandeassisalves.apptarefas.Models.Tarefa
import com.renandeassisalves.apptarefas.R
import java.text.SimpleDateFormat
import java.util.Locale

public class TarefaAdapter(private val listaTarefas: MutableList<Tarefa>):
    RecyclerView.Adapter<TarefaAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitulo: TextView
        val textViewDescricao: TextView
        val textViewData: TextView
        init {
            textViewTitulo = view.findViewById(R.id.textViewTitulo)
            textViewDescricao = view.findViewById(R.id.textViewDescricao)
            textViewData = view.findViewById(R.id.textViewData)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_tarefa, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tarefa = listaTarefas[position];
        holder.textViewTitulo.text = tarefa.titulo
        holder.textViewDescricao.text = tarefa.descricao
        val formatarDataHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
        val dataHoraFormatada = formatarDataHora.format(tarefa.dataHora)
        holder.textViewData.text = dataHoraFormatada
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, EditarTarefaActivity::class.java)
            intent.putExtra("tarefaKey", tarefa.key)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun atualizarLista(novaLista: List<Tarefa>) {
        listaTarefas.clear()
        listaTarefas.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listaTarefas.size
    }
}