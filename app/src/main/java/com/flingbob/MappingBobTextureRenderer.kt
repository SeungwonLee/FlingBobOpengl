package com.flingbob

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES10
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MappingBobTextureRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private var vertices: FloatBuffer
    private var textureId: Int = 0
    private var width: Int = 0
    private var height: Int = 0

    init {
        val byteBuffer: ByteBuffer =
            ByteBuffer
                .allocateDirect(3 * VERTEX_SIZE)
                .order(ByteOrder.nativeOrder())

        vertices = byteBuffer.asFloatBuffer()
        vertices = vertices.put(
            floatArrayOf(
                0.0f, 0.0f, 0.0f, 0.0f,
                319.0f, 0.0f, 1.0f, 0.0f,
                160.0f, 479.0f, 0.5f, 1.0f
            )
        )
        vertices.flip()

        textureId = loadTexture()
    }

    private fun loadTexture(): Int {
        try {
            val option = BitmapFactory.Options()
            val bitmap =
                BitmapFactory.decodeResource(context.resources, R.drawable.bobrgb888, option)
            val textureIds = IntArray(1)
            GLES10.glGenTextures(1, textureIds, 0)
            val textureId = textureIds[0]
            GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textureId)
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
            GLES10.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST.toFloat()
            )
            GLES10.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_NEAREST.toFloat()
            )
            GLES10.glBindTexture(GL10.GL_TEXTURE_2D, 0)
            bitmap?.recycle()

            Log.d("TexturedTriangleTest", "textureId=$textureId")

            return textureId
        } catch (e: IOException) {
            Log.d("TexturedTriangleTest", "couldn't load asset 'bobrgb888.png'!")
            throw RuntimeException("couldn't load asset")
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        if (gl == null) {
            return
        }

        gl.glViewport(0, 0, width, height)
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        gl.glOrthof(0f, 320f, 0f, 480f, 1f, -1f)

        gl.glEnable(GL10.GL_TEXTURE_2D)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        vertices.position(0)
        gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices)
        vertices.position(2)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices)

        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3)

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        gl.glDisable(GL10.GL_TEXTURE_2D)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height

        gl?.glViewport(0, 0, width, height)
        gl?.glClear(GLES10.GL_COLOR_BUFFER_BIT)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    companion object {
        const val VERTEX_SIZE = (2 + 2) * 4
    }
}