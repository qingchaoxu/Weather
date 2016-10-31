package com.example.administrator.weather;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Spinner;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by Administrator on 2016/10/29.
 */

public class SettingActivity extends Activity{
    private TextView city;
    private Button button2;
    private EditText text;
    private int day=3;
    private int language=1;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private int temperature=1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        city=(TextView) findViewById(R.id.cityText);
        button2 = (Button)findViewById(R.id.button2);
        text = (EditText)findViewById(R.id.cityName);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner3);
        text1=(TextView) findViewById(R.id.text1);
        text2=(TextView) findViewById(R.id.text2);
        text3=(TextView) findViewById(R.id.text3);
        //get data from WeatherActivity
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null)
        {

            language=bundle.getInt("language");
        }
        //change languages
        if(language==1)
        {
            city.setText("Please enter the zipcode of city");
            text1.setText("Please select the count of day to show weather");
            text2.setText("select language:");
            button2.setText("Confirm");
            text3.setText("select temperture unit");
        }
        else if (language==2)
        {
            city.setText("请输入城市的邮政编码");
            text1.setText("选择显示天气的天数");
            text2.setText("选择语言");
            button2.setText("确定");
            text3.setText("选择温度的单位");

        }
        //After click confirm, send data to WeatherActivity
        ((Button) findViewById(R.id.button2))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SettingActivity.this,
                                WeatherActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("name", text.getText().toString());
                        bundle.putInt("day", day);
                        bundle.putInt("temperature", temperature);
                        bundle.putInt("language", language);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                day = pos+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                language=pos+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                temperature=pos+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


}
    }
