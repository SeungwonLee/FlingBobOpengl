package com.flingbob

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MappingBobTextureRendererGLE20(val context: Context) : GLSurfaceView.Renderer {
    private val vertices = floatArrayOf(
        // Order of coordinates: X, Y, S, T
        0.0f, 0.0f, 0.0f, 0.0f,
        319.0f, 0.0f, 0.5f, 0.5f,
        160.0f, 479.0f, 1.0f, 1.0f
    )
    private val floatBuffer: FloatBuffer = ByteBuffer
        .allocateDirect(vertices.size * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertices)

    // Shader program
    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram

    private var textureId: Int = 0
    private var width: Int = 0
    private var height: Int = 0

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glViewport(0, 0, width, height)

        floatBuffer.position(0)
        glVertexAttribPointer(
            colorProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            floatBuffer
        )
        glEnableVertexAttribArray(textureProgram.aPositionLocation)
        floatBuffer.position(0)

        floatBuffer.position(2)
        glVertexAttribPointer(
            textureProgram.aPositionLocation,
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            floatBuffer
        )
        glEnableVertexAttribArray(textureProgram.aPositionLocation)
        floatBuffer.position(0)


        textureProgram.useProgram()
        textureProgram.setUniforms(textureId)

        // Draw the mallets.
        colorProgram.useProgram()
//        colorProgram.setUniforms(projectionMatrix)

        GLES20.glDrawArrays(GL10.GL_TRIANGLES, 0, 3)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        textureId = loadTexture(context, R.drawable.bobrgb888)
        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
    }

    companion object {
        const val TAG = "MappingBobTxtRenderer"
        private const val BYTES_PER_FLOAT = 4
        private const val POSITION_COMPONENT_COUNT = 2
        private const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
        private const val STRIDE = (POSITION_COMPONENT_COUNT
                + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT

        fun loadTexture(context: Context, resourceId: Int): Int {
            val textureObjectIds = IntArray(1)
            GLES20.glGenTextures(1, textureObjectIds, 0)

            if (textureObjectIds[0] == 0) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.")
                return 0
            }

            val options = BitmapFactory.Options()
            options.inScaled = false

            // Read in the resource
            val bitmap = BitmapFactory.decodeResource(
                context.resources, resourceId, options
            )

            if (bitmap == null) {
                Log.w(TAG, "Resource ID $resourceId could not be decoded.")

                GLES20.glDeleteTextures(1, textureObjectIds, 0)
                return 0
            }
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])

            // Set filtering: a default must be set, or the texture will be
            // black.
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR_MIPMAP_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            // Note: Following code may cause an error to be reported in the
            // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
            // Failed to generate texture mipmap levels (error=3)
            // No OpenGL error will be encountered (glGetError() will return
            // 0). If this happens, just squash the source image to be
            // square. It will look the same because of texture coordinates,
            // and mipmap generation will work.

            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

            // Recycle the bitmap, since its data has been loaded into
            // OpenGL.
            bitmap.recycle()

            // Unbind from the texture.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

            return textureObjectIds[0]
        }

    }
}