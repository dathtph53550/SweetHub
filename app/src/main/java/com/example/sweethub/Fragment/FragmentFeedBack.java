package com.example.sweethub.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Adapter.AdapterFeedBack;
import com.example.sweethub.Model.FeedBack;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentFeedBack extends Fragment {

    AdapterFeedBack adapter;
    ArrayList<FeedBack> list;
    RecyclerView recyclerView;
    HttpRequest httpRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        httpRequest = new HttpRequest();
        recyclerView = view.findViewById(R.id.recyclerViewFeedback);
        list = new ArrayList<>();
        adapter = new AdapterFeedBack(getContext(),list);
        recyclerView.setAdapter(adapter);

        httpRequest.callAPI().getListFeedback().enqueue(new Callback<Response<ArrayList<FeedBack>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<FeedBack>>> call, retrofit2.Response<Response<ArrayList<FeedBack>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        list.clear();
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<FeedBack>>> call, Throwable t) {

            }
        });
    }




}
