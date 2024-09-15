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
    fun `GET cars {id} should return single car`() = testApplication {
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

}
