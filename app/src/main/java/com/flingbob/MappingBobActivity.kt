package com.flingbob

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MappingBobActivity : AppCompatActivity() {

    private lateinit var surfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapping_bob)

        surfaceView = findViewById(R.id.mapped_bob_surface_view)
        surfaceView.setRenderer(
            MappingBobTextureRenderer(this)
        )
    }

    override fun onPause() {
        super.onPause()
        surfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
    }
}
