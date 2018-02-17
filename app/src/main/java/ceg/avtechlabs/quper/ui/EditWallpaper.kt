package ceg.avtechlabs.quper.ui

import android.annotation.TargetApi
import android.app.WallpaperManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import ceg.avtechlabs.quper.R
import ceg.avtechlabs.quper.utils.changeFont
import ceg.avtechlabs.quper.utils.getFullPath
import ceg.avtechlabs.quper.utils.showAd
import kotlinx.android.synthetic.main.activity_edit_wallpaper.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan
import uk.co.chrisjenx.calligraphy.TypefaceUtils
import java.io.FileOutputStream
import java.util.*

class EditWallpaper : AppCompatActivity() {
    var i = 0
    val fonts = arrayOf(
            "fonts/comic_sans.ttf",
            "fonts/pacifio.ttf",
            "fonts/proxima_nova.ttf",
            "fonts/roboto.ttf",
            "fonts/times_new_roman.ttf")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wallpaper)

        @TargetApi(21)
        window.statusBarColor = Color.TRANSPARENT

        changeBackground()
        showAd()
    }

    fun changeBackground() {
        val colorCodes = arrayOf("cc", "ff", "66", "33", "99")
        val random1 = colorCodes[Random().nextInt(colorCodes.size)]
        val random2 = colorCodes[Random().nextInt(colorCodes.size)]
        val random3 = colorCodes[Random().nextInt(colorCodes.size)]

        val hexCode = "#$random1$random2$random3"
        window.decorView.setBackgroundColor(Color.parseColor(hexCode))
    }

    fun changeColor(v: View) {
        changeBackground()
    }

    fun changeTypeface(v: View) {
        textView_editQuote.changeFont(this, fonts, i)
        val cursorPosition = textView_editQuote.text.toString().length
        textView_editQuote.setSelection(cursorPosition)

        i = i + 1
    }

    fun hideButtons() {
        textView_editQuote.isCursorVisible = false
        button_changeBackground.visibility = View.INVISIBLE
        button_changeTypeface.visibility = View.INVISIBLE

        button_saveQuote.visibility = View.INVISIBLE
    }

    fun showButtons() {
        textView_editQuote.isCursorVisible = true
        button_changeBackground.visibility = View.VISIBLE
        button_changeTypeface.visibility = View.VISIBLE
        button_saveQuote.visibility = View.VISIBLE
    }

    fun save(v: View) {
        val view = window.decorView.rootView
        hideButtons()
        view.isDrawingCacheEnabled = true
        val size = getDimensions()
        //Toast.makeText(this, "${size.x}, ${size.y}", Toast.LENGTH_LONG).show()
        view.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(view.drawingCache)

        val fileName = getFullPath(Date().toString())
        try {
            val out = FileOutputStream(fileName)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(this, "File saved as $fileName", Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(this, "Failed. Try again :)", Toast.LENGTH_LONG).show()
        } finally {
            view.isDrawingCacheEnabled = false
        }

        showButtons()
        val builder = AlertDialog.Builder(this)
                .setTitle("Set wallpaper")
                .setMessage("Do you want this to set as wallpaper?")
                .setPositiveButton("Yes", object: DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, which: Int) {
                        val wallpaperManager = WallpaperManager.getInstance(this@EditWallpaper)
                        wallpaperManager.setBitmap(bitmap)
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .create()

        builder.show()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun getDimensions(): Point {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }
}
