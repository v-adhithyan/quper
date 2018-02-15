package ceg.avtechlabs.quper.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import ceg.avtechlabs.quper.R
import ceg.avtechlabs.quper.adapter.GridAdapter
import ceg.avtechlabs.quper.utils.getFullPath
import ceg.avtechlabs.quper.utils.listQuperDirectory
import ceg.avtechlabs.quper.utils.permissionGranted
import ceg.avtechlabs.quper.utils.quperDirectory
import kotlinx.android.synthetic.main.activity_main.*

import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    val PERMISSIONS_WRITE_STORAGE = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!permissionGranted()) {
            requestPermission()
        }

        val adapter = GridAdapter(this, R.layout.grid_layout, listQuperDirectory())
        gridView.adapter = adapter
        gridView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                val image = view!!.findViewById<ImageView>(R.id.image)
                val fileName = image?.tag.toString()

                val intent = Intent(this@MainActivity, ViewActivity::class.java)
                intent.putExtra(ViewActivity.INTENT_FILE_NAME, fileName)
                startActivity(intent)
            }

        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_WRITE_STORAGE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Write permission granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Write permission declined", Toast.LENGTH_LONG).show()
                    requestPermission()
                }
            }
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
