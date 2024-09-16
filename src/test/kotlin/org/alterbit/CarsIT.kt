package org.alterbit

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarsIT {

    @Test
    fun `GET cars should return all cars`() = testApplication {
        client.get("/cars").apply {
            assertThat(status).isEqualTo(HttpStatusCode.OK)
            assertThat(headers["Content-Length"]).isEqualTo("46")
            assertThat(headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
            assertThat(headers.names().size).isEqualTo(2)
            assertThat(bodyAsText()).isEqualTo("""[{"id":1,"make":"Audi"},{"id":2,"make":"BMW"}]""")
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
    fun `POST car should create a new car`() = testApplication {
        client.post("/cars") {
            contentType(ContentType.Application.Json)
            setBody("""{"make":"Dacia"}""")
        }.apply {
            assertThat(status).isEqualTo(HttpStatusCode.Created)
            assertThat(headers["Content-Length"]).isEqualTo("23")
            assertThat(headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
            assertThat(headers.names().size).isEqualTo(2)
            assertThat(bodyAsText()).isEqualTo("""{"id":3,"make":"Dacia"}""")
            assertThat(client.get("/cars/3").bodyAsText()).isEqualTo("""{"id":3,"make":"Dacia"}""")
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
}
