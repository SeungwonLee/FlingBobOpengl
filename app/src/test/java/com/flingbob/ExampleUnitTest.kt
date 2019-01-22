package com.flingbob

import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.FloatBuffer

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val floatBuffer = FloatBuffer.allocate(10)

        System.out.println("capacity = ${floatBuffer.capacity()}")
        System.out.println("remaining = ${floatBuffer.remaining()}")
        System.out.println("limit = ${floatBuffer.limit()}")
        System.out.println("position = ${floatBuffer.position()}")

        floatBuffer.put(1f)
        floatBuffer.put(2f)
        floatBuffer.put(3f)

        System.out.println()
        System.out.println("capacity = ${floatBuffer.capacity()}")
        System.out.println("remaining = ${floatBuffer.remaining()}")
        System.out.println("limit = ${floatBuffer.limit()}")
        System.out.println("position = ${floatBuffer.position()}")

        floatBuffer.limit(7)

        System.out.println()
        System.out.println("capacity = ${floatBuffer.capacity()}")
        System.out.println("remaining = ${floatBuffer.remaining()}")
        System.out.println("limit = ${floatBuffer.limit()}")
        System.out.println("position = ${floatBuffer.position()}")

        System.out.println()

        floatBuffer.flip()

        System.out.println("capacity = ${floatBuffer.capacity()}")
        System.out.println("remaining = ${floatBuffer.remaining()}")
        System.out.println("limit = ${floatBuffer.limit()}")
        System.out.println("position = ${floatBuffer.position()}")

    }
}
