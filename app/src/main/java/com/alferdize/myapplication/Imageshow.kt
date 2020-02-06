package com.alferdize.myapplication

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.provider.MediaStore
import android.graphics.BitmapFactory
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import android.content.ContextWrapper
import android.content.Intent
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import android.net.Uri
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.FileNotFoundException
import java.io.OutputStream
import java.lang.NullPointerException


class Imageshow : AppCompatActivity(){
    private val MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_view)

        val text=intent.getStringExtra("Sholkas")
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.daat)
        var bitmapConfig = bitmap.config
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }

        val largeIcon = bitmap.copy(bitmapConfig, true);

        val canvas = Canvas(largeIcon)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.rgb(255,255,255)
        val scaledSizeInPixels = resources.getDimensionPixelSize(R.dimen.image).toFloat()
        paint.setTextSize(scaledSizeInPixels)
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create("casual", Typeface.BOLD));
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = largeIcon.width/2
        var y = largeIcon.height/2.5
        for (line in text.split("\n")) {
            canvas.drawText(line, x.toFloat(), y.toFloat(), paint)
            y += paint.descent().toInt() - paint.ascent().toInt()
        }
        val image_view = findViewById(R.id.imageView) as ImageView
        image_view.setImageBitmap(largeIcon)
        val button = findViewById(R.id.download) as Button
        val share = findViewById(R.id.share) as Button


        button.setOnClickListener{
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Log.d("Done","Given")
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_STORAGE)
                    }
                }
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {


                val title = "Buddha Thought"
                val wrapper = ContextWrapper(applicationContext)
                var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
                val timeStamp = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())).toString();
                file = File(file, timeStamp+".jpg");
                var stream: OutputStream? = null
                stream =  FileOutputStream(file);
                largeIcon.compress(Bitmap.CompressFormat.JPEG,100,stream);
                stream.flush();
                stream.close();
                val savedImageURI = Uri.parse(file.getAbsolutePath());
                Toast.makeText(this,savedImageURI.toString(), Toast.LENGTH_SHORT).show()
                val savedImageURL = MediaStore.Images.Media.insertImage(
                        contentResolver,
                        largeIcon,
                        title,
                        "Image of $title"
                    )
                Log.d("Path",savedImageURL)
                val intent =  Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(savedImageURL.toString()), "image/*");
                startActivity(intent);

            }

        }
        share.setOnClickListener{
            try{
                val title = "Buddha Thought"
                val wrapper = ContextWrapper(applicationContext)
                var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
                val timeStamp = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())).toString();

                file = File(file, timeStamp+".jpg");
                largeIcon.compress(Bitmap.CompressFormat.JPEG,100,FileOutputStream(file));
                val url = FileProvider.getUriForFile(this, this.packageName, file);
                if (url != null) {

                    val intent = Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                    intent.setDataAndType(url, getContentResolver().getType(url));
                    intent.putExtra(Intent.EXTRA_STREAM, url);
                    startActivity(Intent.createChooser(intent, "Choose an app"));

                }

            }catch (e: NullPointerException) {}







        }
    }





}