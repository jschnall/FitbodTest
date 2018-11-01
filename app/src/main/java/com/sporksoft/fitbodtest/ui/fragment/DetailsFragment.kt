package com.sporksoft.fitbodtest.ui.fragment


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.sporksoft.fitbodtest.model.Workout
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_details.*
import com.sporksoft.fitbodtest.R
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.sporksoft.fitbodtest.model.FeedItem
import kotlinx.android.synthetic.main.item_feed.*
import java.text.SimpleDateFormat
import java.util.*




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
public const val ARG_WORKOUTS = "workouts"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailsFragment : Fragment() {
    private var data: FeedItem? = null
    private val entries = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable(ARG_WORKOUTS)
            for (workout in data!!.workouts) {
                entries.add(Entry(workout.date.time.toFloat(), workout.weight.toFloat()))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        title.text = data!!.name
        maxWeight.text = data!!.max.toString()
        initChart()
    }

    private fun initChart() {
        val dataSet: LineDataSet = LineDataSet(entries, resources.getString(R.string.one_rep_max))
        dataSet.setColor(ContextCompat.getColor(activity!!, R.color.colorPrimary));
        dataSet.setValueTextColor(Color.WHITE);
        chart.xAxis.textColor = Color.WHITE
        chart.xAxis.labelRotationAngle = -75f
        chart.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        chart.xAxis.setValueFormatter(object : IAxisValueFormatter {
            val decimalDigits: Int
                get() = 0

            override fun getFormattedValue(value: Float, axis: AxisBase): String {
                val date = Date(value.toLong())
                val df = SimpleDateFormat("MM/d/yy")
                return df.format(date)
            }
        })

        chart.axisLeft.setValueFormatter(object : IAxisValueFormatter {
            val decimalDigits: Int
                get() = 0

            override fun getFormattedValue(value: Float, axis: AxisBase): String {
                return resources.getString(R.string.label_pounds, value.toInt())
            }
        })

        chart.axisLeft.textColor = Color.WHITE
        chart.legend.textColor = Color.WHITE

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(entries: ArrayList<Workout>) =
                DetailsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(ARG_WORKOUTS, entries)
                    }
                }
    }
}
