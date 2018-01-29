package com.example.applemacbookproretina.learningandroid1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException


val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
val REQUEST_IMAGE_CAPTURE = 1
private val REQUEST_EXTERNAL_STORAGE = 1
private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)


class MainActivity : AppCompatActivity() {

    var bitmap: Bitmap? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendMessage(view: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)

            bitmap = imageBitmap

    var permission = ActivityCompat.checkSelfPermission(this.applicationContext!!, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
            var executeImagga = ExecuteImaggaActiviti()
            val tagsList : List<String> = executeImagga.execute(bitmap).get()
            val tagLayout = findViewById<View>(R.id.tagLayout) as TagLayout
            val layoutInflater = layoutInflater
            var tag: String

            for (tag in tagsList) {
                val tagView = layoutInflater.inflate(R.layout.tag_layout, null, false)

                val tagTextView = tagView.findViewById<TextView>(R.id.tagTextView)
                tagTextView.text = tag
                tagLayout.addView(tagView)
            }

//            for (i in 0..20) {
//                tag = "#tag" + i
//
//            }


            var intent = packageManager.getLaunchIntentForPackage("com.instagram.android")
            if (intent != null) {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.`package` = "com.instagram.android"

                val path: String = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).toString() + "/FitnessGirl0.png"

                try {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, path, "I am Happy", "Share happy !")))
                } catch (e: FileNotFoundException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                shareIntent.type = "image/png"

                startActivity(shareIntent)
            } else {
                // bring user to the market to download the app.
                // or let them choose an app?
                intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse("market://details?id=" + "com.instagram.android")
                startActivity(intent)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}