/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import java.util.ArrayList
import java.text.SimpleDateFormat
import java.util.Date

class EarthquakeActivity : AppCompatActivity() {

    lateinit var earthquake_list : RecyclerView
    var earthquakes:ArrayList<Earthquake> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        fun loadData() {
//            earthquakes.add(Earthquake("7.2", "San Francisco", "Feb 1, 2016"))
//            earthquakes.add(Earthquake("6.1", "London", "Feb 2, 2016"))
//            earthquakes.add(Earthquake("3.9", "Tokyo", "Feb 3, 2016"))
//            earthquakes.add(Earthquake("5.4", "Mexico City", "Feb 4, 2016"))
//            earthquakes.add(Earthquake("2.8", "San Francisco2", "Feb 5, 2016"))
//            earthquakes.add(Earthquake("4.9", "San Francisco3", "Feb 6, 2016"))
//            earthquakes.add(Earthquake("1.6", "San Francisco4", "Feb 7, 2016"))

            earthquakes = QueryUtils.extractEarthquakes()
        }

        loadData()

        earthquake_list = findViewById(R.id.rv_earthquake_list) as RecyclerView
        earthquake_list.layoutManager = LinearLayoutManager(this)
//        country_list.layoutManager = GridLayoutManager(this,2)
        earthquake_list.adapter = EarthquakeAdapter(earthquakes, this)


    }

}
