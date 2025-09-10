package com.example

import com.example.model.Routes
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testCanBeFoundByPriority()=testApplication{
        application{module()}

        val response = client.get("/tasks/byPriority/High")
        val body = response.bodyAsText()
        println(body)

        assertEquals(HttpStatusCode.OK,response.status)
        assertContains(body,"Mow the lawn")
        assertContains(body,"Paint the fence")

    }

    @Test
    fun invalidPriorityProducts400() = testApplication {
        application { module() }

        val response = client.get("/tasks/byPriority/Invalid")
        assertEquals(HttpStatusCode.BadRequest,response.status)
    }

    @Test
    fun newTaskCanBeAdded()=testApplication{
        application{module()}

        val response1 = client.post(Routes.ADD_TASKS){
            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(listOf(
                "name" to "swimming",
                "description" to "Go to the beach",
                "priority" to "Low"
            ).formUrlEncode())
        }

        assertEquals(HttpStatusCode.Created,response1.status)
        println("status: ${response1.status}")

        val response2 = client.get(Routes.TASKS)
        assertEquals(HttpStatusCode.OK,response2.status)
        val body = response2.bodyAsText()
        assertContains(body,"swimming")
        assertContains(body,"Go to the beach")

    }

}