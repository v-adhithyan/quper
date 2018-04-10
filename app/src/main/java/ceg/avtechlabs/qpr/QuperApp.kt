package ceg.avtechlabs.qpr

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Adhithyan V on 27-12-2017.
 */
class QuperApp(): Application() {

    override fun onCreate() {
        super.onCreate()

        val calligraphy = CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/roboto.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()

        CalligraphyConfig.initDefault(calligraphy)
    }
}