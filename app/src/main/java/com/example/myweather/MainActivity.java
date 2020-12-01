package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.lljjcoder.citypickerview.widget.CityPicker;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_response,tv_address,weather;
    private EditText cityid;
    private SharedPreferences sharedPreferences;
    private CityPicker mCP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = findViewById(R.id.send_request);
        Button refresh = findViewById(R.id.refresh);
        Button btn_address = findViewById(R.id.btn_address);
        Button btn_addresscode = findViewById(R.id.btn_addresscode);
        Button concern = findViewById(R.id.concern);
        btn_address.setOnClickListener(this);
        btn_addresscode.setOnClickListener(this);
        concern.setOnClickListener(this);
        sendRequest.setOnClickListener(this);
        refresh.setOnClickListener(this);
        this.tv_response = findViewById(R.id.response);
        cityid = findViewById(R.id.cityid);
        tv_address = findViewById(R.id.address);
        weather = findViewById(R.id.weather);
    }
    //根据天气返回的字符串进行切片
    public String tranString(String rs) {
        rs = rs.substring(rs.indexOf("[") + 1, rs.indexOf("]"));
        weather wt = JSONObject.parseObject(rs, weather.class);
        return wt.toString();
    }
    //存入sharedpreference
    public void setSharedPreference(String cityid, String responeStr) {
        sharedPreferences = getSharedPreferences("weather", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(cityid, responeStr);
        editor.commit();
    }
    //读取sharedpreference
    public String getSharePreference(String cityid) {
        sharedPreferences = getSharedPreferences("weather", MODE_PRIVATE);
        String str = sharedPreferences.getString(cityid, "");
        return str;
    }

    //根据输入的城市获取返回的城市信息并进行切片
    public String tranString1(String rs1) {
        rs1 = rs1.substring(rs1.indexOf("adcode") + 9 ,rs1.indexOf("adcode") + 15);
        return rs1;
    }

    //滚轮选择城市
    public void initCityPicker() {
        mCP = new CityPicker.Builder(MainActivity.this)
                .textSize(20)//滚轮文字的大小
                .title("城市选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#EE00EE")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                .textColor(Color.parseColor("#912CEE"))//滚轮文字的颜色
                .provinceCyclic(true)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(false)
                .build();
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];

                tv_address.setText(province + city + district);

            }

            @Override
            public void onCancel() {

            }
        });
    }
    //插入数据库
    private void insertData(String city_code,String city_name) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("city_name", city_name);
        contentValues.put("city_code", city_code);
    }
    //设置点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //通过滚轮选择城市
            case R.id.btn_address:
                initCityPicker();
                mCP.show();
                break;
             //通过缓存查看天气
            case R.id.send_request:
                if(!getSharePreference(cityid.getText().toString()).equals(""))
                {
            tv_response.setText(tranString(getSharePreference(cityid.getText().toString())));
                    Toast.makeText(MainActivity.this, "缓存数据", Toast.LENGTH_SHORT).show();
            }
                //初始化OKHttp客户端
                OkHttpClient client = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request request = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/weather/weatherInfo?city=" + cityid.getText() + "&key=0a9b6dfd8d263cd415cf5f794d06f864&extensions=base")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败处理
                        ToastUtils.showToast(MainActivity.this, "Get请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //返回结果处理
                        final String responseStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (responseStr.length() < 100) {
                                    Toast.makeText(MainActivity.this, "城市不存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    tv_response.setText(tranString(responseStr));
                                    setSharedPreference(cityid.getText().toString(), responseStr);
                                }
                            }
                        });

                    }
                });
                break;
                //从网络上获取信息
                case R.id.refresh:
                //初始化OKHttp客户端
                OkHttpClient client0 = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request request0 = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/weather/weatherInfo?city=" + cityid.getText() + "&key=0a9b6dfd8d263cd415cf5f794d06f864&extensions=base")
                        .build();
                Call call0 = client0.newCall(request0);
                call0.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call0, IOException e) {
                        //失败处理
                        ToastUtils.showToast(MainActivity.this, "Get请求失败");
                    }
                    @Override
                    public void onResponse(Call call0, Response response0) throws IOException {
                        //返回结果处理
                        final String responseStr0 = response0.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (responseStr0.length() < 100) {
                                    Toast.makeText(MainActivity.this, "城市不存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    tv_response.setText(tranString(responseStr0));
                                    Toast.makeText(MainActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                    }
                });
                //通过选择的城市来查看相应的编码
                case R.id.btn_addresscode:
                OkHttpClient client1 = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request addresscode = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/geocode/geo?address="+tv_address.getText()+"&output=JSON&key=0a9b6dfd8d263cd415cf5f794d06f864")
                        .build();
                Call call1 = client1.newCall(addresscode);
                call1.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call1, IOException e) {
                        //失败处理
                        ToastUtils.showToast(MainActivity.this, "Get请求失败");
                    }

                    @Override
                    public void onResponse(Call call1, Response addresscode) throws IOException {
                        //返回结果处理
                        final String responseStr1 = addresscode.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    cityid.setText(tranString1(responseStr1));
                            }
                        });

                    }
                });/*
                //点击添加到列表
           case R.id.concern:
                Toast toast = Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_LONG);
                toast.show();
                String city_code = cityid.getText().toString();
                String city_name = tv_address.getText().toString();
                insertData(city_code, city_name);
                finish();*/
        }
        }
    }