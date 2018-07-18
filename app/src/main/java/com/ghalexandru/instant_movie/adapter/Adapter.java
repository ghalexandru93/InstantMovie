package com.ghalexandru.instant_movie.adapter;

import android.support.v7.widget.RecyclerView;

import com.ghalexandru.instant_movie.model.Model;

import java.util.List;

public abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public abstract void add(List<? extends Model> list);

    public abstract void clear();
}
