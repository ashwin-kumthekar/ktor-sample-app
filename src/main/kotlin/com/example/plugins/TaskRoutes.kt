package com.example.plugins

import com.example.model.Priority
import com.example.model.Routes
import com.example.model.Task
import com.example.model.TaskRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import java.lang.IllegalStateException
import kotlin.text.isEmpty

fun Route.taskRoutes() {
    post(Routes.ADD_TASKS){
        val formContent = call.receiveParameters()
        val params = Triple(
            formContent["name"]?:"",
            formContent["description"]?:"",
            formContent["priority"]?:"Low"
        )

        if(params.toList().any{it.isEmpty()}){
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        try{
            val priority = Priority.valueOf(params.third)
            TaskRepository.addTask(Task(params.first,params.second,priority))
            call.respondText("Task added successfully", status = HttpStatusCode.Created)
        }
        catch (e:IllegalArgumentException){
            println("Exception in adding task: ${e.message}")
            call.respond(HttpStatusCode.BadRequest)
        }catch (e: IllegalStateException){
            println("Exception in adding task: ${e.message}")
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}