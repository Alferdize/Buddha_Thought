package com.alferdize.myapplication

import android.Manifest
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import android.R.attr.name
import android.annotation.SuppressLint
import android.content.pm.ResolveInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1
    val users = ArrayList<Thought>()

    //adding some dummy data to the list

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        fetchjson()
        users.add(Thought("There is no fear for one\nwhose mind is not filled\nwith desires."))
        users.add(Thought("Work out your own salvation.\n Do not depend on others.\n"))
        users.add(Thought("If anything is worth doing,\n do it with all your heart."))
        users.add(Thought("A man is not called wise because he" +
                "\n talks and talks again; but if he\n is peaceful, loving and fearless" +
                " then he\n is in truth called wise."))
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
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
        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)



        //creating our adapter
        val adapter = ThoughtAdapter(users)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    fun fetchjson(){
        println("Data inport :: ")
        val url = "https://api.letsbuildthatapp.com/youtube/home_feed"

        val request = Request.Builder().url(url).build()

        var client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                val data = response.body.toString()
                println(data)
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed in execution")
            }
        })
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("WrongConstant")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                val recyclerView = findViewById(R.id.recyclerView) as RecyclerView

                //adding a layoutmanager
                recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)



                val adapter = ThoughtAdapter(users)

                //now adding the adapter to recyclerview
                recyclerView.adapter = adapter
            }
            R.id.nav_about -> {

            }

            R.id.nav_share -> {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    var shareMessage = "\nLet me recommend you this application\n\n"
                    shareMessage =
                        shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch (e: Exception) {

                }
            }
            R.id.nav_feedback -> {
                val Email = Intent(Intent.ACTION_SEND)
                Email.type = "text/email"
                Email.putExtra(Intent.EXTRA_EMAIL, arrayOf("alferdize.info@gmail.com"))
                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "")
                val pm = packageManager
                val matches = pm.queryIntentActivities(Email, 0)
                var best: ResolveInfo? = null
                for (info in matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info
                if (best != null)
                    Email.setClassName(best.activityInfo.packageName, best.activityInfo.name)
                startActivity(Email)

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
