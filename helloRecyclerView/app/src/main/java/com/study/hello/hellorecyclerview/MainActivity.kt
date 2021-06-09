package com.study.hello.hellorecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val exampleList = generateDummyList(500)
        var mDataSet = Array<String>(20, {"0"})

        recycler_view.adapter = MyAdapter(mDataSet)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        button.setOnClickListener {
            Log.d("TAG",""+recycler_view )
        }
    }

    private fun generateDummyList(size: Int): List<ExampleItem> {
        val list = ArrayList<ExampleItem>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_launcher_background
                1 -> R.drawable.ic_launcher_foreground
                else -> R.drawable.ic_launcher_background
            }
            val item = ExampleItem(drawable, "Item $i", "Line 2")
            list += item
        }
        return list
    }

//    private fun generateDummyList2(size: Int): String {
//        val list = String[]
//        for (i in 0 until size) {
//            val drawable = when (i % 3) {
//                0 -> R.drawable.ic_launcher_background
//                1 -> R.drawable.ic_launcher_foreground
//                else -> R.drawable.ic_launcher_background
//            }
//            val item = ExampleItem(drawable, "Item $i", "Line 2")
//            list += item
//        }
//        return list
//    }
}