package async

import kotlinx.coroutines.*

suspend fun doWork(id:Int,sleep:Long) = coroutineScope {
    try {
        println("$id: entered $sleep")
        delay(sleep)
        println("$id: leaving $sleep")
        withContext(NonCancellable) {
            println("$id:do not disturb,please")
            delay(5000)
            println("$id: Ok,you can talk to me now")
        }
        println("$id: outside the restricted context")
        println("$id: isActive: $isActive")
    } catch (ex:CancellationException) {
        println("$id: doWork($sleep) was cancelled")
    }
}

suspend fun main() {
    runBlocking {
        val job = launch(Dispatchers.Default) {
            launch { doWork(1,3000) }
            launch { doWork(2,1000) }
        }

        Thread.sleep(2000)
        job.cancel()
        println("cancelling")
        job.join()
        println("done")
    }
}
