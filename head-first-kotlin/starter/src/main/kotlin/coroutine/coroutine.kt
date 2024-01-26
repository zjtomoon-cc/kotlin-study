package coroutine

import java.io.File
import javax.sound.sampled.AudioSystem
import kotlinx.coroutines.*

suspend fun playBeats(beats: String, file: String) {
    var parts = beats.split("x")
    var count = 0
    for (part in parts) {
        count += part.length + 1
        if (part == "") {
            playSound(file)
        } else {
//            Thread.sleep(100 * (part.length + 1L))
            delay(100 * (part.length + 1L))
            if (count < beats.length) {
                playSound(file)
            }
        }
    }
}

fun playSound(file: String) {
    var clip = AudioSystem.getClip()
    var audioInputStream = AudioSystem.getAudioInputStream(
        File(
            file
        )
    )
    clip.open(audioInputStream)
    clip.start()
}

suspend fun main() {
//    playBeats("x-x-x-x-x-x-","toms.aiff")
    // 创建新的线程和新的协程
//    GlobalScope.launch {
//        playBeats("x-x-x-x-x-x-", "toms.aiff")
//    }
//    playBeats("x-----x-----", "crash_cymbal.aiff")

    runBlocking {
        launch {
            playBeats("x-x-x-x-x-x-", "toms.aiff")
        }
        playBeats("x-----x-----", "crash_cymbal.aiff")
    }
}