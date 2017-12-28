package ceg.avtechlabs.quper.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.View
import android.widget.TextView
import ceg.avtechlabs.quper.R
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

        textView_editQuote.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        textView_editQuote.getBackground().clearColorFilter()

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
        val builder = SpannableStringBuilder()
        val content = textView_editQuote.text.toString()
        builder.append(content)

        val index = Random().nextInt(fonts.size)
        val typefaceSpan = CalligraphyTypefaceSpan(TypefaceUtils.load(assets, fonts[index]))
        builder.setSpan(typefaceSpan, 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView_editQuote.setText(builder, TextView.BufferType.SPANNABLE)
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
        val view = window.decorView
        hideButtons()
        view.isDrawingCacheEnabled = true
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0,0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(view.drawingCache)

        val fileName = getFullPath(Date().toString())
        try {
            val out = FileOutputStream(fileName)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            view.isDrawingCacheEnabled = false
        }

        showButtons()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
