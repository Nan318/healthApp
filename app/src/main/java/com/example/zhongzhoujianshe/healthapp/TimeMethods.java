package com.example.zhongzhoujianshe.healthapp;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.webkit.DateSorter;

import org.threeten.bp.LocalDate;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeMethods {

    private static DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat timeFormat2 = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
    private static Calendar c = Calendar.getInstance();

    public static String getDateToday(){
        Date date = new Date();
        return dateFormat.format(date);
        //Log.e("today","----"+DateToday);
    }
    public static String getDateStringForDb(Date date){
        return dateFormat.format(date);
    }

    public static String getTimeStringForDb(Date date){
        return timeFormat.format(date);
    }
    public static String getTimeStringForTxt(Date date){
        return timeFormat2.format(date);
    }


    public static String[] getDateLastWeek(){
        String[] week = new String[2];
        Date date = new Date();
        //get last month
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        week[0] = dateFormat.format(start);
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();
        week[1] = dateFormat.format(end);

        return week;
    }

    public static String[] getDateLastMonth(){
        String[] month = new String[2];
        //get last month
        c.set(Calendar.DATE, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDateOfPreviousMonth = c.getTime();
        month[1] = dateFormat.format(lastDateOfPreviousMonth);
        c.set(Calendar.DATE, 1);
        Date firstDateOfPreviousMonth = c.getTime();
        month[0] = dateFormat.format(firstDateOfPreviousMonth);

        return month;
    }

    //transfer date or time format
    //from: format "yyyy-MM-dd-HH-mm" or yyyy-MM-dd
    //to: format: 11 Oct (e.g.)
    public static String[] getXAxisText(ArrayList<String> dateUnion, int length, boolean time){
        String[] xAxis = new String[length];
        Log.e("getXAxisText", "EMPTY:" + dateUnion.isEmpty());

        if(length != 0){
            DateFormat newformat = new SimpleDateFormat("dd MMM");
            DateFormat oldformat;
            if (time){  //indicate this is a time
                oldformat = timeFormat;
            }else{  //this is a date
                oldformat = dateFormat;
            }

            for (int i = 0; i < length; i++){
                String dateStr = dateUnion.get(i);
                try {
                    Date date = oldformat.parse(dateStr);
                    xAxis[i] = newformat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        return xAxis;
    }


    //every day
    public static List<String> getEveryLabel(ArrayList<String> dateUnion, int length, boolean time){
        List<String> xAxis = new ArrayList<>();
        Log.e("getXAxisText", "EMPTY:" + dateUnion.isEmpty());



        if(length != 0){
//            xAxis.add("");
            DateFormat newformat = new SimpleDateFormat("dd MMM");
            DateFormat oldformat;
            if (time){  //indicate this is a time
                oldformat = timeFormat;
            }else{  //this is a date
                oldformat = dateFormat;
            }
            Date dateStart = null;
            Date dateEnd = null;
            try {
                dateStart = oldformat.parse(dateUnion.get(0));
                dateEnd = oldformat.parse(dateUnion.get(length-1));
                xAxis.add(newformat.format(dateStart));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);
            while(cal.getTime().compareTo(dateEnd)<=0){
                xAxis.add(dateFormat.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_MONTH,1);
            }

        }

        return xAxis;
    }


    //get the date range
    //in format: "16 Apr 2018 - 18 Oct 2018"
    public static String getDateRange(ArrayList<String> dateUnion, int size, boolean time){
         Log.e("getDateRange", "EMPTY:" + dateUnion.isEmpty());
         String rangeStr = "";
        if(size != 0){
            DateFormat newformat = new SimpleDateFormat("dd MMM yyyy");
            DateFormat oldformat;
            if (time){  //indicate this is a time
                oldformat = timeFormat;
            }else{  //this is a date
                oldformat = dateFormat;
            }

            String firstDate = dateUnion.get(0);
            String lastDate = dateUnion.get(size-1);
            try {
                Date first = oldformat.parse(firstDate);
                Date last = oldformat.parse(lastDate);
                rangeStr = "(" + newformat.format(first) + " - "
                        + newformat.format(last) + ")";
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }

        return rangeStr;
    }



    //can't call this method if dateUnion.size() is 0
    //otherwise, error may occur
    public static int[] getDayDuration(ArrayList<String> dateUnion, int length){
        int[] dateIds = new int[length];
        Log.e("getDayDuration", "EMPTY:" + dateUnion.isEmpty());

        if(length != 0){
            if (length == 1){
                dateIds[0] = 1;
            }else if (length > 1){
                dateIds[0] = 0;
                String date1 = dateUnion.get(0);
                //calculate from the second date
                for (int i = 1; i < length; i++){
                    String date2 = dateUnion.get(i);
                    try{
                        Date previousDate = dateFormat.parse(date1);
                        Date currentDate = dateFormat.parse(date2);
                        //Note that the different is in milliseconds,
                        // it needs to be divided by 1000 to get the number of seconds.
                        long difference = currentDate.getTime() - previousDate.getTime();   //in miliseconds
                        long durationInSec = difference/1000;   //in seconds
                        long durationInMinus = durationInSec/60;  //in minus
                        long durationInDay = (long) durationInMinus/1440;  //in days

                        dateIds[i] = (int) durationInDay;
                        //Toast.makeText(getApplicationContext() , dateIds[i] , Toast.LENGTH_SHORT).show();
                        Log.e("GET", "getDayDuration:" + dateIds[i]);


                    }catch (java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return dateIds;
    }
    public int[] getMinsDuration(ArrayList<String> dateUnion, int length){
        int[] dateIds = new int[length];
        Log.e("getMinsDuration", "EMPTY:" + dateUnion.isEmpty());

        if(length != 0){
            if (length == 1){
                dateIds[0] = 1;
            }else if (length > 1){
                dateIds[0] = 0;
                String time1 = dateUnion.get(0);
                //calculate from the second date
                for (int i = 1; i < length; i++){
                    String time2 = dateUnion.get(i);
                    try{
                        Date previousDate = timeFormat.parse(time1);
                        Date currentDate = timeFormat.parse(time2);
                        //Note that the different is in milliseconds,
                        // it needs to be divided by 1000 to get the number of seconds.
                        long difference = currentDate.getTime() - previousDate.getTime();   //in miliseconds
                        long durationInSec = difference/1000;   //in seconds
                        long durationInMinus = durationInSec/60;  //in minus
                        dateIds[i] = (int) durationInMinus;
                        //Toast.makeText(getApplicationContext() , dateIds[i] , Toast.LENGTH_SHORT).show();
                        Log.e("GET", "getMinsDuration:" + dateIds[i]);


                    }catch (java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return dateIds;
    }

}
