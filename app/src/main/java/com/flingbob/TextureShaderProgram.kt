package com.flingbob

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1i


class TextureShaderProgram(
    context: Context
) : ShaderProgram(
    context, R.raw.texture_vertex_shader,
    R.raw.texture_fragment_shader
) {
    // Uniform locations
    private val uMatrixLocation: Int = glGetUniformLocation(program, U_MATRIX)
    private val uTextureUnitLocation: Int = glGetUniformLocation(program, U_TEXTURE_UNIT)

    // Attribute locations
    val aPositionLocation: Int = glGetAttribLocation(program, A_POSITION)
    val aTextureCoordinatesLocation: Int =
        glGetAttribLocation(program, A_TEXTURE_COORDINATES)

    fun setUniforms(/*matrix: FloatArray, */textureId: Int) {
        // Pass the matrix into the shader program.
//        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0)
    }
}