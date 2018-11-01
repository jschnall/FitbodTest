package com.sporksoft.fitbodtest.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import androidx.navigation.Navigation
import com.sporksoft.fitbodtest.R
import com.sporksoft.fitbodtest.model.Workout
import com.sporksoft.fitbodtest.model.FeedItem
import com.sporksoft.fitbodtest.ui.adapter.FeedAdapter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_feed.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*


class FeedFragment : Fragment() {
    private val LOGTAG = FeedFragment::class.java.simpleName
    private val FILENAME = "workoutData.txt"

    private lateinit var layout: View
    private lateinit var adapter: FeedAdapter

    private var queryStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        fetchFeedItems()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = FeedAdapter(onClickListener = View.OnClickListener {
            Log.d(LOGTAG, "Clicked item " + it.tag)
            val item = adapter.items.get(it.tag as Int)
            val bundle: Bundle = Bundle()
            bundle.putParcelable(ARG_WORKOUTS, item)
            Navigation.findNavController(it).navigate(R.id.detailsFragment, bundle)
        })
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity)
                .colorResId(R.color.grey_light)
                .sizeResId(R.dimen.divider_height)
                .marginResId(
                    R.dimen.divider_left_margin,
                    R.dimen.divider_right_margin
                ).build())
    }

    fun buildEntry(line: String ): Workout {
        val tokens = line.split(",")
        val entry = Workout()

        entry.date = SimpleDateFormat("MMM d yyyy").parse(tokens[0])
        entry.name = tokens[1]
        entry.sets = tokens[2].toInt()
        entry.reps = tokens[3].toInt()
        entry.weight = tokens[4].toInt()

        if (entry.reps < 1) {
            entry.max = 0
        } else  if (entry.reps > 36) {
            // Epley formula
            entry.max = entry.weight * (1 + entry.reps / 30)
        } else {
            // Brzycki
            entry.max = entry.weight * (36 / (37 - entry.reps))
        }

        return entry
    }

    fun fetchFeedItems() {
        doAsync {
            val map = HashMap<String, ArrayList<Workout>>()

            val json_string = context?.resources!!.assets.open(FILENAME).bufferedReader().useLines { lines ->
                lines.forEach {
                    val entry = buildEntry(it)
                    val list: ArrayList<Workout>?
                    if (map.contains(entry.name)) {
                        list = map[entry.name]
                    } else {
                        list = arrayListOf()
                        map.put(entry.name, list)
                    }
                    list!!.add(entry)
                }
                val feedItems: MutableList<FeedItem> = mutableListOf()
                for (entry in map) {
                    val list: MutableList<Workout> = entry.value
                    var max: Int = 0
                    for (item in list) {
                        max = Math.max(max, item.max)
                    }
                    feedItems.add(FeedItem(entry.key, entry.value, max))
                }
                uiThread {
                    adapter.reset(feedItems.sortedBy { it.name })
                }
            }
        }
    }
}
