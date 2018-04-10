package ceg.avtechlabs.qpr.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import ceg.avtechlabs.qpr.R
import ceg.avtechlabs.qpr.utils.quperDirectory
import com.squareup.picasso.Picasso
import java.io.File

/**
 * Created by Adhithyan V on 14-02-2018.
 */

class GridAdapter internal constructor(internal val context: Context, internal val layoutResourceId: Int,
                  internal var data: Array<out String>?): ArrayAdapter<String>(context, layoutResourceId, data) {

    private fun getBitmapFromFilePath(path: String): Bitmap? {
        val file = File(context.quperDirectory(), path)

        if (file.exists()) {
            Log.d("setimage", "true")
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            return bitmap
        } else {
            Log.d("setimage", "false")

        }

        Log.d("setimage", path)

        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        var holder: ViewHolder? = null


            val inflater = (context as Activity).layoutInflater
            row = inflater.inflate(layoutResourceId, parent, false)
            holder = ViewHolder()
            holder.image = row!!.findViewById<View>(R.id.image) as ImageView


        val path = data!![position]
        //getBitmapFromFilePath(path)
        val file = File(context.quperDirectory(), path)
        Picasso.with(context).load(file).into(holder.image)
        holder.image?.tag = path
        return row
    }

    internal class ViewHolder {
        var image: ImageView? = null
    }
}
