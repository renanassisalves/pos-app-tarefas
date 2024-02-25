package com.renandeassisalves.apptarefas.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.renandeassisalves.apptarefas.Models.Tarefa
import com.renandeassisalves.apptarefas.R

class AdapterAno(private val listaTarefas: List<Tarefa>):
    RecyclerView.Adapter<AdapterAno.ViewHolder>() {
    var listaAtual: List<Tarefa> = listaTarefas

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
        holder.textViewTitulo.text = this.listaAtual[position].getTitulo()
        holder.textViewDescricao.text = this.listaAtual[position].getDescricao()
        holder.textViewData.text = this.listaAtual[position].getDataHora().toString()
        holder.itemView.setOnClickListener{
//            val intent = Intent(holder.itemView.context, VeiculoActivity::class.java)
//            Auxiliar.setCodigoAno(this.listaAtual[position].retornarCodigo())
//            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaAtual.size
    }
}