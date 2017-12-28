package ceg.avtechlabs.quper.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.util.Measure
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import ceg.avtechlabs.quper.R
import ceg.avtechlabs.quper.utils.getFullPath
import ceg.avtechlabs.quper.utils.permissionGranted

import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    val PERMISSIONS_WRITE_STORAGE = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //saveLayout(window.decorView)
        if(!permissionGranted()) {
            requestPermission()
        } else {
            saveLayout(window.decorView)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_WRITE_STORAGE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Write permission granted", Toast.LENGTH_LONG).show()
                    saveLayout(window.decorView)
                } else {
                    Toast.makeText(this, "Write permission declined", Toast.LENGTH_LONG).show()
                    requestPermission()
                }
            }
        }
    }
    fun saveLayout(view: View) {

        view.isDrawingCacheEnabled = true
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0,0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(view.drawingCache)

        val fileName = getFullPath(Date().toString())
        try {
            val out = FileOutputStream(fileName)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            view.isDrawingCacheEnabled = false
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_WRITE_STORAGE)
    }

    fun createNewWallpaper(view: View) {
        startActivity(Intent(this, EditWallpaper::class.java))
    }
}
