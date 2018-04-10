package ceg.avtechlabs.qpr.utils

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.EditText
import android.widget.TextView
import ceg.avtechlabs.qpr.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan
import uk.co.chrisjenx.calligraphy.TypefaceUtils
import java.io.File

/**
 * Created by Adhithyan V on 25-12-2017.
 */

fun Context.quperDirectory(): File {
    return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Globals.DIRECTORY)
}

fun Context.checkAndMakeQuperDirectory(): String {
    val rootDir = quperDirectory()

    if(!rootDir.exists()) {
        //Toast.makeText(this, "dir not exists", Toast.LENGTH_LONG).show()
        if(rootDir.mkdirs()) {
            //Toast.makeText(this, "dir created", Toast.LENGTH_LONG).show()
        }
    }

    return rootDir.toString()
}

fun Context.listQuperDirectory(): Array<out String>? {
    val dir = quperDirectory()
    if(dir.exists()) {
        return dir.list()
    }

    return null
}

fun Context.getFullPath(fileName: String): String {
    val dir = checkAndMakeQuperDirectory()
    return File(dir, "$fileName.png").toString()
}

fun Activity.permissionGranted() : Boolean{
    return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED

}

fun TextView.changeFont(context: Context, fonts: Array<String>, i: Int) {
    val builder = SpannableStringBuilder()
    val content = this.text.toString()
    builder.append(content)

    val font = fonts[i % fonts.size]

    val typefaceSpan = CalligraphyTypefaceSpan(TypefaceUtils.load(context.assets, font))
    builder.setSpan(typefaceSpan, 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.setText(builder, TextView.BufferType.SPANNABLE)
    this.typeface = TypefaceUtils.load(context.assets, font)
}

fun EditText.changeFont(context: Context, fonts: Array<String>) {
    val builder = android.text.SpannableStringBuilder()
    val content = this.text.toString()
    builder.append(content)

    val index = java.util.Random().nextInt(fonts.size)
    val typefaceSpan = uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan(uk.co.chrisjenx.calligraphy.TypefaceUtils.load(context.assets, fonts[index]))
    builder.setSpan(typefaceSpan, 0, content.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.setText(builder, android.widget.TextView.BufferType.SPANNABLE)
    this.typeface = TypefaceUtils.load(context.assets, fonts[index])
}

fun Context.showInterstitialAd() {
    val ad = InterstitialAd(this)
    ad.adUnitId = getString(R.string.admob_inter)

    val request = AdRequest.Builder()
    ad.loadAd(request.build())
    ad.adListener = object: AdListener() {
        override fun onAdLoaded() {
            if(ad.isLoaded) {
                ad.show()
            }
        }
    }
}

fun Context.showAd() {
    Handler().postDelayed({
        showInterstitialAd()
            }, 15000)
}


fun Context.setWallpaper(context: Context, bitmap: Bitmap) {
    val builder = AlertDialog.Builder(this)
            .setTitle("Set wallpaper")
            .setMessage("Do you want this to set as wallpaper?")
            .setPositiveButton("Yes", object: DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface, which: Int) {
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    wallpaperManager.setBitmap(bitmap)
                }
            })
            .setNegativeButton("No", null)
            .setIcon(android.R.drawable.ic_dialog_info)
            .create()

    builder.show()
}