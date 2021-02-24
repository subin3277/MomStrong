/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.capston_design;

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class CameraActivity : AppCompatActivity() {

  private var drawerLayout: DrawerLayout? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.tfe_pn_activity_camera)
    savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.container, PosenetActivity())
            .commit()

    val toolbar = findViewById<View>(R.id.camera_toolbar) as Toolbar
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar
    actionBar!!.setDisplayShowTitleEnabled(false)
    actionBar!!.setDisplayHomeAsUpEnabled(true)
    actionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

    drawerLayout = findViewById<View>(R.id.camera_drawer_layout) as DrawerLayout

    val navigationView = findViewById<View>(R.id.camera_nav_view) as NavigationView
    navigationView.setNavigationItemSelectedListener { item ->
      item.isChecked = true
      drawerLayout!!.closeDrawers()
      val id = item.itemId
      val title = item.title.toString()
      if (id == R.id.tab_info) {
        val intent = Intent(this@CameraActivity, InformationActivity::class.java)
        startActivity(intent)
        finish()
      } else if (id == R.id.tab_eat) {
        val intent = Intent(this@CameraActivity, DietActivity::class.java)
        startActivity(intent)
        finish()
      } else if (id == R.id.tab_yoga) {
        val intent = Intent(this@CameraActivity, YogaActivity::class.java)
        startActivity(intent)
        finish()
      } else if (id == R.id.tab_hos) {
        val intent = Intent(this@CameraActivity, HospitalActivity::class.java)
        startActivity(intent)
        finish()
      }
      true
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        drawerLayout!!.openDrawer(GravityCompat.START)
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }
}