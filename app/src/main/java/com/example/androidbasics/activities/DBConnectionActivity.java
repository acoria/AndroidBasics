package com.example.androidbasics.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidbasics.DBConnUseCase;
import com.example.androidbasics.R;
import com.example.androidbasics.adapter.DBConnectionAdapter;
import com.example.androidbasics.persist.DBConnStore;
import com.example.androidbasics.pojo.RandomWord;
import com.example.androidbasics.vm.DBConnectionViewModel;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

public class DBConnectionActivity extends AppCompatActivity {

    private DBConnUseCase useCase;
    private DBConnectionViewModel viewModel;
    private TextView inputField;
    private InitialValueObservable<CharSequence> textToDBobservable;
    private DBConnectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbconnection);
        inputField = findViewById(R.id.inp_text_to_db);

        RecyclerView recyclerView = findViewById((R.id.recycl_db));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DBConnectionAdapter();
        recyclerView.setAdapter(adapter);

        useCase = new DBConnUseCase(this);
        viewModel = new DBConnectionViewModel(useCase);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(DBConnStore.LOADER_ID,null, useCase.getLoaderCallback());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(DBConnStore.LOADER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        textToDBobservable = RxTextView.textChanges(inputField);

        RxView.clicks(findViewById(R.id.btn_save_to_db))
                .withLatestFrom(textToDBobservable, Pair::new)
                .filter(pair -> pair.second.length() != 0)
                .map(pair -> pair.second)
                .subscribe(text -> {
                    handleNewTextToSave(text);
                    inputField.setText("");
                });

        viewModel.getWords()
                .subscribe(adapter::updateData);
    }

    private void handleNewTextToSave(CharSequence newText) {
        useCase.addRandomWord(new RandomWord(newText.toString()),getApplicationContext());
    }
}
