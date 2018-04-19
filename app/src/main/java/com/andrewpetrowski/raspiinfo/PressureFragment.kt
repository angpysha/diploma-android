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

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.andrewpetrowski.raspiinfo.Models.PageDatePair
import java.text.FieldPosition
import com.andrewpetrowski.raspiinfo.Models.TaskParams
import io.github.angpysha.diploma_bridge.Models.Bmp180_Data
import kotlinx.android.synthetic.main.fragment_pressure.*
import java.util.*
import com.andrewpetrowski.raspiinfo.Controllers.AndroidBMPController
import com.andrewpetrowski.raspiinfo.Helpers.*
import com.andrewpetrowski.raspiinfo.Records.Temperature
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.orm.SugarRecord
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PressureFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PressureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PressureFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

//    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pressure, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val date = args!!.getString("DATE").toDate()
        val type = args!!.getString("TYPE").toInt()
        LoadData().execute(TaskParams(date, type))

        swiperefresh_pressure!!.setOnRefreshListener {
            LoadData().execute(TaskParams(date, type))
        }

        sel_date_pres_but!!.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.time = date

            val dialog = DatePickerDialog.newInstance(this@PressureFragment, cal)

            dialog.show(activity!!.fragmentManager, "Change humidity")
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
//        if (mListener != null) {
//            mListener!!.onFragmentInteraction(uri)
//        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            mListener = context
//        } else {
//            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
//        mListener = null
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     *
//     *
//     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
//     */
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PressureFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): PressureFragment {
            val fragment = PressureFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstace(position: Int, type: Int, size: Int): PressureFragment {
            val fragment = PressureFragment()
            val args = Bundle()
            val date = PageDatePair.GetDate(position, size, type)
            args.putString("DATE", date!!.toFormatedString())
            args.putString("TYPE", type.toString())
            fragment.arguments = args
            return fragment
        }
    }

    inner class LoadData() : AsyncTask<TaskParams, Void, List<Bmp180_Data>?>() {
        private lateinit var _date: Date
        private var type = 0
        override fun doInBackground(vararg params: TaskParams?): List<Bmp180_Data>? {
            val bmp = AndroidBMPController()

            val date: Date = params[0]!!.date!!.zeroTime()
            var data: List<Bmp180_Data>? = ArrayList()
            this.type = params[0]!!.type


            when (this.type) {
                0 -> {
                    data = bmp.GetByDate(date)
                }
                1 -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = date.fullTime()
                    val now = Date()
                    // calendar.set(Calendar.DAY_OF_MONTH,27)
//                    val calendar = GregorianCalendar
//                    if (calendar.firstDayOfWeek == Calendar.MONDAY) {
//                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
//                    } else {
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)

//                    }
                    if (calendar.time > now)
                        calendar.time = now.fullTime()
                    data = bmp.GetByDate(calendar.time, 1)
                }

                2 -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = date.fullTime()
                    val now = Date()

                    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

                    calendar.set(Calendar.DAY_OF_MONTH, maxDay)
                    if (calendar.time > now)
                        calendar.time = now.fullTime()
                    data = bmp.GetByDate(calendar.time, 2)
                }

                3 -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = date.fullTime()
                    val now = Date()

                    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)

                    calendar.set(Calendar.DAY_OF_YEAR, maxDay)
                    if (calendar.time > now)
                        calendar.time = now.fullTime()
                    data = bmp.GetByDate(calendar.time, 3)
                }
            }
            _date = date
            return data
        }

        override fun onPostExecute(result: List<Bmp180_Data>?) {
            super.onPostExecute(result)

                if ( result == null || result.isEmpty()) {
                    if (pressure_header == null || pressure_graph == null){
//                        swiperefresh_pressure!!.isRefreshing = false
//                        (activity as Pressure).progress.hide()
                        return
                    }

                    val ddf = SimpleDateFormat("MM\\dd\\yyyy")
                    pressure_header!!.text = String.format(resources
                            .getString(R.string.humidity_header), ddf.format(_date))
                    pressure_graph!!.title = "There is not data for this day"
                    swiperefresh_pressure!!.isRefreshing = false
                    (activity as Pressure).progress.hide()
                    return
                }

                when (type) {
                    0 -> {
                        var list: MutableList<DataPoint> = ArrayList()
//                val temp_gragh = view!!.findViewById<GraphView>(R.id.temperature_graph)
//                var series = LineGraphSeries<DataPoint>()
                        result!!.mapTo(list) { DataPoint(it.created_at, it.pressure.toDouble()) }

                        var aas = list.toTypedArray()
                        aas.sortBy { it.x }
                        var series = LineGraphSeries(aas)

                        if (series == null || pressure_graph == null) {
//                            swiperefresh_pressure!!.isRefreshing = false
//                            (activity as Pressure).progress.hide()
                            return
                        }
//                 temperature_header!!.text = "hey"

//                temperature_graph!!.removeAllSeries()
//                temperature_graph!!.addSeries(series)

//                temperature_graph!!.viewport.isScalable = true
                        series.isDrawDataPoints = true
                        pressure_graph!!.addSeries(series)
                        pressure_graph!!.viewport.setMinX(aas!!.get(0)!!.x ?: 0.0)
//                if (resources.getInteger(R.integer.num_axis) < list.size - 1)
//                    temperature_graph!!.viewport.setMaxX(aas.get(resources.getInteger(R.integer.num_axis)).x ?: 1.0)
//                else
//                    temperature_graph!!.viewport.setMaxX(aas.get(list.size - 1).x ?: 1.0)
                        pressure_graph!!.viewport.setMinY(aas.minBy { it.y }!!.y - 0.5)
                        pressure_graph!!.viewport.setMaxY(aas.maxBy { it.y }!!.y + 0.5)
                        pressure_graph!!.viewport.isXAxisBoundsManual = true
                        pressure_graph!!.viewport.isYAxisBoundsManual = true
                        //temperature_graph!!.viewport.maxXAxisSize = 1.0
//                temperature_graph!!.gridLabelRenderer.numHorizontalLabels = resources.getInteger(R.integer.num_axis) + 1

//                temperature_graph!!.viewport.isScalable = true
//                temperature_graph!!.viewport.isScrollable = true
//
                        val ddf = SimpleDateFormat("MM\\dd\\yyyy")
                        pressure_header!!.text = String.format(resources
                                .getString(R.string.humidity_header), ddf.format(_date))
                        val sdf = DateAsXAxisLabelFormatter(context, SimpleDateFormat("HH:mm"))
                        pressure_graph!!.gridLabelRenderer.labelFormatter = sdf
                        swiperefresh_pressure!!.isRefreshing = false
                        (activity as Pressure).progress.hide()
                    }

                    1 -> {
                        var list: MutableList<DataPoint> = ArrayList()
                        result!!.mapTo(list) { DataPoint(it.created_at, it.pressure.toDouble()) }
                        var aas = list.toTypedArray()
                        aas.sortBy { it.x }
                        var series = LineGraphSeries(aas)
                        if (series == null || pressure_graph == null || series.isEmpty) {
//                            swiperefresh_pressure!!.isRefreshing = false
//                            (activity as Pressure).progress.hide()
                            return
                        }
                        series.isDrawDataPoints = true
                        pressure_graph!!.removeAllSeries()
//                        temperature_graph!!.gridLabelRenderer.numHorizontalLabels = 5
//                        temperature_graph!!.gridLabelRenderer.horizontalAxisTitle = "Days of week"

                        pressure_graph!!.addSeries(series)
                        pressure_graph!!.viewport.setMinX(aas!!.get(0).x)
//                        temperature_graph!!.viewport.setMaxX(aas.maxBy {it.x}!!.x?:0.0)
                        //                       temperature_graph!!.viewport.setMinX(list!!.first()!!.x)
//                        temperature_graph!!.viewport.setMaxX(list.last().x)
                        //                       temperature_graph!!.viewport.setMinY(10.0)
                        //                       temperature_graph!!.viewport.setMaxY(30.0)
                        pressure_graph!!.viewport.isXAxisBoundsManual = true
                        //                       temperature_graph!!.viewport.isYAxisBoundsManual = true
                        //                       temperature_graph!!.gridLabelRenderer.numHorizontalLabels = 3
                        val labelDateFormat = DateAsXAxisLabelFormatter(context, SimpleDateFormat("dd\\MM"))
                        pressure_graph!!.gridLabelRenderer.labelFormatter = labelDateFormat
                        val header_str = Additionals.DateRange(result!!.last().created_at, result!!.first().created_at)
                        pressure_header!!.text = String.format(resources.getString(R.string.pressure_header), header_str)
                        swiperefresh_pressure!!.isRefreshing = false
                        (activity as Pressure).progress.hide()
                    }

                    2 -> {
                        var list: MutableList<DataPoint> = ArrayList()
                        result!!.mapTo(list) { DataPoint(it.created_at, it.pressure.toDouble()) }
                        var aas = list.toTypedArray()
                        aas.sortBy { it.x }
                        var series = LineGraphSeries(aas)
                        if (series == null || pressure_graph == null || series.isEmpty) {
//                            swiperefresh_pressure!!.isRefreshing = false
//                            (activity as Pressure).progress.hide()
                            return
                        }
                        series.isDrawDataPoints = true
                        pressure_graph!!.removeAllSeries()
                        pressure_graph!!.gridLabelRenderer.numHorizontalLabels = 3
//                        temperature_graph!!.gridLabelRenderer.horizontalAxisTitle = "Days of week"

                        pressure_graph!!.addSeries(series)
                        pressure_graph!!.viewport.setMinX(aas!!.get(0).x)
//                        temperature_graph!!.viewport.setMaxX(aas.maxBy {it.x}!!.x?:0.0)
                        //                       temperature_graph!!.viewport.setMinX(list!!.first()!!.x)
//                        temperature_graph!!.viewport.setMaxX(list.last().x)
                        //                       temperature_graph!!.viewport.setMinY(10.0)
                        //                       temperature_graph!!.viewport.setMaxY(30.0)
                        pressure_graph!!.viewport.isXAxisBoundsManual = true
                        //                       temperature_graph!!.viewport.isYAxisBoundsManual = true
                        //                       temperature_graph!!.gridLabelRenderer.numHorizontalLabels = 3
                        val labelDateFormat = DateAsXAxisLabelFormatter(context, SimpleDateFormat("dd\\MM"))
                        pressure_graph!!.gridLabelRenderer.labelFormatter = labelDateFormat
                        val header_str = Additionals.DateRange(result!!.last().created_at, result!!.first().created_at)
                        pressure_header!!.text = String.format(resources.getString(R.string.pressure_header), header_str)
                        swiperefresh_pressure!!.isRefreshing = false
                        (activity as Pressure).progress.hide()

                    }

                    3 -> {
                        var list: MutableList<DataPoint> = ArrayList()
                        result!!.mapTo(list) { DataPoint(it.created_at, it.pressure.toDouble()) }
                        var aas = list.toTypedArray()
                        aas.sortBy { it.x }
                        var series = LineGraphSeries(aas)
                        if (series == null || pressure_graph == null || series.isEmpty) {
//                            swiperefresh_pressure!!.isRefreshing = false
//                            (activity as Pressure).progress.hide()
                            return
                        }
                        series.isDrawDataPoints = true
                        pressure_graph!!.removeAllSeries()
                      //  pressure_graph!!.gridLabelRenderer.numHorizontalLabels = 3
//                        temperature_graph!!.gridLabelRenderer.horizontalAxisTitle = "Days of week"

                        pressure_graph!!.addSeries(series)
                        pressure_graph!!.viewport.setMinX(aas!!.get(0).x)
//                        temperature_graph!!.viewport.setMaxX(aas.maxBy {it.x}!!.x?:0.0)
                        //                       temperature_graph!!.viewport.setMinX(list!!.first()!!.x)
//                        temperature_graph!!.viewport.setMaxX(list.last().x)
                        //                       temperature_graph!!.viewport.setMinY(10.0)
                        //                       temperature_graph!!.viewport.setMaxY(30.0)
                        pressure_graph!!.viewport.isXAxisBoundsManual = true
                        //                       temperature_graph!!.viewport.isYAxisBoundsManual = true
                        //                       temperature_graph!!.gridLabelRenderer.numHorizontalLabels = 3
                        val labelDateFormat = DateAsXAxisLabelFormatter(context, SimpleDateFormat("MMM"))
                        pressure_graph!!.gridLabelRenderer.labelFormatter = labelDateFormat
                        val header_str = Additionals.DateRange(result!!.last().created_at, result!!.first().created_at)
                        pressure_header!!.text = String.format(resources.getString(R.string.pressure_header), header_str)

                        swiperefresh_pressure!!.isRefreshing = false
                        (activity as Pressure).progress.hide()
                    }
                }
//            swiperefresh_pressure!!.isRefreshing = false
//            (activity as Pressure).progress.hide()
        }

    }
}// Required empty public constructor
