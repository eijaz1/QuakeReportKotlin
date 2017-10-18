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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.util.ArrayList

class EarthquakeActivity : AppCompatActivity() {

    //initialize @earthquake_list with type recyclerView
    //iniialize @earthquakes with type ArrayList of Earthquake objects
    lateinit var earthquake_list : RecyclerView
    var earthquakes:ArrayList<Earthquake> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        //list of earthquakes pulled from JSON response
        earthquakes = QueryUtils.extractEarthquakes()

        //setting up RecyclerView to recycle earthquake_list_item in
        // rv_earthquake_list for each earthquake
        earthquake_list = findViewById(R.id.rv_earthquake_list) as RecyclerView
        earthquake_list.layoutManager = LinearLayoutManager(this)
        earthquake_list.adapter = EarthquakeAdapter(earthquakes, this)

    }

}
