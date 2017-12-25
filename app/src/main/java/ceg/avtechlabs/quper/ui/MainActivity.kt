package ceg.avtechlabs.quper.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ceg.avtechlabs.quper.R
import ceg.avtechlabs.quper.R.id.activity_main
import ceg.avtechlabs.quper.utils.getFullPath
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveLayout(window.decorView)
    }

    fun saveLayout(view: View) {

        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val cache = view.drawingCache
        val bitmap = Bitmap.createBitmap(cache, 0, 0, view.width, view.height)
        val fileName = getFullPath(Date().toString())
        try {
            val out = FileOutputStream(fileName)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            view.destroyDrawingCache()
        }
    }

}
