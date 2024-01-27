package coroutines.withContext

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
    runBlocking {
        println("starting in Thread ${Thread.currentThread()}")
        withContext(Dispatchers.Default) { task1() }
        launch { task2() }
        println("ending in Thread ${Thread.currentThread()}")
    }

}
