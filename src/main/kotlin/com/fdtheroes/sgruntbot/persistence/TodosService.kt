package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Todos
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class TodosService(private val todosRepository: TodosRepository) {

    fun addTodo(userId: Long, testo: String): Todos {
        val todos = Todos(
            userId = userId,
            todo = testo,
            open = true,
        )
        return todosRepository.save(todos)
    }

    fun closeTodo(userId: Long, id: Long): Boolean {
        val todo = todosRepository.findByIdAndUserIdAndOpen(id, userId, true)
        if (todo == null) {
            return false
        }
        todo.open = false
        todosRepository.save(todo)

        return true
    }

    fun allTodos(onlyOpen: Boolean): List<Todos> {
        val allTodos = todosRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
        if (onlyOpen) {
            return allTodos.filter { it.open }
        }
        return allTodos
    }

}
