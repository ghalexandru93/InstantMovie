package com.ghalexandru.instant_movie.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ghalexandru on 5/22/17.
 */

public class Util {

    public static final String KEY_LIST_IMAGE = "list_image";
    public static final String KEY_IMAGE_TYPE = "image_type";
    public static final String KEY_MEDIA_ID = "media_id";
    public static final String KEY_MEDIA_TYPE = "media_type";
    public static final String KEY_MOVIE_TYPE = "movie_type";
    public static final String KEY_TV_SHOW_TYPE = "tv_show_type";
    public static final String KEY_MEDIA_TITLE = "media_title";


    public static final String USER_SHARED_PREFERENCE = "user_shared_preference";
    public static final String COLOR_THEME_SHARED_PREFERENCE = "color_theme_shared_preference";

    public static final String RECYCLE_VIEW_MODE = "view_mode";
    public static final int RECYCLE_VIEW_LIST = 1;
    public static final int RECYCLE_VIEW_GRID = 2;

    public static final String NO_ANSWER = "N/A";

    public static final String IMAGE_URL_PATH = "http://image.tmdb.org/t/p/w185";
    public static final String YOUTUBE_URL_IMAGE_PATH = "http://i1.ytimg.com/vi/";

    public static final String YOUTUBE_URL_PATH = "http://www.youtube.com/watch?v=";
    public static final String BACKDROP_URL_PATH = "http://image.tmdb.org/t/p/w780";
    public static final String PERSON_URL_PATH = "http://image.tmdb.org/t/p/w185";


    @SuppressLint("SimpleDateFormat")
    public static String convertDate(String oldDate) {
        if (oldDate == null) return NO_ANSWER;

        final String JSON_DATE_FORMAT = "yyyyy-mm-dd";
        final String DESIRE_DATE_FORMAT = "d MMM yyyy";

        SimpleDateFormat dt = new SimpleDateFormat(JSON_DATE_FORMAT);
        SimpleDateFormat nw = new SimpleDateFormat(DESIRE_DATE_FORMAT);
        Date date;
        String result = null;
        try {
            date = dt.parse(oldDate);
            result = nw.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (result == null) return NO_ANSWER;

        return result;
    }

    private static class BiMap<K, V> {

        HashMap<K, V> map = new HashMap<>();
        HashMap<V, K> inversedMap = new HashMap<>();

        void put(K k, V v) {
            map.put(k, v);
            inversedMap.put(v, k);
        }

        V getValue(K k) {
            return map.get(k);
        }

        K getKey(V v) {
            return inversedMap.get(v);
        }

    }

    private static final BiMap<String, Integer> movieGenre = new BiMap<String, Integer>() {
        {
            put("Action", 28);
            put("Adventure", 12);
            put("Animation", 16);
            put("Comedy", 35);
            put("Crime", 80);
            put("Documentary", 99);
            put("Drama", 18);
            put("Family", 10751);
            put("Fantasy", 14);
            put("History", 36);
            put("Horror", 27);
            put("Music", 10402);
            put("Mystery", 9648);
            put("Romance", 10749);
            put("Sci-Fi", 878);
            put("TV Movie", 10770);
            put("Thriller", 53);
            put("War", 10752);
            put("Western", 37);
            put("Action & Adventure", 10759);
            put("Kids", 10762);
            put("News", 10763);
            put("Reality", 10764);
            put("Sci-Fi & Fantasy", 10765);
            put("Soap", 10766);
            put("Talk", 10767);
            put("War & Politics", 10768);
        }
    };

    public static String getMovieGenres(List<Integer> movieGenres) {
        List<String> list = new ArrayList<>();

        for (int genre : movieGenres)
            list.add(movieGenre.getKey(genre));

        return split(list);
    }

    public static String split(List<String> list) {
        String result = "";

        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
            if (i == list.size() - 1) break;
            result += ", ";
        }

        return getAString(result);
    }

    public static String getAString(String value) {
        if (value == null || value.length() < 1 || value.equals("0"))
            return NO_ANSWER;
        else
            return value;
    }

    public static String getAString(int value) {
        if (value == 0)
            return NO_ANSWER;
        else
            return String.valueOf(value);
    }

    public static String getAString(double value) {
        if (value == 0.0)
            return NO_ANSWER;
        else
            return String.valueOf(value);
    }

}
