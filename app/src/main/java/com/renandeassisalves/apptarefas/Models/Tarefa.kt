package com.renandeassisalves.apptarefas.Models

import java.util.Date

class Tarefa
    (
    private val titulo: String,
    private val descricao: String,
    private val dataHora: Date,
) {
    fun getTitulo(): String {
        return this.titulo;
    }

    fun getDescricao(): String {
        return this.descricao;
    }

    fun getDataHora(): Date {
        return this.dataHora;
    }
}