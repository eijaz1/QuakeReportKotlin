package com.example.android.quakereport

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.earthquake_list_item.view.*
import java.text.SimpleDateFormat
import java.util.Date
import java.text.DecimalFormat
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.support.v4.content.ContextCompat

/**
 * Created by eijazallibhai on 2017-09-10.
 */

class EarthquakeAdapter(items: ArrayList<Earthquake>, ctx: Context) : RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>(){

    var list = items
    var context = ctx

    //Get size of the ArrayList of earthquake objects
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        //call bindItems function from ViewHolder subclass
        holder?.bindItems(list[position])

        //find current earthquake in the list of earthquake objects
        var currentEarthquake = list[position]

        //format timeInmilliseconds from JSON Response into a readable date and time
        //display date and time in separate text views
        var dateObject = Date(currentEarthquake.timeInMilliseconds)

        var formattedDate = formatDate(dateObject)

        var formattedTime = formatTime(dateObject)

        holder?.itemView?.tv_date?.text = formattedDate

        holder?.itemView?.tv_time?.text = formattedTime

        //Separate location JSON response to put locationOffset and primary Location
        //in separate text views
        val locationSeparator = " of "
        var originalLocation = currentEarthquake.location
        var locationOffset = ""
        var primaryLocation = ""

        if (originalLocation.contains(locationSeparator)) {
            var parts = originalLocation.split(locationSeparator)
            locationOffset = parts[0] + locationSeparator
            primaryLocation = parts[1]
        } else {
            locationOffset = context.getString(R.string.near_the)
            primaryLocation = originalLocation
        }

        holder?.itemView?.tv_location_offset?.text = locationOffset
        holder?.itemView?.tv_primary_location?.text = primaryLocation

        //format magnitude to one decimal place
        var mag = currentEarthquake.magnitude

        var decimalFormatter = DecimalFormat("0.0")
        var formattedMagnitude = decimalFormatter.format(mag)

        holder?.itemView?.tv_magnitude?.text = formattedMagnitude

        //get color of the magnitude circle based on the magnitude of the earthquake
        fun getMagnitudeColor(mag: Double): Int {

            var magnitudeColorResourceId: Int

            var magnitudeFloor = Math.floor(mag)
            when (magnitudeFloor) {

                0.0, 1.0 -> magnitudeColorResourceId = R.color.magnitude1
                2.0 -> magnitudeColorResourceId = R.color.magnitude2
                3.0 -> magnitudeColorResourceId = R.color.magnitude3
                4.0 -> magnitudeColorResourceId = R.color.magnitude4
                5.0 -> magnitudeColorResourceId = R.color.magnitude5
                6.0 -> magnitudeColorResourceId = R.color.magnitude6
                7.0 -> magnitudeColorResourceId = R.color.magnitude7
                8.0 -> magnitudeColorResourceId = R.color.magnitude8
                9.0 -> magnitudeColorResourceId = R.color.magnitude9
                else -> magnitudeColorResourceId = R.color.magnitude10plus
            }

            return ContextCompat.getColor(context, magnitudeColorResourceId)

        }

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        val magnitudeCircle = holder?.itemView?.tv_magnitude?.background as GradientDrawable
        // Get the appropriate background color based on the current earthquake magnitude
        val magnitudeColor = getMagnitudeColor(mag)
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor)

    }

    //create ViewHolder for RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.earthquake_list_item,parent,false))
    }

    //Return formatted time string
    private fun formatDate(dateObject: Date): String {
        val dateFormat = SimpleDateFormat("LLL dd, yyyy")
        return dateFormat.format(dateObject)
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private fun formatTime(dateObject: Date): String {
        val timeFormat = SimpleDateFormat("h:mm a")
        return timeFormat.format(dateObject)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        //set website intent for each earthquake object
        fun bindItems(list: Earthquake) {

            itemView.setOnClickListener({
                val earthquakeUri = Uri.parse(list.url)

                val websiteIntent = Intent(Intent.ACTION_VIEW, earthquakeUri)

                itemView.context.startActivity(websiteIntent)
            })

        }



    }
}