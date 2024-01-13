package com.example.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory

@EnableDiscoveryClient
@SpringBootApplication
class SimpleGatewayApplication

fun main(args: Array<String>) {
    runApplication<SimpleGatewayApplication>(*args)
}

