package chapter03

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import java.util.concurrent.Executors


suspend fun task3() {
    println("start task1 in Thread ${Thread.currentThread()}")
    yield()
    println("end task1 in Thread ${Thread.currentThread()}")
}

suspend fun task4() {
    println("start task2 in Thread ${Thread.currentThread()}")
    yield()
    println("end task2 in Thread ${Thread.currentThread()}")
}

suspend fun main() {
    // 单线程池
    println("single")
    Executors.newSingleThreadExecutor().asCoroutineDispatcher().use { context ->
        println("start")

        runBlocking {
            launch(context) { task3() }
            launch { task4() }
            println("called task1 and task2 from ${Thread.currentThread()}")
        }
        println("done")
    }
    // 多线程池
    println("multiple")
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).asCoroutineDispatcher().use { context ->
        println("start")

        runBlocking {
            launch(context) { task3() }
            launch { task4() }
            println("called task1 and task2 from ${Thread.currentThread()}")
        }
        println("done")
    }
}