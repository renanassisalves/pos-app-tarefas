package com.renandeassisalves.apptarefas.Repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.renandeassisalves.apptarefas.Models.Tarefa
import java.util.Date

class TarefaRepository(context: Context) {
    private val database = FirebaseDatabase.getInstance().reference.child("tarefas")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val sharedPreferences = context.getSharedPreferences("tarefas_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun cadastrarTarefa(tarefa: Tarefa) {
        userId?.let { uid ->
            val tarefaRef = database.child(uid).push()
            tarefaRef.setValue(tarefa)
        }
        val tarefas = lerTarefasLocais().toMutableList()
        tarefas.add(tarefa)
        salvarTarefas(tarefas)
    }

    fun lerTarefas(callback: (List<Tarefa>) -> Unit) {
        userId?.let { uid ->
            database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tarefas = mutableListOf<Tarefa>()
                    for (snapshot in dataSnapshot.children) {
                        val tarefa = snapshot.getValue(Tarefa::class.java)
                        val tarefaKey = snapshot.key
                        tarefa?.let {
                            it.key = tarefaKey
                            tarefas.add(it)
                        }
                    }
                    salvarTarefas(tarefas)
                    callback(tarefas)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    }

    fun encontrarTarefaPorKey(key: String, callback: (Tarefa?) -> Unit) {
        userId?.let { uid ->
            database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var tarefaEncontrada: Tarefa? = null
                    for (snapshot in dataSnapshot.children) {
                        val tarefa = snapshot.getValue(Tarefa::class.java)
                        val tarefaKey = snapshot.key
                        if (tarefa != null && snapshot.key == key) {
                            tarefaEncontrada = tarefa
                            tarefaEncontrada.let {
                                it.key = tarefaKey
                            }
                            break
                        }
                    }
                    callback(tarefaEncontrada)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
        }
    }

    fun atualizarTarefa(tarefa: Tarefa, callback: (Boolean) -> Unit) {
        userId?.let { uid ->
            database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var tarefaEncontrada: Tarefa? = null
                    for (snapshot in dataSnapshot.children) {
                        val tarefaFirebase = snapshot.getValue(Tarefa::class.java)
                        val tarefaKey = snapshot.key
                        if (tarefaFirebase != null && tarefaKey == tarefa.key) {
                            database.child(uid).child(tarefaKey!!).setValue(tarefa)
                            tarefaEncontrada = tarefaFirebase

                            val tarefas = lerTarefasLocais().toMutableList()
                            val index = tarefas.indexOfFirst { it.key == tarefa.key }
                            if (index != -1) {
                            tarefas[index] = tarefa
                            salvarTarefas(tarefas)
                            }

                            break
                        }
                    }
                    val success = tarefaEncontrada != null
                    callback(success)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(false)
                }
            })
        }
    }

    fun lerTarefasLocais(): List<Tarefa> {
        val json = sharedPreferences.getString(userId, null)
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Tarefa>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun encontrarTarefaLocalPorKey(key: String): Tarefa? {
        val json = sharedPreferences.getString(userId, null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Tarefa>>() {}.type
            val tarefas: List<Tarefa> = gson.fromJson(json, type)

            return tarefas.find { it.key == key }
        }
        return null
    }


    private fun salvarTarefas(tarefas: List<Tarefa>) {
        val json = gson.toJson(tarefas)
        sharedPreferences.edit().putString(userId, json).apply()
    }


    fun deletarTarefa(tarefa: Tarefa, callback: (Boolean) -> Unit) {
        userId?.let { uid ->
            database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var tarefaEncontrada = false
                    for (snapshot in dataSnapshot.children) {
                        val tarefaFirebase = snapshot.getValue(Tarefa::class.java)
                        val tarefaKey = snapshot.key
                        if (tarefaFirebase != null && tarefaKey == tarefa.key) {
                            database.child(uid).child(tarefaKey!!).removeValue()
                            tarefaEncontrada = true

                            val tarefas = lerTarefasLocais().toMutableList()
                            tarefas.removeIf { it.key == tarefa.key }
                            salvarTarefas(tarefas)

                            break
                        }
                    }
                    callback(tarefaEncontrada)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(false)
                }
            })
        }
    }

    fun deletarTarefa(tarefa: Tarefa) {
        userId?.let { uid ->
            database.child(uid).orderByChild("key").equalTo(tarefa.key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val tarefaRef = snapshot.ref
                        tarefaRef.removeValue()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
        val tarefas = lerTarefasLocais().toMutableList()
        tarefas.removeIf { it.key == tarefa.key }
        salvarTarefas(tarefas)
    }

}
