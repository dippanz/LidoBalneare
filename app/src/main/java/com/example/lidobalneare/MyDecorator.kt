package com.example.lidobalneare

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade

class MyDayDecorator(private val context: Context, private val startDay: CalendarDay?, private val endDay: CalendarDay?, @DrawableRes private val drawableId: Int) :
    com.prolificinteractive.materialcalendarview.DayViewDecorator {


    override fun shouldDecorate(day: CalendarDay): Boolean {

        return if(startDay == null && endDay == null){
            false
        }
        else if(startDay == null){
            day == endDay
        }
        else if(endDay == null){
            day == startDay
        }else{
            (day.isInRange(startDay, endDay) )
        }


    }

    override fun decorate(view: DayViewFacade) {
            // apply drawable to dayView
            view.setSelectionDrawable((AppCompatResources.getDrawable(context, drawableId)!!))
            // setto il colore con uno span per evidenziarlo rispetto allo sfondo
            view.addSpan( ForegroundColorSpan(Color.WHITE))

    }

}