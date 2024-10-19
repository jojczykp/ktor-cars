package org.alterbit

import com.typesafe.config.ConfigFactory
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.alterbit.rest.CarResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.MountableFile
import io.ktor.server.testing.ApplicationTestBuilder

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarsApplicationTest {

    private val database = PostgreSQLContainer("postgres:16-alpine")
        .withCopyFileToContainer(
            MountableFile.forClasspathResource("init.sql"),
            "/docker-entrypoint-initdb.d/init.sql")

    private lateinit var testConfig: ApplicationConfig

    @BeforeAll
    fun setup() {
        database.start()

        val defaultConfig = HoconApplicationConfig(ConfigFactory.load("application.conf"))
        val databaseConfig = MapApplicationConfig(
            "cars.database.url" to database.jdbcUrl,
            "cars.database.user" to database.username,
            "cars.database.password" to database.password
        )

        testConfig = databaseConfig.withFallback(defaultConfig)
    }

    @AfterAll
    fun teardown() {
        database.stop()
    }

    @Nested
    inner class GET {

        @Test
        fun `GET cars should return all cars`() = testApplication {
            environment { config = testConfig }
            val id1 = createCar("Audi", "Red").id
            val id2 = createCar("BMW", "Blue").id

            client.get("/cars").apply {
                status shouldBe HttpStatusCode.OK
                headers["Content-Length"]!!.toInt() shouldBeGreaterThan 0
                headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                headers.names().size shouldBe 2
                bodyAsText() shouldContain """{"id":"$id1","make":"Audi","colour":"Red"}"""
                bodyAsText() shouldContain """{"id":"$id2","make":"BMW","colour":"Blue"}"""
            }
        }

        @Test
        fun `GET cars {id} should return a car`() = testApplication {
            environment { config = testConfig }
            val id1 = createCar("Lexus", "Brown").id
            val id2 = createCar("Dacia", "Yellow").id

            client.get("/cars/$id2").apply {
                status shouldBe HttpStatusCode.OK
                headers["Content-Length"] shouldBe "78"
                headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                headers.names().size shouldBe 2
                bodyAsText() shouldBe """{"id":"$id2","make":"Dacia","colour":"Yellow"}"""
            }
        }

        @Test
        fun `GET cars {id} should return 404 if car does not exist`() = testApplication {
            environment { config = testConfig }

            client.get("/cars/99").apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `GET cars {id} should return 404 if incorrect id format specified`() = testApplication {
            environment { config = testConfig }

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
            environment { config = testConfig }
            val id1 = createCar("Audi", "Red").id
            val id2 = createCar("BMW", "Blue").id

            client.delete("/cars/$id1").apply {
                status shouldBe HttpStatusCode.NoContent
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }

            client.get("/cars/$id1").status shouldBe HttpStatusCode.NotFound
        }

        @Test
        fun `DELETE cars {id} should return 404 if car does not exist`() = testApplication {
            environment { config = testConfig }

            client.delete("/cars/99").apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `DELETE cars {id} should return 404 if incorrect id format specified`() = testApplication {
            environment { config = testConfig }

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
        fun `POST car should return 400 if request body is not valid`() = testApplication {
            environment { config = testConfig }

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
            environment { config = testConfig }

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
            environment { config = testConfig }

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
            environment { config = testConfig }
            val id = createCar("Toyota", "Brown").id
            client.get("/cars/${id}").bodyAsText() shouldBe """{"id":"${id}","make":"Toyota","colour":"Brown"}"""

            client.put("/cars/${id}") {
                contentType(ContentType.Application.Json)
                setBody("""{"make":"Hyundai","colour":"Blue"}""")
            }.apply {
                status shouldBe HttpStatusCode.OK
                headers["Content-Length"] shouldBe "78"
                headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                headers.names().size shouldBe 2
                bodyAsText() shouldBe """{"id":"${id}","make":"Hyundai","colour":"Blue"}"""
            }

            client.get("/cars/${id}").bodyAsText() shouldBe """{"id":"${id}","make":"Hyundai","colour":"Blue"}"""
        }

        @Test
        fun `PUT car should return 404 if car does not exist`() = testApplication {
            environment { config = testConfig }

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
            environment { config = testConfig }

            client.put("/cars/invalid-id-format"){
                contentType(ContentType.Application.Json)
                setBody("""{"make":"Volkswagen"}""")
            }.apply {
                status shouldBe HttpStatusCode.NotFound
                headers["Content-Length"] shouldBe "0"
                headers.names().size shouldBe 1
                bodyAsText() shouldBe ""
            }
        }

        @Test
        fun `PUT car should return 400 if request body is not valid`() = testApplication {
            environment { config = testConfig }

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
            environment { config = testConfig }

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
            environment { config = testConfig }

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

    private suspend fun ApplicationTestBuilder.createCar(make: String, colour: String): CarResponse {
        val deserializingClient = createClient { install(ContentNegotiation) { json() } }

        val response = deserializingClient.post("/cars") {
            contentType(ContentType.Application.Json);
            setBody("""{"make":"$make","colour":"$colour"}""")
        }.apply {
            status shouldBe HttpStatusCode.Created
        }.body<CarResponse>()

        client.get("/cars/${response.id}").status shouldBe HttpStatusCode.OK

        return response
    }
}
