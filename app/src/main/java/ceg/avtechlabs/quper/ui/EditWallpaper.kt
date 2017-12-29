package ceg.avtechlabs.quper.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.View
import android.widget.TextView
import android.widget.Toast
import ceg.avtechlabs.quper.R
import ceg.avtechlabs.quper.utils.changeFont
import ceg.avtechlabs.quper.utils.getFullPath
import kotlinx.android.synthetic.main.activity_edit_wallpaper.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan
import uk.co.chrisjenx.calligraphy.TypefaceUtils
import java.io.FileOutputStream
import java.util.*

class EditWallpaper : AppCompatActivity() {
    val fonts = arrayOf("fonts/roboto.ttf",
            "fonts/proxima_nova.ttf",
            "fonts/comic_sans.ttf",
            "fonts/times_new_roman.ttf")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wallpaper)

        @TargetApi(21)
        window.statusBarColor = Color.TRANSPARENT

        changeBackground()
    }

    fun changeBackground() {
        val colors = resources.getIntArray(R.array.colorsArray)
        val color = colors[Random().nextInt(colors.size)]
        window.decorView.setBackgroundColor(color)
    }

    fun changeColor(v: View) {
        changeBackground()
    }

    fun changeTypeface(v: View) {
        textView_editQuote.changeFont(this, fonts)
    }

    fun hideButtons() {
        button_changeBackground.visibility = View.INVISIBLE
        button_changeTypeface.visibility = View.INVISIBLE
        button_saveQuote.visibility = View.INVISIBLE
    }

    fun showButtons() {
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