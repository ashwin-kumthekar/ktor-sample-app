package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
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

}