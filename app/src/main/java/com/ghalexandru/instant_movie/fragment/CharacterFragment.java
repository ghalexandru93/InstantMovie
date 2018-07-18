package com.ghalexandru.instant_movie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.adapter.CharacterAdapter;
import com.ghalexandru.instant_movie.model.character.Character;
import com.ghalexandru.instant_movie.model.character.Person;
import com.ghalexandru.instant_movie.model.character.PersonResponse;
import com.ghalexandru.instant_movie.rest.ApiClient;
import com.ghalexandru.instant_movie.rest.ApiInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment used to show a list of people
 */
public class CharacterFragment extends MediaFragment {

    private String TAG = getClass().getSimpleName();

    private static final String KEY_CHARACTER_LIST = "character_list";
    private List<Character> characterList;

    @Nullable
    private ApiInterface apiService;

    /**
     * Create an instance of this fragment that will fill {@link #recyclerView} with data from server
     * @return
     */
    public static CharacterFragment newInstance() {
        CharacterFragment fragment = new CharacterFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create an instance of this fragment that will be filled with {@param characterList}
     */
    public static CharacterFragment newInstance(List<? extends Character> characterList) {
        CharacterFragment fragment = new CharacterFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_CHARACTER_LIST, (Serializable) characterList);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            characterList = (List<Character>) getArguments().getSerializable(KEY_CHARACTER_LIST);

        /**
         * if {@link #characterList} is null or empty then we retrieve data from server
         */
        if (characterList == null || characterList.size() < 1)
            apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    protected void getMedia() {
        showProgressLoader();
        apiService.getPopularPersons(getString(R.string.api_key), currentPage + 1).enqueue(personCallback);
    }

    @Override
    protected void setupRecyclerView() {
        if (characterList != null)
            mediaAdapter = new CharacterAdapter(characterList);
        else
            mediaAdapter = new CharacterAdapter();

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mediaAdapter);

        if (characterList == null)
            recyclerView.addOnScrollListener(recyclerCallback);
    }

    private Callback<PersonResponse> personCallback = new Callback<PersonResponse>() {
        @Override
        public void onResponse(@NonNull Call<PersonResponse> call, @NonNull Response<PersonResponse> response) {
            PersonResponse personResponse = response.body();

            if (personResponse != null) {
                totalPages = personResponse.getTotalPages();
                currentPage = personResponse.getPage();
                List<Person> persons = response.body().getResults();
                mediaAdapter.add(new ArrayList<>(persons));
            }

            hideProgressLoader();
        }

        @Override
        public void onFailure(@NonNull Call<PersonResponse> call, @NonNull Throwable t) {
            hideProgressLoader();
        }
    };
}
