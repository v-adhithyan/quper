package ceg.avtechlabs.quper.ui

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import ceg.avtechlabs.quper.R
import ceg.avtechlabs.quper.adapter.GridAdapter
import ceg.avtechlabs.quper.utils.*
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.grid_layout.*
import java.io.File

import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    val PERMISSIONS_WRITE_STORAGE = 300
    var adapter: GridAdapter? = null
    var toDelete = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!permissionGranted()) {
            requestPermission()
        }

        Log.d("datasize", "else")
        setContentView(R.layout.activity_main)
        notifyAdapterChange()
        gridView.choiceMode = GridView.CHOICE_MODE_MULTIPLE_MODAL

        gridView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                val image = view!!.findViewById<ImageView>(R.id.image)
                val fileName = image?.tag.toString()

                val intent = Intent(this@MainActivity, ViewActivity::class.java)
                intent.putExtra(ViewActivity.INTENT_FILE_NAME, fileName)
                startActivity(intent)
            }
        })



        gridView.setMultiChoiceModeListener(object: AbsListView.MultiChoiceModeListener {
            override fun onActionItemClicked(mode: ActionMode?, p1: MenuItem?): Boolean {
                val dialog = ProgressDialog(this@MainActivity)
                dialog.setMessage("Deleting files")
                dialog.show()
                dialog.setCancelable(false)
                val files = this@MainActivity.listQuperDirectory()
                for(key in toDelete.keys) {
                    if(toDelete[key] == true) {
                        val file = File(quperDirectory(), files!![key])
                        Log.d("longpress", file.absolutePath)
                        file.delete()
                    }
                }
                Log.d("longpress", files!!.size.toString())
                Log.d("longpress", listQuperDirectory()!!.size.toString())

                dialog.dismiss()
                Toast.makeText(this@MainActivity, "Delete success", Toast.LENGTH_LONG).show()
                notifyAdapterChange()
                mode?.finish()
                return false
            }

            override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int, id: Long, checked: Boolean) {
                val imageView = mode?.customView?.findViewById<ImageView>(R.id.image)

                if (toDelete.containsKey(position)) {
                    if(!checked){
                        toDelete.put(position, checked)
                        //imageView?.setBackgroundColor(Color.TRANSPARENT);
                    }
                } else {
                    //imageView?.
                    //imageView?.setBackgroundColor(Color.DKGRAY)
                    toDelete.put(position, checked)
                }


                mode?.title = "${gridView.checkedItemCount} selected"
            }

            override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                val inflater = menuInflater
                inflater.inflate(R.menu.context_menu, menu)
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                toDelete = mutableMapOf<Int, Boolean>()
                notifyAdapterChange()
            }

        })

        adMain.loadAd(AdRequest.Builder().build())
        //showAd()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_WRITE_STORAGE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Write permission granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Write permission declined", Toast.LENGTH_LONG).show()
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_WRITE_STORAGE)
    }

    fun createNewWallpaper(view: View) {
        startActivity(Intent(this, EditWallpaper::class.java))
    }

    override fun onResume() {
        super.onResume()
        notifyAdapterChange()
    }


    fun notifyAdapterChange() {
        val data = listQuperDirectory()

        if(data == null) {
            hideGridView()
        } else {
            if(data.size == 0 && data.isEmpty()) {
                hideGridView()
            } else {
                gridView.visibility = View.VISIBLE
                textNoData.visibility = View.INVISIBLE
                adapter?.notifyDataSetInvalidated()
                adapter = GridAdapter(this, R.layout.grid_layout, data)
                gridView.adapter = adapter
            }

        }

    }

    fun hideGridView() {
        gridView.visibility = View.INVISIBLE
        textNoData.visibility = View.VISIBLE
    }
}