package com.flingbob

import android.opengl.GLES10
import android.opengl.GLES10.GL_COLOR_BUFFER_BIT
import android.opengl.GLES10.GL_FLOAT
import android.opengl.GLES10.GL_PROJECTION
import android.opengl.GLES10.GL_TRIANGLES
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ColoredTriangleRenderer : GLSurfaceView.Renderer {
    private val vertices: FloatBuffer

    private var width: Int = 0
    private var height: Int = 0

    init {
        val byteBuffer = ByteBuffer
            .allocateDirect(3 * VERTEX_SIZE) // Native Heap Memory, If not RuntimeException.
            .order(ByteOrder.nativeOrder()) // Big-Endian or Little-Endian
        vertices = byteBuffer.asFloatBuffer()
        vertices.put(
            floatArrayOf(
                // x, y, R, G, B, A
                0.0f, 0.0f, 1f, 0f, 0f, 1f,
                319.0f, 0.0f, 0f, 1f, 0f, 1f,
                160.0f, 479.0f, 0f, 0f, 1f, 1f
            )
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        if (BuildConfig.DEBUG) {
            Log.d("This", "onDrawFrame: $width $height")
        }

        if (gl == null) {
            return
        }
        gl.glViewport(0, 0, width, height) // the resolution of the current screen
        gl.glClear(GL_COLOR_BUFFER_BIT) // initialize the previous color value

        gl.glMatrixMode(GL_PROJECTION)
        gl.glLoadIdentity() // initialize the matrix calculation
        gl.glOrthof(0f, 320f, 0f, 480f, 1f, -1f) // (0,0,1) ~ (480,320,-1)

        gl.glEnableClientState(GLES10.GL_COLOR_ARRAY)
        gl.glEnableClientState(GLES10.GL_VERTEX_ARRAY)

        // set triangle vertices, GL don't know what it is the triangle vertex.
        vertices.position(0)
        gl.glVertexPointer(
            2,
            GL_FLOAT,
            VERTEX_SIZE,
            vertices
        )

        // set color vertices, GL don't know what it is the color code.
        vertices.position(2)
        gl.glColorPointer(
            4,
            GL_FLOAT,
            VERTEX_SIZE,
            vertices
        )

        gl.glDrawArrays(GL_TRIANGLES, 0, 3)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height

        gl?.glViewport(0, 0, width, height)
        gl?.glClear(GL_COLOR_BUFFER_BIT)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    companion object {
        const val VERTEX_SIZE = (2 + 4) * 4 // (x,y + RGBA) * Float Size
    }
}
