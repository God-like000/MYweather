package com.example.myweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Arrays;

import static com.example.myweather.DBHeleper.DB_NAME;

public class List extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    ArrayAdapter simpleAdapter;
    ListView cityList;
    private java.util.List<String> city_nameList = new ArrayList<>();
    private java.util.List<String> city_codeList = new ArrayList<>();

    private void InitConcern() {       //进行数据填装
        DBHeleper dbHelper = new DBHeleper(this, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Concern", null);
        while (cursor.moveToNext()) {
            String city_code = cursor.getString(cursor.getColumnIndex("city_code"));
            String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
            city_codeList.add(city_code);
            city_nameList.add(city_name);
        }
    }

    private void add(String city_code) {
    }

    public void RefreshList() {
        city_nameList.removeAll(city_nameList);
        city_codeList.removeAll(city_codeList);
        simpleAdapter.notifyDataSetChanged();
        DBHeleper dbHelper = new DBHeleper(this, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Concern", null);
        while (cursor.moveToNext()) {
            String city_code = cursor.getString(cursor.getColumnIndex("city_code"));
            String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
            city_codeList.add(city_code);
            city_nameList.add(city_name);
        }
    }

    private void removeAll(List city_nameList) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        RefreshList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        cityList = findViewById(R.id.citylist);

        InitConcern();

        simpleAdapter = new ArrayAdapter(List.this, android.R.layout.simple_list_item_1, city_nameList);

        cityList.setAdapter(simpleAdapter);
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {      //配置ArrayList点击按钮
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tran = city_codeList.get(position);
                Intent intent = new Intent();
                intent.putExtra("adcode", tran);
                startActivity(intent);
            }
        });

    }

    private void get(int position) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);   //startActivity方法
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    citylist.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    });
*/

    public void initEvent() {
        final MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
        cityList.setAdapter(myBaseAdapter);
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                Intent intent = new Intent(List.this, MainActivity.class);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        });
    }

    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            Cursor cursor = mDatabase.rawQuery("select count(2) from " + DBHeleper.TABLE_NAME, null);
            cursor.moveToFirst();
            long count = cursor.getLong(0);
            cursor.close();
            int num = (int) count;
            return num;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

    }


}

