package chapter03
import kotlinx.coroutines.*

suspend fun task1() {
    println("start task1 in Thread ${Thread.currentThread()}")
    yield()
    println("end task1 in Thread ${Thread.currentThread()}")
}

suspend fun task2() {
    println("start task2 in Thread ${Thread.currentThread()}")
    yield()
    println("end task2 in Thread ${Thread.currentThread()}")
}

suspend fun main() {
    println("start")
    runBlocking {
        launch(Dispatchers.Default) { task1() } //设置上下文
        launch { task2() }
        println("called task1 and task2 in Thread ${Thread.currentThread()}")
    }
    println("done")
}