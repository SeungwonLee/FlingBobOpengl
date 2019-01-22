package com.flingbob

import android.opengl.GLES20
import android.util.Log

object ShaderHelper {

    /**
     * Compiles a shader, returning the OpenGL object ID.
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        // Create a new shader object.
        val shaderObjectId = GLES20.glCreateShader(type)

        if (shaderObjectId == 0) {
            Log.w(MappingBobTextureRendererGLE20.TAG, "Could not create new shader.")

            return 0
        }

        // Pass in the shader source.
        GLES20.glShaderSource(shaderObjectId, shaderCode)

        // Compile the shader.
        GLES20.glCompileShader(shaderObjectId)

        // Get the compilation status.
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(
            shaderObjectId, GLES20.GL_COMPILE_STATUS,
            compileStatus, 0
        )

        // Print the shader info log to the Android log output.
        Log.v(
            MappingBobTextureRendererGLE20.TAG, "Results of compiling source:" + "\n" + shaderCode
                    + "\n:" + GLES20.glGetShaderInfoLog(shaderObjectId)
        )

        // Verify the compile status.
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            GLES20.glDeleteShader(shaderObjectId)

            Log.w(MappingBobTextureRendererGLE20.TAG, "Compilation of shader failed.")

            return 0
        }

        // Return the shader object ID.
        return shaderObjectId
    }

    /**
     * Links a vertex shader and a fragment shader together into an OpenGL
     * program. Returns the OpenGL program object ID, or 0 if linking failed.
     */
    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {

        // Create a new program object.
        val programObjectId = GLES20.glCreateProgram()

        if (programObjectId == 0) {
            Log.w(MappingBobTextureRendererGLE20.TAG, "Could not create new program")

            return 0
        }

        // Attach the vertex shader to the program.
        GLES20.glAttachShader(programObjectId, vertexShaderId)

        // Attach the fragment shader to the program.
        GLES20.glAttachShader(programObjectId, fragmentShaderId)

        // Link the two shaders together into a program.
        GLES20.glLinkProgram(programObjectId)

        // Get the link status.
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(
            programObjectId, GLES20.GL_LINK_STATUS,
            linkStatus, 0
        )

        // Print the program info log to the Android log output.
        Log.v(
            MappingBobTextureRendererGLE20.TAG,
            "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programObjectId)
        )

        // Verify the link status.
        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            GLES20.glDeleteProgram(programObjectId)

            Log.w(MappingBobTextureRendererGLE20.TAG, "Linking of program failed.")

            return 0
        }

        // Return the program object ID.
        return programObjectId
    }

    /**
     * Validates an OpenGL program. Should only be called when developing the
     * application.
     */
    fun validateProgram(programObjectId: Int): Boolean {
        GLES20.glValidateProgram(programObjectId)
        val validateStatus = IntArray(1)
        GLES20.glGetProgramiv(
            programObjectId, GLES20.GL_VALIDATE_STATUS,
            validateStatus, 0
        )
        Log.v(
            MappingBobTextureRendererGLE20.TAG, "Results of validating program: " + validateStatus[0]
                    + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId)
        )

        return validateStatus[0] != 0
    }

    /**
     * Helper function that compiles the shaders, links and validates the
     * program, returning the program ID.
     */
    fun buildProgram(
        vertexShaderSource: String,
        fragmentShaderSource: String
    ): Int {
        val program: Int

        // Compile the shaders.
        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, (vertexShaderSource))
        val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)

        // Link them into a shader program.
        program = linkProgram(vertexShader, fragmentShader)

        validateProgram(program)

        return program
    }
}