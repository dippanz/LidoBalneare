package com.example.lidobalneare

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

class CalendarFragment: Fragment(R.layout.calendar_nested) {

    private lateinit var calendarView: MaterialCalendarView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calendarView = view.findViewById(R.id.calendarView1)

        val today = CalendarDay.today()
        //la data massima sarà 6 mesi da oggi
        val maxDate = CalendarDay.from(today.year, today.month + 5, today.day)

        calendarView.state().edit().setMinimumDate(today).setMaximumDate(maxDate).commit()

        var firstDate: CalendarDay? = null
        var endDate: CalendarDay? = null
        var lastFirstDate: CalendarDay? = null


        //setta graficamente le date selezionate
        calendarView.setOnDateChangedListener { _, date, selected ->

            if(selected){
                if(firstDate == null || date.isBefore(firstDate!!)){
                    calendarView.removeDecorators()
                    firstDate = date
                    endDate = null
                    setDecoration(firstDate, null, R.drawable.background_day_selected)


                }else if(endDate == null && firstDate!!.isBefore(date)){
                    endDate = date
                    setDecoration(firstDate, null, R.drawable.background_day_selected_left)
                    setDecoration(null, endDate, R.drawable.background_day_selected_right)
                    //qui i valori non sono nulli
                    setDecoration(nextDay(firstDate!!), beferoDay(endDate!!), R.drawable.background_day_selected_center)
                    lastFirstDate = firstDate
                    firstDate = null
                }
            }
        }

        view.findViewById<Button>(R.id.selezionaDateButton).setOnClickListener {
            if(firstDate != null && endDate == null){
                activity?.findViewById<TextView>(R.id.textDate)?.text =
                    getString(R.string.formato_date_singologiorno, getDayOfWeek(firstDate!!),
                        firstDate!!.day.toString(), getMonth(firstDate!!))
            }else if(lastFirstDate != null){
                //imposta il range nel text view usando una stringa come formato predefinito -> lun 11 giu - mar 12 giu
                activity?.findViewById<TextView>(R.id.textDate)?.text =
                    getString(R.string.formato_date, getDayOfWeek(lastFirstDate!!),
                        lastFirstDate!!.day.toString(), getMonth(lastFirstDate!!), getDayOfWeek(endDate!!), endDate!!.day.toString(), getMonth(endDate!!) )

            }

            ( activity as? MainPrenotazione)?.getBottomSheet()?.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }
    private fun getMonth(calendarDay: CalendarDay): String {
        val calendar = Calendar.getInstance()
        calendar.set(calendarDay.year, calendarDay.month, calendarDay.day)
        val dayOfWeekString = when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> "gen"
            Calendar.FEBRUARY -> "feb"
            Calendar.MARCH -> "mar"
            Calendar.APRIL -> "apr"
            Calendar.MAY -> "mag"
            Calendar.JUNE -> "giu"
            Calendar.JULY -> "lug"
            Calendar.AUGUST -> "ago"
            Calendar.SEPTEMBER -> "set"
            Calendar.OCTOBER -> "ott"
            Calendar.NOVEMBER -> "nov"
            Calendar.DECEMBER -> "dic"
            else -> "_"
        }
        return dayOfWeekString
    }

    private fun getDayOfWeek(calendarDay: CalendarDay): String {
        val calendar = Calendar.getInstance()
        calendar.set(calendarDay.year, calendarDay.month, calendarDay.day)
        val dayOfWeekString = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "dom"
            Calendar.MONDAY -> "lun"
            Calendar.TUESDAY -> "mar"
            Calendar.WEDNESDAY -> "mer"
            Calendar.THURSDAY -> "gio"
            Calendar.FRIDAY -> "ven"
            Calendar.SATURDAY -> "sab"
            else -> "_"
        }
        return dayOfWeekString
    }

    private fun beferoDay(date: CalendarDay): CalendarDay {
        return CalendarDay.from(date.year, date.month, date.day - 1)
    }

    private fun setDecoration(startDay: CalendarDay?,endDay: CalendarDay?, @DrawableRes drawableId: Int){
        calendarView.addDecorator(MyDayDecorator(requireContext(), startDay, endDay, drawableId))
    }

    private fun nextDay(date: CalendarDay): CalendarDay{
        return CalendarDay.from(date.year, date.month, date.day + 1)
    }




}