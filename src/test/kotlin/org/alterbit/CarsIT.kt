package org.alterbit

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.alterbit.model.Car
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarsIT {

    @Test
    fun `GET cars should return all cars`() = testApplication {
        client.get("/cars").apply {
            assertThat(status).isEqualTo(HttpStatusCode.OK)
            assertThat(headers["Content-Length"]).isEqualTo("70")
            assertThat(headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
            assertThat(headers.names().size).isEqualTo(2)
            assertThat(bodyAsText()).isEqualTo("""[{"id":1,"make":"Audi"},{"id":2,"make":"BMW"},{"id":3,"make":"Lexus"}]""")
        }
    }

    @Test
    fun `GET cars {id} should return a car`() = testApplication {
        client.get("/cars/1").apply {
            assertThat(status).isEqualTo(HttpStatusCode.OK)
            assertThat(headers["Content-Length"]).isEqualTo("22")
            assertThat(headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
            assertThat(headers.names().size).isEqualTo(2)
            assertThat(bodyAsText()).isEqualTo("""{"id":1,"make":"Audi"}""")
        }
    }

    @Test
    fun `GET cars {id} should return 404 if car does not exist`() = testApplication {
        client.get("/cars/99").apply {
            assertThat(status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `GET cars {id} should return 404 if incorrect id format specified`() = testApplication {
        client.get("/cars/invalid-id-format").apply {
            assertThat(status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `DELETE cars {id} should delete a car`() = testApplication {
        client.delete("/cars/2").apply {
            assertThat(status).isEqualTo(HttpStatusCode.NoContent)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
            assertThat(client.get("/cars/2").status).isEqualTo(HttpStatusCode.NotFound)
        }
    }

    @Test
    fun `DELETE cars {id} should return 404 if car does not exist`() = testApplication {
        client.delete("/cars/99").apply {
            assertThat(status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `DELETE cars {id} should return 404 if incorrect id format specified`() = testApplication {
        client.delete("/cars/invalid-id-format").apply {
            assertThat(status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `POST car should create a new car`() = testApplication {
        client.post("/cars") {
            contentType(ContentType.Application.Json)
            setBody("""{"make":"Dacia"}""")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.Created)
            assertThat(headers["Content-Length"]).isEqualTo("23")
            assertThat(headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
            assertThat(headers.names().size).isEqualTo(2)
            assertThat(bodyAsText()).isEqualTo("""{"id":4,"make":"Dacia"}""")
            assertThat(client.get("/cars/4").bodyAsText()).isEqualTo("""{"id":4,"make":"Dacia"}""")
        }
    }

    @Test
    fun `POST car should return 400 if request body is not valid`() = testApplication {
        client.post("/cars") {
            contentType(ContentType.Application.Json)
            setBody("""{"no-make":"Something"}""")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `POST car should return 400 if request body is not well-formed`() = testApplication {
        client.post("/cars") {
            contentType(ContentType.Application.Json)
            setBody("no json")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `POST car should return 415 if Content-Type is missing`() = testApplication {
        client.post("/cars") {
            setBody("""{"make":"Tesla"}""")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.UnsupportedMediaType)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `PUT car should update a car`() = testApplication {
        val client = createClient { install(ContentNegotiation) { json() } }
        val id = client.post("/cars") {
            contentType(ContentType.Application.Json)
            setBody("""{"make":"Toyota"}""")
        }.body<Car>().id

        val response = client.put("/cars/${id}") {
            contentType(ContentType.Application.Json)
            setBody("""{"make":"Hyundai"}""")
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.headers["Content-Length"]).isEqualTo("25")
        assertThat(response.headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
        assertThat(response.headers.names().size).isEqualTo(2)
        assertThat(response.bodyAsText()).isEqualTo("""{"id":${id},"make":"Hyundai"}""")
        assertThat(client.get("/cars/${id}").bodyAsText())
            .isEqualTo("""{"id":${id},"make":"Hyundai"}""")

    }

    @Test
    fun `PUT car should return 404 if car does not exist`() = testApplication {
        client.put("/cars/99") {
            contentType(ContentType.Application.Json)
            setBody("""{"make":"Opel"}""")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `PUT cars {id} should return 404 if incorrect id format specified`() = testApplication {
        client.put("/cars/invalid-id-format").apply {
            assertThat(status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `PUT car should return 400 if request body is not valid`() = testApplication {
        client.put("/cars/50") {
            contentType(ContentType.Application.Json)
            setBody("""{"no-make":"Something"}""")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `PUT car should return 400 if request body is not well-formed`() = testApplication {
        client.put("/cars/50") {
            contentType(ContentType.Application.Json)
            setBody("no json")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }

    @Test
    fun `PUT car should return 415 if Content-Type is missing`() = testApplication {
        client.put("/cars/50") {
            setBody("""{"make":"Peugeot"}""")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.UnsupportedMediaType)
            assertThat(headers["Content-Length"]).isEqualTo("0")
            assertThat(headers.names().size).isEqualTo(1)
            assertThat(bodyAsText()).isEmpty()
        }
    }
}
