package ceg.avtechlabs.quper.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import java.io.File

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