package ceg.avtechlabs.qpr.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import ceg.avtechlabs.qpr.R
import ceg.avtechlabs.qpr.utils.quperDirectory
import ceg.avtechlabs.qpr.utils.setWallpaper
import com.google.android.gms.ads.AdRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view.*
import java.io.File

class ViewActivity : AppCompatActivity() {

    var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val fileName = intent.getStringExtra(INTENT_FILE_NAME)
        file = File(quperDirectory(), fileName)
        Picasso.with(this).load(file).into(imageView)

        //showAd()
        adView.loadAd(AdRequest.Builder().build())
    }

    fun shareImage(v: View) {

        MediaScannerConnection.scanFile(
                this,
                arrayOf(file!!.absolutePath),
                null,
                object: MediaScannerConnection.OnScanCompletedListener {
                    override fun onScanCompleted(path: String?, uri: Uri?) {
                        Log.d("scanfile", "$path $uri")

                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_STREAM, uri)
                        startActivity(Intent.createChooser(intent, "Share using .."))
                    }
                }
        )
    }

    fun set(v: View) {
        setWallpaper(this, BitmapFactory.decodeFile(file?.absolutePath))
    }
    companion object {
        val INTENT_FILE_NAME = "file_name"
    }
}
