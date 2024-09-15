package org.alterbit

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarsIT {

    @Test
    fun `GET cars should return cars`() = testApplication {
        client.get("/cars").apply {
            assertThat(status).isEqualTo(HttpStatusCode.OK)
            assertThat(headers["Content-Length"]).isEqualTo("46")
            assertThat(headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
            assertThat(headers.names().size).isEqualTo(2)
            assertThat(bodyAsText()).isEqualTo("""[{"id":1,"make":"Audi"},{"id":2,"make":"BMW"}]""")
        }
    }

    @Test
    fun `HEAD cars should return cars headers`() = testApplication {
        client.head("/cars").apply {
            assertThat(status).isEqualTo(HttpStatusCode.OK)
            assertThat(headers["Content-Length"]).isEqualTo("46")
            assertThat(headers["Content-Type"]).isEqualTo("application/json; charset=UTF-8")
            assertThat(headers.names().size).isEqualTo(2)
            assertThat(bodyAsText()).isEmpty()
        }
    }
}
