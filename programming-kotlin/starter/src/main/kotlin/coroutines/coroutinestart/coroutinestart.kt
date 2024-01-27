package coroutines.coroutinestart

import kotlinx.coroutines.*
import java.util.concurrent.Executors


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
    // 多线程池
    println("multiple")
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).asCoroutineDispatcher().use { context ->
        println("start")

        runBlocking {
            @UseExperimental(ExperimentalCoroutinesApi::class)
            launch(context = context,start = CoroutineStart.UNDISPATCHED) { task1() }
            launch { task2() }
            println("called task1 and task2 from ${Thread.currentThread()}")
        }
        println("done")
    }

}