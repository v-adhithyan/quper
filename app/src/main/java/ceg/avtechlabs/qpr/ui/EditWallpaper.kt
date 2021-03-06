package ceg.avtechlabs.qpr.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import ceg.avtechlabs.qpr.R
import ceg.avtechlabs.qpr.utils.changeFont
import ceg.avtechlabs.qpr.utils.getFullPath
import ceg.avtechlabs.qpr.utils.setWallpaper
import kotlinx.android.synthetic.main.activity_edit_wallpaper.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
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

    var whiteColor = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wallpaper)

        @TargetApi(21)
        window.statusBarColor = Color.TRANSPARENT

        changeBackground()
        //showAd()
    }

    fun changeBackground() {
        val colorCodes = arrayOf("cc", "ff", "66", "33", "99")
        val random1 = colorCodes[Random().nextInt(colorCodes.size)]
        val random2 = colorCodes[Random().nextInt(colorCodes.size)]
        val random3 = colorCodes[Random().nextInt(colorCodes.size)]

        val hexCode = "#$random1$random2$random3"
        window.decorView.setBackgroundColor(Color.parseColor(hexCode))
        Log.d("windowcolor", hexCode)
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

    fun changeFontColor(v: View) {
        val currentColor = textView_editQuote.textColors

        if(whiteColor) {
            whiteColor = false
            textView_editQuote.setTextColor(Color.BLACK)
            textView_editQuote.setHintTextColor(Color.BLACK)
        } else {
            whiteColor = true
            textView_editQuote.setTextColor(Color.WHITE)
            textView_editQuote.setHintTextColor(Color.WHITE)
        }
    }

    fun hideButtons() {
        textView_editQuote.isCursorVisible = false
        button_changeBackground.visibility = View.INVISIBLE
        button_changeTypeface.visibility = View.INVISIBLE
        button_changeFontColor.visibility = View.INVISIBLE
        button_saveQuote.visibility = View.INVISIBLE

    }

    fun showButtons() {
        textView_editQuote.isCursorVisible = true
        button_changeBackground.visibility = View.VISIBLE
        button_changeTypeface.visibility = View.VISIBLE
        button_changeFontColor.visibility = View.VISIBLE
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
        setWallpaper(this@EditWallpaper, bitmap)
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
