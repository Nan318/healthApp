package com.example.zhongzhoujianshe.healthapp;


import android.util.Log;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateFromStringToInt {

    private static DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    //can't call this method if dateUnion.size() is 0
    //otherwise, error may occur
    public static ArrayList<Integer> getDayDuration(ArrayList<String> dateUnion){
        ArrayList<Integer> dateIds = new ArrayList<>();
        if(dateUnion.size() != 0){
            if (dateIds.size() == 1){
                dateIds.set(0, 1);
            }else if (dateIds.size() > 1){
                dateIds.set(0, 1);
                //calculate from the second date
                for (int i = 1; i < dateUnion.size(); i++){
                    String date1 = dateUnion.get(i-1);
                    String date2 = dateUnion.get(i);
                    try{
                        Date previousDate = dateFormat.parse(date1);
                        Date currentDate = dateFormat.parse(date2);
                        //Note that the different is in milliseconds,
                        // it needs to be divided by 1000 to get the number of seconds.
                        long difference = currentDate.getTime() - previousDate.getTime();
                        long durationInSec = difference/1000;
                        long durationInDay = durationInSec/86400;
                        dateIds.set(i, (int) durationInDay);

                    }catch (java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }else {
            dateIds.set(0, -1);  //no duration actually

        }
        return dateIds;
    }
    public static ArrayList<Integer> getMinsDuration(ArrayList<String> dateUnion){
        ArrayList<Integer> dateIds = new ArrayList<>();
        Log.e("TAG", "getMinsDuration:" + dateUnion.size());
        if(dateUnion.size() != 0){
            if (dateUnion.size() == 1){
                dateIds.set(0, 1);
            }else if (dateUnion.size() > 1){
                dateIds.set(0, 1);
                //calculate from the second date
                for (int i = 1; i < dateUnion.size(); i++){
                    String time1 = dateUnion.get(i-1);
                    String time2 = dateUnion.get(i);
                    try{
                        Date previousDate = timeFormat.parse(time1);
                        Date currentDate = timeFormat.parse(time2);
                        //Note that the different is in milliseconds,
                        // it needs to be divided by 1000 to get the number of seconds.
                        long difference = currentDate.getTime() - previousDate.getTime();
                        long durationInSec = difference/1000;
                        long durationInMinus = durationInSec/60;
                        dateIds.set(i, (int) durationInMinus);

                    }catch (java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }else {
            dateIds.set(0, -1);
        }

        return dateIds;
    }

}
