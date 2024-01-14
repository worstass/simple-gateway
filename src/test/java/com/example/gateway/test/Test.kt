package com.example.gateway.test

import com.alibaba.nacos.api.NacosFactory
import com.alibaba.nacos.api.PropertyKeyConst
import com.sun.net.httpserver.HttpServer
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.InetSocketAddress
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Test(
    @LocalServerPort
    private var port: Int
) {

    companion object {
        private val SERVER_IP = "127.0.0.1"
        private val SERVER_NAME = "greeting"
        private val SERVER_PORT: Int = 8000
        private val server: HttpServer =  HttpServer.create(InetSocketAddress(SERVER_PORT), 0)

        @JvmStatic
        @BeforeAll
        fun setUp() {
            server.createContext("/hello") {
                val response = "world"
                it.sendResponseHeaders(200, response.length.toLong())
                val os = it.responseBody
                os.write("world".toByteArray())
                os.close()
            }
            server.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            server.stop(0)
        }
    }

    @Test
    fun resigger() {
        given()
            .get("http://${SERVER_IP}:${SERVER_PORT}/hello")
            .then()
            .assertThat().body(containsString("world"))

//        val properties = Properties()
//        properties[PropertyKeyConst.SERVER_ADDR] = "127.0.0.1:8848"
//        val service = NacosFactory.createNamingService(properties)
//        try {
//            service.registerInstance(SERVER_NAME, SERVER_IP, SERVER_PORT)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        Thread.sleep(5000)

        given()
            .port(port)
            .get("${SERVER_NAME}/hello")
            .then()
            .assertThat().body(containsString("world"))
    }
}


