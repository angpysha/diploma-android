/*
 * Copyright 2018 Andrew Petrowsky
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.andrewpetrowski.raspiinfo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.toDate
import com.andrewpetrowski.raspiinfo.Helpers.toFormatedString
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_temperature.*
import kotlinx.android.synthetic.main.fragment_temperature.*
import kotlinx.android.synthetic.main.fragment_temperature.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TemperatureFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TemperatureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TemperatureFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
//    private lateinit var cur_date: Date

    //private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_temperature, container, false)


        return view
    }

   // @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val args = arguments
       val date = args!!.getString("DATE").toDate()
       LoadData().execute(date)

       sel_date_temp_but!!.setOnClickListener {
           val cal = Calendar.getInstance()
           cal.time = date
           val dialog = DatePickerDialog.newInstance(this@TemperatureFragment,
                   cal.get(Calendar.YEAR),
                   cal.get(Calendar.MONTH),
                   cal.get(Calendar.DAY_OF_MONTH))

           //dialog.show(fragmentManager,"dfdf")
       }

//        var list: MutableList<DataPoint> = ArrayList()
//        val temp_gragh = view!!.findViewById<GraphView>(R.id.temperature_graph)
////                var series = LineGraphSeries<DataPoint>()
//        data.mapTo(list) { DataPoint(it.created_at, it.temperature.toDouble()) }
//
//        var aas = list.toTypedArray()
//        var series = LineGraphSeries(aas)
        //       temperature_graph!!.removeAllSeries()
        //      temperature_graph!!.addSeries(series)
        //   temperature_graph!!.viewport.isScalable = true

    }

    // TODO: Rename method, update argument and hook method into UI event
    /*  fun onButtonPressed(uri: Uri) {
          if (mListener != null) {
              mListener!!.onFragmentInteraction(uri)
          }
      }*/

    /*  override fun onAttach(context: Context?) {
          super.onAttach(context)
          if (context is OnFragmentInteractionListener) {
              mListener = context
          } else {
              throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
          }
      }*/

    /* override fun onDetach() {
         super.onDetach()
         mListener = null
     }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    /* interface OnFragmentInteractionListener {
         // TODO: Update argument type and name
         fun onFragmentInteraction(uri: Uri)
     }*/

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

        // private val date : Date
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TemperatureFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(date: Date?): TemperatureFragment {
            val fragment = TemperatureFragment()
            val args = Bundle()
            args.putString("DATE", date!!.toFormatedString())
            fragment.arguments = args
            return fragment
        }
    }

    inner class LoadData : AsyncTask<Date, Void, List<DHT11_Data>>() {
        override fun doInBackground(vararg params: Date?): List<DHT11_Data> {
            val temperatureContorller = AndroidDHTController()

            val date: Date = params[0]!!.zeroTime()
            var data = temperatureContorller.GetByDate(date).sortedBy { it.created_at }
            return data
        }

        override fun onPostExecute(result: List<DHT11_Data>?) {
            super.onPostExecute(result)
            result!!.let {

                var list: MutableList<DataPoint> = ArrayList()
//                val temp_gragh = view!!.findViewById<GraphView>(R.id.temperature_graph)
//                var series = LineGraphSeries<DataPoint>()
                result.mapTo(list) { DataPoint(it.created_at, it.temperature.toDouble()) }

                var aas = list.toTypedArray()
                var series = LineGraphSeries(aas)
//                 temperature_header!!.text = "hey"

//                temperature_graph!!.removeAllSeries()
//                temperature_graph!!.addSeries(series)

//                temperature_graph!!.viewport.isScalable = true
                series.isDrawDataPoints = true
                temperature_graph!!.addSeries(series)
                temperature_graph!!.viewport.setMinX(aas!!.get(0)!!.x ?: 0.0)
//                if (resources.getInteger(R.integer.num_axis) < list.size - 1)
//                    temperature_graph!!.viewport.setMaxX(aas.get(resources.getInteger(R.integer.num_axis)).x ?: 1.0)
//                else
//                    temperature_graph!!.viewport.setMaxX(aas.get(list.size - 1).x ?: 1.0)
//                temperature_graph!!.viewport.setMinY(aas.minBy { it.y }!!.y - 0.5)
//                temperature_graph!!.viewport.setMaxY(aas.maxBy { it.y }!!.y + 0.5)
                temperature_graph!!.viewport.isXAxisBoundsManual = true
//                temperature_graph!!.viewport.isYAxisBoundsManual = true
                //temperature_graph!!.viewport.maxXAxisSize = 1.0
//                temperature_graph!!.gridLabelRenderer.numHorizontalLabels = resources.getInteger(R.integer.num_axis) + 1

//                temperature_graph!!.viewport.isScalable = true
//                temperature_graph!!.viewport.isScrollable = true
//
                val ddf = SimpleDateFormat("MM\\dd\\yyyy")
                temperature_header!!.text = String.format(resources
                        .getString(R.string.temperature_header), ddf.format(result.get(0).created_at))
                val sdf = DateAsXAxisLabelFormatter(context, SimpleDateFormat("HH:mm"))
                temperature_graph!!.gridLabelRenderer.labelFormatter = sdf
//
////                cur_date = result.get(0).created_at.zeroTime()
                swiperefresh_temperature!!.isRefreshing = false

            }

        }
    }
}// Required empty public constructor
