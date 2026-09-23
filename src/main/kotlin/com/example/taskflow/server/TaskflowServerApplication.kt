package com.example.taskflow.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskFlowServerApplication

fun main(args: Array<String>) {
	runApplication<TaskFlowServerApplication>(*args)
}