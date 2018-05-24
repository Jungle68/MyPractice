package com.thc.www.practice

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        launch(CommonPool) {
            delay(2000L)    // 1
            println("Hello")
        }
        println("World")
        Thread.sleep(3000L) // 2
        // 在主线程中启动协程
        runBlocking {
            println("T0")
            launch(CommonPool) {
                println("T1")
                delay(3000L)
                println("T2 Hello")
            }
            println("T3 World")
            delay(5000L)
            println("T4")
        }
    }
}
