package com.flingbob

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MappingBobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapping_bob)

        findViewById<GLSurfaceView>(R.id.mapped_bob_surface_view).setRenderer(
            MappingBobTextureRenderer(this)
        )
    }

}
