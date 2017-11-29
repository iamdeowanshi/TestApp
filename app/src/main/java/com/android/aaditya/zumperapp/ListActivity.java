package com.android.aaditya.zumperapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.aaditya.zumperapp.base.BaseActivity;
import com.android.aaditya.zumperapp.model.Place;
import com.android.aaditya.zumperapp.model.Review;
import com.android.aaditya.zumperapp.module.details.ReviewAdapter;
import com.android.aaditya.zumperapp.service.place.ListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;

public class ListActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Type listType = new TypeToken<List<Place>>() {
        }.getType();
        Bundle extras = getIntent().getExtras();
        List<Place> places = new Gson().fromJson(extras.getString("place"), listType);


        adapter = new ListAdapter(this, places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
