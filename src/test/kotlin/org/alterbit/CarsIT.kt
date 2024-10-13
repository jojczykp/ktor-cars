package org.alterbit

import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.alterbit.rest.CarResponse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CarsIT {

    @Nested
    inner class GET {

        @Test
        fun `GET cars should return all cars`() = testApplication {
            client.get("/cars").apply {
                status shouldBe HttpStatusCode.OK
                headers["Content-Length"] shouldBe "200"
                headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                headers.names().size shouldBe 2
                bodyAsText() shouldBe """[{"id":1,"make":"Audi","colour":"Red"},{"id":2,"make":"BMW","colour":"Blue"},{"id":3,"make":"Lexus","colour":"Pink"},{"id":4,"make":"Renault","colour":"Brown"},{"id":5,"make":"Ford","colour":"Green"}]"""
            }
        }

        @Test
        fun `GET cars {id} should return a car`() = testApplication {
            client.get("/cars/1").apply {
                status shouldBe HttpStatusCode.OK
                headers["Content-Length"] shouldBe "37"
                headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                headers.names().size shouldBe 2
                bodyAsText() shouldBe """{"id":1,"make":"Audi","colour":"Red"}"""
            }
        }

        @Test
        fun `GET cars {id} should return 404 if car does not exist`() = testApplication {
            client.get("/cars/99").apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `GET cars {id} should return 404 if incorrect id format specified`() = testApplication {
            client.get("/cars/invalid-id-format").apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }
    }

    @Nested
    inner class DELETE {

        @Test
        fun `DELETE cars {id} should delete a car`() = testApplication {
            client.delete("/cars/2").apply {
                status shouldBe HttpStatusCode.NoContent
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
                client.get("/cars/2").status shouldBe HttpStatusCode.NotFound
            }
        }

        @Test
        fun `DELETE cars {id} should return 404 if car does not exist`() = testApplication {
            client.delete("/cars/99").apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `DELETE cars {id} should return 404 if incorrect id format specified`() = testApplication {
            client.delete("/cars/invalid-id-format").apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }
    }

    @Nested
    inner class POST {

        @Test
        fun `POST car should create a new car`() = testApplication {
            client.post("/cars") {
                contentType(ContentType.Application.Json)
                setBody("""{"make":"Dacia","colour":"Red"}""")
            }.apply {
                status shouldBe HttpStatusCode.Created
                headers["Content-Length"] shouldBe "38"
                headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                headers.names().size shouldBe 2
                bodyAsText() shouldBe """{"id":6,"make":"Dacia","colour":"Red"}"""
                client.get("/cars/6").bodyAsText() shouldBe """{"id":6,"make":"Dacia","colour":"Red"}"""
            }
        }

        @Test
        fun `POST car should return 400 if request body is not valid`() = testApplication {
            client.post("/cars") {
                contentType(ContentType.Application.Json)
                setBody("""{"no-make":"Something"}""")
            }.apply {
                status shouldBe HttpStatusCode.BadRequest
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `POST car should return 400 if request body is not well-formed`() = testApplication {
            client.post("/cars") {
                contentType(ContentType.Application.Json)
                setBody("no json")
            }.apply {
                status shouldBe HttpStatusCode.BadRequest
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `POST car should return 415 if Content-Type is missing`() = testApplication {
            client.post("/cars") {
                setBody("""{"make":"Tesla"}""")
            }.apply {
                status shouldBe HttpStatusCode.UnsupportedMediaType
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }
    }

    @Nested
    inner class PUT {

        @Test
        fun `PUT car should update a car`() = testApplication {
            val client = createClient { install(ContentNegotiation) { json() } }
            val id = client.post("/cars") {
                contentType(ContentType.Application.Json)
                setBody("""{"make":"Toyota","colour":"Blue"}""")
            }.body<CarResponse>().id

            val response = client.put("/cars/${id}") {
                contentType(ContentType.Application.Json)
                setBody("""{"make":"Hyundai","colour":"Blue"}""")
            }

            response.status shouldBe HttpStatusCode.OK
            response.headers["Content-Length"] shouldBe "41"
            response.headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
            response.headers.names().size shouldBe 2
            response.bodyAsText() shouldBe """{"id":${id},"make":"Hyundai","colour":"Blue"}"""
            client.get("/cars/${id}").bodyAsText() shouldBe """{"id":${id},"make":"Hyundai","colour":"Blue"}"""

        }

        @Test
        fun `PUT car should return 404 if car does not exist`() = testApplication {
            client.put("/cars/99") {
                contentType(ContentType.Application.Json)
                setBody("""{"make":"Opel"}""")
            }.apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `PUT cars {id} should return 404 if incorrect id format specified`() = testApplication {
            client.put("/cars/invalid-id-format").apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `PUT car should return 400 if request body is not valid`() = testApplication {
            client.put("/cars/50") {
                contentType(ContentType.Application.Json)
                setBody("""{"no-make":"Something"}""")
            }.apply {
                status shouldBe HttpStatusCode.BadRequest
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `PUT car should return 400 if request body is not well-formed`() = testApplication {
            client.put("/cars/50") {
                contentType(ContentType.Application.Json)
                setBody("no json")
            }.apply {
                status shouldBe HttpStatusCode.BadRequest
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `PUT car should return 415 if Content-Type is missing`() = testApplication {
            client.put("/cars/50") {
                setBody("""{"make":"Peugeot"}""")
            }.apply {
                status shouldBe HttpStatusCode.UnsupportedMediaType
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }
    }
}
