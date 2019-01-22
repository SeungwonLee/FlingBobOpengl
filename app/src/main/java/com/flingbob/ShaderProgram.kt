package com.flingbob

import android.content.Context
import android.opengl.GLES20.glUseProgram

open class ShaderProgram constructor(
    context: Context, vertexShaderResourceId: Int,
    fragmentShaderResourceId: Int
) {
    // Shader program
    val program: Int

    init {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(
                context, vertexShaderResourceId
            ),
            TextResourceReader.readTextFileFromResource(
                context, fragmentShaderResourceId
            )
        )
    }

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program)
    }

    companion object {
        // Uniform constants
        val U_MATRIX = "u_Matrix"
        val U_TEXTURE_UNIT = "u_TextureUnit"

        // Attribute constants
        val A_POSITION = "a_Position"
        val A_COLOR = "a_Color"
        val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
    }
}