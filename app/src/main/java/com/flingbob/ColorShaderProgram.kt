package com.flingbob

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation

class ColorShaderProgram(context: Context) : ShaderProgram(context, R.raw.simple_vertex_shader,
    R.raw.simple_fragment_shader) {
    // Uniform locations
    private val uMatrixLocation: Int = glGetUniformLocation(program, U_MATRIX)

    // Attribute locations
    val aPositionLocation: Int = glGetAttribLocation(program, A_POSITION)
    val aColorLocation: Int =  glGetAttribLocation(program, A_COLOR)

    fun setUniforms(matrix: FloatArray) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

}