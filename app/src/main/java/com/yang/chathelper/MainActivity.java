package com.yang.chathelper;

import android.Manifest;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private List<String> mListContent = new ArrayList<>();
    private Map<Integer, String> mMap;
    private Map<Integer, String> mFinalMap;

    final RxPermissions rxPermissions = new RxPermissions(this);

    private String mAppType;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FileIOUtils.readFile2String()
        
        initView();
        initRecyclerview();

        saveContent();

    }

    private void saveContent() {
        Button save = findViewById(R.id.save);
        save.setOnClickListener(view -> rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        int i = 0;
                        for (Map.Entry<Integer, String> entry : mMap.entrySet()) {
                            mFinalMap.put(i, entry.getValue());
                            i++;
                        }
                        Log.d(TAG, "onCreate: " +  new Gson()
                                .toJson(new Gson().toJson(mFinalMap)));

                        boolean success = FileIOUtils.writeDownloadFile(mAppType + "/content.txt",
                                new Gson().toJson(mFinalMap));
                        boolean status = FileIOUtils.writeDownloadFile(mAppType + "/status.txt",
                                new Gson().toJson(mMap));
                        boolean size = FileIOUtils.writeDownloadFile(mAppType + "/size.txt",
                                mFinalMap.size() + "");
                        if (success && size && status) {
                            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "需要权限进行操作", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void initRecyclerview() {
        RecyclerView recyclerView = findViewById(R.id.ad_content);


        ArrayList<String> contents = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            contents.add("输入广告内容" + (i + 1));
        }

        mMap = new HashMap();
        mFinalMap = new TreeMap<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdContentAdapter adContentAdapter = new AdContentAdapter(contents, this);
        recyclerView.setAdapter(adContentAdapter);


        adContentAdapter.setOnTextChangeListener((position, string) -> {
            if (adContentAdapter.getCheckStates().get(position)) {
                if (!TextUtils.isEmpty(string)) {
                    mMap.put(position,  string);
                } else {
                    mMap.remove(position);
                }
            }
        });
    }

    private void initView() {
        AppType[] values = AppType.values();
        for (int i = 0; i < values.length; i++) {
            mListContent.add(values[i].getName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, this.mListContent);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAppType = values[i].getName();
                Toast.makeText(MainActivity.this, mAppType, Toast.LENGTH_SHORT).show();
//                switch (values[i]) {
//                    case TULIAO:
////                        Toast.makeText(MainActivity.this, "TULIAO", Toast.LENGTH_SHORT).show();
//                        break;
//                    case CESHI:
////                        Toast.makeText(MainActivity.this, "CESHI", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}
