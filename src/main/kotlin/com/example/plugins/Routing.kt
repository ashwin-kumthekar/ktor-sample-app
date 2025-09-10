package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.*


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
            val taskName = call.request.queryParameters["name"]
            val taskDescription = call.request.queryParameters["description"]
            val taskPriority = call.request.queryParameters["priority"]


        }

    }
}