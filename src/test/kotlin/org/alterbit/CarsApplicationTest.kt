package org.alterbit

import com.typesafe.config.ConfigFactory
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.ShouldSpec
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
import org.alterbit.rest.cars.CarResponse
import org.alterbit.test_utils.FlywayDBContainerExtension
import org.testcontainers.containers.PostgreSQLContainer

class CarsApplicationTest : ShouldSpec({

    val db = install(FlywayDBContainerExtension(
        PostgreSQLContainer("postgres:16-alpine")))

    val configOverrides = MapApplicationConfig(
        "app.database.url" to db.jdbcUrl,
        "app.database.user" to db.username,
        "app.database.password" to db.password
    )

    context("GET") {

        should("GET /cars should return all cars") {
            testApplicationWith(configOverrides) {
                val id1 = createCar("Audi", "Red").id
                val id2 = createCar("BMW", "Blue").id

                val response = client.get("/cars")

                response.apply {
                    status shouldBe HttpStatusCode.OK
                    headers["Content-Length"]!!.toInt() shouldBeGreaterThan 0
                    headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                    headers.names().size shouldBe 2
                    bodyAsText() shouldContain """{"id":"$id1","make":"Audi","colour":"Red"}"""
                    bodyAsText() shouldContain """{"id":"$id2","make":"BMW","colour":"Blue"}"""
                }
            }
        }

        should("GET /cars/{id} should return a car") {
            testApplicationWith(configOverrides) {
                val id1 = createCar("Lexus", "Brown").id
                val id2 = createCar("Dacia", "Yellow").id

                val response = client.get("/cars/$id2")

                response.apply {
                    status shouldBe HttpStatusCode.OK
                    headers["Content-Length"] shouldBe "78"
                    headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                    headers.names().size shouldBe 2
                    bodyAsText() shouldBe """{"id":"$id2","make":"Dacia","colour":"Yellow"}"""
                }
            }
        }

        should("GET /cars/{id} should return 404 if car does not exist") {
            testApplicationWith(configOverrides) {
                val response = client.get("/cars/99")

                response.apply {
                    status shouldBe HttpStatusCode.NotFound
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("GET /cars/{id} should return 404 if incorrect id format has been specified") {
            testApplicationWith(configOverrides) {
                val response = client.get("/cars/invalid-id-format")

                response.apply {
                    status shouldBe HttpStatusCode.NotFound
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }
    }

    context("DELETE") {

        should("DELETE /cars/{id} should delete a car") {
            testApplicationWith(configOverrides) {
                val id1 = createCar("Audi", "Red").id
                val id2 = createCar("BMW", "Blue").id

                val response = client.delete("/cars/$id1")

                response.apply {
                    status shouldBe HttpStatusCode.OK
                    headers["Content-Length"] shouldBe "74"
                    headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                    headers.names().size shouldBe 2
                    bodyAsText() shouldBe """{"id":"$id1","make":"Audi","colour":"Red"}"""
                }

                getCarOrNull(id1) shouldBe null
            }
        }

        should("DELETE /cars/{id} should return 404 if car does not exist") {
            testApplicationWith(configOverrides) {
                val response = client.delete("/cars/99")

                response.apply {
                    status shouldBe HttpStatusCode.NotFound
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("DELETE /cars/{id} should return 404 if incorrect id format has been specified") {
            testApplicationWith(configOverrides) {
                val response = client.delete("/cars/invalid-id-format")

                response.apply {
                    status shouldBe HttpStatusCode.NotFound
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }
    }

    context("POST") {

        should("POST /cars should return 400 if request body is not valid") {
            testApplicationWith(configOverrides) {
                val response = client.post("/cars") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"no-make":"Something"}""")
                }

                response.apply {
                    status shouldBe HttpStatusCode.BadRequest
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("POST /cars should return 400 if request body is not well-formed") {
            testApplicationWith(configOverrides) {
                val response = client.post("/cars") {
                    contentType(ContentType.Application.Json)
                    setBody("no json")
                }

                response.apply {
                    status shouldBe HttpStatusCode.BadRequest
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("POST /cars should return 415 if Content-Type is missing") {
            testApplicationWith(configOverrides) {
                val response = client.post("/cars") {
                    setBody("""{"make":"Tesla"}""")
                }

                response.apply {
                    status shouldBe HttpStatusCode.UnsupportedMediaType
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }
    }

    context("PUT") {

        should("PUT /cars/{id} should update a car") {
            testApplicationWith(configOverrides) {
                // Given
                val id = createCar("Toyota", "Brown").id

                // When
                val response = client.put("/cars/$id") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"make":"Hyundai","colour":"Blue"}""")
                }

                // Then
                response.apply {
                    status shouldBe HttpStatusCode.OK
                    headers["Content-Length"] shouldBe "78"
                    headers["Content-Type"] shouldBe "application/json; charset=UTF-8"
                    headers.names().size shouldBe 2
                    bodyAsText() shouldBe """{"id":"$id","make":"Hyundai","colour":"Blue"}"""
                }

                getCar(id) shouldBe CarResponse(id, "Hyundai", "Blue")
            }
        }

        should("PUT /cars/{id} should return 404 if car does not exist") {
            testApplicationWith(configOverrides) {
                val response = client.put("/cars/99") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"make":"Opel"}""")
                }

                response.apply {
                    status shouldBe HttpStatusCode.NotFound
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("PUT /cars/{id} should return 404 if incorrect id format has been specified") {
            testApplicationWith(configOverrides) {
                val response = client.put("/cars/invalid-id-format") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"make":"Volkswagen"}""")
                }

                response.apply {
                    status shouldBe HttpStatusCode.NotFound
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("PUT /cars/{id} should return 400 if request body is not valid") {
            testApplicationWith(configOverrides) {
                val response = client.put("/cars/50") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"no-make":"Something"}""")
                }

                response.apply {
                    status shouldBe HttpStatusCode.BadRequest
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("PUT /cars/{id} should return 400 if request body is not well-formed") {
            testApplicationWith(configOverrides) {
                val response = client.put("/cars/50") {
                    contentType(ContentType.Application.Json)
                    setBody("no json")
                }

                response.apply {
                    status shouldBe HttpStatusCode.BadRequest
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }

        should("PUT /cars/{id} should return 415 if Content-Type is missing") {
            testApplicationWith(configOverrides) {
                val response = client.put("/cars/50") {
                    setBody("""{"make":"Peugeot"}""")
                }

                response.apply {
                    status shouldBe HttpStatusCode.UnsupportedMediaType
                    headers["Content-Length"] shouldBe "0"
                    headers.names().size shouldBe 1
                    bodyAsText() shouldBe ""
                }
            }
        }
    }
})

fun testApplicationWith(configOverrides: ApplicationConfig, test: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        environment {
            config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
                .mergeWith(configOverrides)
        }
        test()
    }
}

suspend fun ApplicationTestBuilder.createCar(make: String, colour: String): CarResponse {
    val deserializingClient = createClient { install(ContentNegotiation) { json() } }

    return deserializingClient.post("/cars") {
        contentType(ContentType.Application.Json)
        setBody("""{"make":"$make","colour":"$colour"}""")
    }.apply {
        status shouldBe HttpStatusCode.Created
    }.body<CarResponse>()
}

suspend fun ApplicationTestBuilder.getCar(id: String): CarResponse =
    getCarOrNull(id) ?: throw IllegalArgumentException("Car with id $id not found")

suspend fun ApplicationTestBuilder.getCarOrNull(id: String): CarResponse? {
    val deserializingClient = createClient { install(ContentNegotiation) { json() } }

    val response = deserializingClient.get("/cars/$id") {
        accept(ContentType.Application.Json)
    }

    return when (response.status) {
        HttpStatusCode.OK -> response.body()
        HttpStatusCode.NotFound -> null
        else -> throw IllegalStateException("Unexpected response status: ${response.status}")
    }
}
