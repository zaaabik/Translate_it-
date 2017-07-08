package org.suai.zabik.fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import org.suai.zabik.models.DataBase;
import org.suai.zabik.models.History;
import org.suai.zabik.views.R;
import org.suai.zabik.views.RequestListAdapter;

import java.util.ArrayList;


public class HistoryFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    ResultsIsChanged callback;
    ArrayList<History> historyList;
    RequestListAdapter requestListAdapter;
    //view of fragment
    View fragmentView;
    //listView of History and Favorite requests
    ListView historyAndFavoriteListView;
    private ImageButton backButton;
    //database clear button
    private Button clearHistoryButton;

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    private void onAttachToContext(Context context) {
        callback = (ResultsIsChanged) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        callback = (ResultsIsChanged) getActivity();
        fragmentView =  inflater.inflate(R.layout.activity_history_and_favorite,container,false);
        backButton = (ImageButton) fragmentView.findViewById(R.id.backButton);
        backButton.setImageResource(R.drawable.ic_back);
        historyAndFavoriteListView = (ListView)fragmentView.findViewById(R.id.historyAndFavoritelistView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //find view, get resource and set onClick listener
        try {
            backButton.setOnClickListener(this);
            clearHistoryButton = (Button) getView().findViewById(R.id.clearHistoryButton);
            clearHistoryButton.setOnClickListener(this);
        } catch (NullPointerException e) {
            Log.e("ViewFind", "cant find clearButton on history", e);
        }
        showHistory();

    }

    private void showHistory(){
        historyList = DataBase.getAll(getContext());
        requestListAdapter = new RequestListAdapter(getActivity(),
                R.layout.list_item,
                historyList
        );
        historyAndFavoriteListView.setAdapter(requestListAdapter);
        historyAndFavoriteListView.setOnItemClickListener(this);
        requestListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                getActivity().finish();
                break;
            case R.id.clearHistoryButton:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("History");

                alertDialogBuilder.setMessage("Are you sure you want to delete your history?")
                        .setCancelable(true)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataBase.deleteDataBase(getContext());
                                callback.deleteAll();
                                showHistory();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

        }
    }

    public void changeItemsState(int id){
        RequestListAdapter requestListAdapter = (RequestListAdapter) historyAndFavoriteListView.getAdapter();
        DataBase.changeFavoriteState(getContext(), id);
        ArrayList<History> list = requestListAdapter.getListData();
        History item = new History();
        item.setId(id);
        item = list.get(list.indexOf(item));
        if (list.contains(item)) {
            History history = list.get(list.indexOf(item));
            history.setFavorite(!history.getIsFavlorite());
            requestListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageButton tmpView =(ImageButton) view.findViewById(R.id.star);
        if (DataBase.changeFavoriteState(getContext(), id)) {
            History currentListItem = requestListAdapter.getListData().get(position);
            currentListItem.setFavorite(true);
            tmpView.setImageResource(R.drawable.ic_favorite);
            callback.newItem(currentListItem);
        }
        else {
            tmpView.setImageResource(R.drawable.ic_notfavorite);
            History currentListItem = requestListAdapter.getListData().get(position);
            currentListItem.setFavorite(false);
            requestListAdapter.getListData().get(position).setFavorite(false);
            callback.deleteItemFromFavorite(currentListItem);
        }

    }

    public interface ResultsIsChanged {
        void newItem(History item);

        void deleteItemFromFavorite(History item);

        void deleteAll();
    }
}