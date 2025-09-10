package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import java.lang.IllegalStateException


/*
fun Application.configureRouting() {

    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
//            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    routing {
        get("/") {
            println("method / called")
//            call.respondText("Hello Ashwin ! Current time is: ${System.currentTimeMillis()}")
            call.respondText("Hello World!")
        }

        get("/test") {
            println("method /test called")
            val text = "<h1>Hello From Ktor</h1>"
            val type = ContentType.parse("text/html")
            call.respondText(text, type)
        }

        get("/error-test"){
            throw IllegalStateException("Too busy")
        }
    }
}*/


fun Application.configureRouting() {
    routing {

        staticResources("/task-ui", "task-ui")

        get("/tasks") {
            println("method /tasks called")
            val tasks = TaskRepository.allTasks()
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.taskAsTable()
            )
        }

        get("tasks/byPriority/{priority?}"){
            val priorityAsText = call.parameters["priority"]
            if(priorityAsText==null){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try{
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.taskByPriority(priority)

                if(tasks.isEmpty()){
                    call.respondText("No tasks found with priority $priority", status = HttpStatusCode.NotFound)
                    return@get
                }
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.taskAsTable()
                )
            } catch (e:IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest)
            }

        }

        post("/addTasks"){
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
}