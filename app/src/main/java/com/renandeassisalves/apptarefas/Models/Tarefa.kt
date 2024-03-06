package com.renandeassisalves.apptarefas.Models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Exclude
import java.util.Date

data class Tarefa (
    @get:Exclude
    var key: String? = null,
    var userId: String? = FirebaseAuth.getInstance().currentUser?.uid,
    var titulo: String = "",
    var descricao: String = "",
    var dataHora: Date? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "titulo" to titulo,
            "descricao" to descricao,
            "dataHora" to dataHora
        )
    }
}
