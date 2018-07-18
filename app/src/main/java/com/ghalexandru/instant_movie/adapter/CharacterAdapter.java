package com.ghalexandru.instant_movie.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ghalexandru.instant_movie.GlideApp;
import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.model.Model;
import com.ghalexandru.instant_movie.model.character.Character;
import com.ghalexandru.instant_movie.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for {@link Character}
 */
public class CharacterAdapter extends Adapter<CharacterAdapter.ViewHolder> {

    private List<Character> list;

    public CharacterAdapter() {
        this(new ArrayList<Character>());
    }

    public CharacterAdapter(List<? extends Character> list) {
        this.list = new ArrayList<>(list);
    }

    /**
     * Inflate {@code R.layout.item_person}
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void add(List<? extends Model> list) {
        int oldPosition = this.list.size() + 1;
        this.list.addAll((Collection<? extends Character>) list);
        notifyItemRangeInserted(oldPosition, this.list.size());
    }

    @Override
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_person)
        ImageView ivPerson;

        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final Character character) {
            GlideApp.with(ivPerson.getContext())
                    .load(Util.PERSON_URL_PATH + character.getProfilePath())
                    .error(R.drawable.ic_emoticon_neutral_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPerson);

            tvName.setText(character.getName());
        }


    }
}
