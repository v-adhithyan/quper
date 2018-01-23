package ceg.avtechlabs.quper.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_wallpaper.*
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan
import uk.co.chrisjenx.calligraphy.TypefaceUtils
import java.io.File
import java.util.*

/**
 * Created by Adhithyan V on 25-12-2017.
 */

fun Context.checkAndMakeQuperDirectory(): String {
    val rootDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Globals.DIRECTORY)

    if(!rootDir.exists()) {
        Toast.makeText(this, "dir not exists", Toast.LENGTH_LONG).show()
        if(rootDir.mkdirs()) {
            Toast.makeText(this, "dir created", Toast.LENGTH_LONG).show()
        }
    }

    return rootDir.toString()
}

fun Context.getFullPath(fileName: String): String {
    val dir = checkAndMakeQuperDirectory()
    return File(dir, "$fileName.png").toString()
}

fun Activity.permissionGranted() : Boolean{
    return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED

}

fun TextView.changeFont(context: Context, fonts: Array<String>) {
    val builder = SpannableStringBuilder()
    val content = this.text.toString()
    builder.append(content)

    val index = Random().nextInt(fonts.size)

    val typefaceSpan = CalligraphyTypefaceSpan(TypefaceUtils.load(context.assets, fonts[index]))
    builder.setSpan(typefaceSpan, 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.setText(builder, TextView.BufferType.SPANNABLE)
    this.typeface = TypefaceUtils.load(context.assets, fonts[index])
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