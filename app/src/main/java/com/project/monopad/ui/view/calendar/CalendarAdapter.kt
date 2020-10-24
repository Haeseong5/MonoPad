package com.project.monopad.ui.view.calendar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.monopad.R
import com.project.monopad.model.entity.Day
import com.project.monopad.model.entity.Review
import com.project.monopad.util.CalendarUtil.calendarToString
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.calendar_item_layout.view.*
import java.util.*

class CalendarAdapter(context: Context) : BaseAdapter() {

    private val SIZE_OF_DAY = 7*6
    private val dayList = mutableListOf<Day>()

    private var mContext : Context = context
    private var calendar = Calendar.getInstance()
    private var month = Calendar.getInstance().get(Calendar.MONTH)
    private var day = Calendar.getInstance().get(Calendar.DATE)

    private val reviewList = mutableListOf<Review>()

    fun setReview(list : List<Review>) {
        reviewList.apply {
            clear()
            addAll(list)
        }
    }

    init {
        setCalendar()
    }

    fun updateCalendar(cal: Calendar){
        this.calendar = cal
        setCalendar()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, p2: ViewGroup?): View {
        val layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView = view ?: layoutInflater.inflate(R.layout.calendar_item_layout, null)

        val day: Day = getItem(position) as Day

        val itemYear = day.year
        val itemMonth = day.month
        val itemDay = day.day

        calendar.set(itemYear, itemMonth, itemDay,0,0,0)

        mView.day_tv.apply {
            text = itemDay.toString()
            when(position%7) {
                0 -> setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                6 -> setTextColor(ContextCompat.getColor(context, R.color.colorBlue))
                else -> setTextColor(Color.WHITE)
            }
        }

        val alphaValue = if(itemMonth!=month) 0.4F else 1.0F
        mView.day_tv.alpha = alphaValue
        mView.poster_iv.alpha = alphaValue

        if(day.reviews.isNotEmpty()){
            mView.poster_iv.visibility = View.VISIBLE

            Glide.with(mView.context)
                .load(day.reviews.get(0).review_poster)
                .into(mView.poster_iv)
        }
        else {
            mView.poster_iv.visibility = View.GONE
        }

        return mView
    }

    override fun getItem(position: Int): Any = dayList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dayList.size

    private fun setCalendar(){
        dayList.clear()

        month = calendar.get(Calendar.MONTH)

        var cal = calendar
        cal.set(Calendar.DATE, 1)
        var startOfMonth = cal.get(Calendar.DAY_OF_WEEK) - 1
        cal.add(Calendar.DATE, -startOfMonth)

        while (dayList.size < SIZE_OF_DAY) {
            val it = reviewList.iterator()
            val reviews = mutableListOf<Review>()
            while(it.hasNext()){
                var item = it.next()
                if(item.date==calendarToString(cal, "yyyy/MM/dd")){
                    reviews.add(item)
                }
            }
            dayList.add(Day(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),reviews))
            cal.add(Calendar.DATE, 1);
        }
    }
}