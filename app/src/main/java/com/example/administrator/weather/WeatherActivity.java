package com.example.administrator.weather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.app.Activity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class WeatherActivity extends Activity implements LocationFinder.LocationDetector {
    private TextView name;
    private ImageView icon;
    private TextView weather;
    private TextView humidity;
    private TextView maxtemperature;
    private Button button1;
    private List<JSONObject> lists;
    private ProgressDialog pd;
    private MyListView weatherList = null;
    private MyAdapter adapter = null;
    private String name1 = "";
    private int day = 3;
    private int language = 1;
    private int temperature = 1;
    private final int MY_PERMISSIONS_ACCESS_FINE_LOCATION=1;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lists = new ArrayList<JSONObject>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        name = (TextView) findViewById(R.id.cityText);
        icon = (ImageView) findViewById(R.id.icon);
        weather = (TextView) findViewById(R.id.weather);
        humidity = (TextView) findViewById(R.id.mintemp);
        maxtemperature = (TextView) findViewById(R.id.maxtemp);
        button1 = (Button) findViewById(R.id.button1);
        weatherList = (MyListView) findViewById(R.id.weatherList);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }
        }
        //recieve data from setting
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            name1 = bundle.getString("name");
            day = bundle.getInt("day");
            language = bundle.getInt("language");
            temperature = bundle.getInt("temperature");
        }
        if (language == 1) {
            button1.setText("Setting");
        }
        if (language == 2) {
            button1.setText("设置");
        }
        ((Button) findViewById(R.id.button1))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(WeatherActivity.this,
                                SettingActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("language", language);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
        // if you do not enter Zipcode, it will show current location weather
        if (name1.equals("")) {
            LocationFinder locationFinder = new LocationFinder(this,this);
            locationFinder.detectLocation();

        } else {
            getData2();
            getData3();
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //interface method
    @Override
    public void locationFound(Location location) {
        getData(location);
        getData1(location);
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
    }
    //request promission
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:
            {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Weather Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public final class ViewHolder {
        public TextView weekday;

        public ImageView weatherPic1;
        public TextView weatherType1;
        public TextView temp1;
        public TextView temp2;

    }

    private void getData(Location location) {
        new MyAsyncTask().execute(location);

    }

    private void getData1(Location location) {
        new MyAsyncTask1().execute(location);

    }

    private void getData2() {
        new MyAsyncTask2().execute();

    }

    private void getData3() {
        new MyAsyncTask3().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<JSONObject> list = new ArrayList<JSONObject>();
        ;

        public MyAdapter(Context context, List<JSONObject> ls) {
            this.mInflater = LayoutInflater.from(context);
            list = ls;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public boolean areAllItemsEnabled() {
            // all items are separator
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            // all items are separator
            return false;
        }

        @Override
        //get listview data
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.adapter_weather_item,
                        null);

                holder.weatherPic1 = (ImageView) convertView
                        .findViewById(R.id.weatherIv1);
                holder.weatherType1 = (TextView) convertView
                        .findViewById(R.id.weatherType1);
                holder.temp1 = (TextView) convertView.findViewById(R.id.temp1);
                holder.temp2 = (TextView) convertView.findViewById(R.id.temp2);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //data analysis
            try {
                //

                JSONArray weather = list.get(position).getJSONArray("weather");
                JSONObject tempObj = (JSONObject) weather.get(0);
                String WeatherType = tempObj.getString("main");
                String WeatherType1 = tempObj.getString("description");
                String weathertype = tempObj.getString("icon");
                JSONObject tempObj1 = list.get(position).getJSONObject("temp");

                int mintemperature = 0;
                int maxtemperature = 0;
                if (temperature == 1) {
                    mintemperature = tempObj1.getInt("min") - 273;
                    maxtemperature = tempObj1.getInt("max") - 273;
                    holder.temp1.setText("temperature:" + mintemperature + "℃ " + "----" + maxtemperature + "℃   ");
                }
                if (temperature == 2) {
                    double tep1 = (tempObj1.getInt("min") - 273) * 1.8 + 32;
                    double tep2 = (tempObj1.getInt("max") - 273) * 1.8 + 32;
                    mintemperature = (int) tep1;
                    maxtemperature = (int) tep2;
                    holder.temp1.setText("temperature:" + mintemperature + "℉ " + "----" + maxtemperature + "℉   ");
                }

                String humidity = list.get(position).getString("humidity");

                holder.weatherPic1.setImageResource(parseIcon(0, weathertype));
                holder.weatherType1.setText(WeatherType + "(" + WeatherType1 + ")");
                holder.temp2.setText("humidity:" + humidity);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

    }

    //determine the icon
    private int parseIcon(int time, String strIcon) {
        if (strIcon == null)
            return -1;
        if (time == 0) {
            if ("01d".equals(strIcon))
                return R.drawable.weather_01d;
            if ("01n".equals(strIcon))
                return R.drawable.weather_01n;
            if ("02d".equals(strIcon))
                return R.drawable.weather_02d;
            if ("02n".equals(strIcon))
                return R.drawable.weather_02n;
            if ("03d".equals(strIcon))
                return R.drawable.weather_03d;
            if ("03n".equals(strIcon))
                return R.drawable.weather_03n;
            if ("04d".equals(strIcon))
                return R.drawable.weather_04d;
            if ("04n".equals(strIcon))
                return R.drawable.weather_04n;
            if ("09d".equals(strIcon))
                return R.drawable.weather_09d;
            if ("09n".equals(strIcon))
                return R.drawable.weather_09n;
            if ("10d".equals(strIcon))
                return R.drawable.weather_10d;
            if ("10n".equals(strIcon))
                return R.drawable.weather_10n;
            if ("11d".equals(strIcon))
                return R.drawable.weather_11d;
            if ("11n".equals(strIcon))
                return R.drawable.weather_11n;
            if ("13d".equals(strIcon))
                return R.drawable.weather_13d;
            if ("13n".equals(strIcon))
                return R.drawable.weather_13n;
            if ("50d".equals(strIcon))
                return R.drawable.weather_50d;
            if ("50n".equals(strIcon))
                return R.drawable.weather_50n;


        }
        // return 0;
        return R.drawable.icon;
    }

    protected void onDestroy() {
        super.onDestroy();
    }
    //show current location weather
    class MyAsyncTask extends AsyncTask<Location, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Location... params) {
            // TODO Auto-generated method stub
            String path = "";
            double a=params[0].getLatitude();double b=params[0].getLongitude();
            String lat=Double.toString(a);String lon=Double.toString(b);
            path = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=57e36352876e7d971d2747af1b0bdd35";
            String jsonString = HttpUtils.getJsonContent(path);

            return jsonString;// send data
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            doingData(result);
        }

    }


    public void doingData(String data) {
        System.out.println("data" + data);
        JSONObject temp;
        try {
            temp = new JSONObject(data);
            name.setText(temp.getString("name"));
            JSONArray posts = temp.getJSONArray(("weather"));

            JSONObject tempObj = (JSONObject) posts.get(0);

            String icon1 = tempObj.getString("icon");
            icon.setImageResource(parseIcon(0, icon1));

            weather.setText(tempObj.getString("main") + "(" + tempObj.getString("description") + ")");
            JSONObject tempWeatherData = temp.getJSONObject("main");

            if (temperature == 1) {
                int tep = tempWeatherData.getInt("temp_max") - 273;
                maxtemperature.setText("" + tep + "℃");
            }
            if (temperature == 2) {
                double tep1 = (tempWeatherData.getInt("temp_max") - 273) * 1.8 + 32;
                int tep = (int) tep1;
                maxtemperature.setText("" + tep + "℉");
            }


            humidity.setText((String) tempWeatherData
                    .getString("humidity"));


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error",
                    Toast.LENGTH_SHORT).show();
        }

    }
    //show current location listview weather
    class MyAsyncTask1 extends AsyncTask<Location, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            if (lists.size() == 0) {
                if (pd != null)
                    pd.dismiss();
                pd = ProgressDialog.show(WeatherActivity.this, null, "waiting for data");
                pd.setCancelable(true);
            }
        }

        @Override
        protected String doInBackground(Location... params) {
            // TODO Auto-generated method stub

            String path = "";
            double a=params[0].getLatitude();double b=params[0].getLongitude();

            String lat=Double.toString(a);String lon=Double.toString(b);
            path = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="+lat+"&lon="+lon+"&appid=57e36352876e7d971d2747af1b0bdd35";
            System.out.println(path);
            String jsonString1 = HttpUtils.getJsonContent(path);

            return jsonString1;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            doingData1(result);
        }

    }

    public void doingData1(String data) {
        System.out.println("data" + data);
        JSONObject temp1;
        try {

            temp1 = new JSONObject(data);

            lists.clear();

            JSONArray tempArr = temp1.getJSONArray("list");

            JSONObject temp = new JSONObject();
            for (int i = 0; i < day; i++) {
                temp = (JSONObject) tempArr.get(i);
                lists.add(temp);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error",
                    Toast.LENGTH_SHORT).show();
        }
        setListView();
        pd.dismiss();
    }

    private void setListView() {
        adapter = new MyAdapter(this, lists);
        weatherList.setAdapter(adapter);
        weatherList.setonRefreshListener(new MyListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {

                            Thread.sleep(1000);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {

                        adapter.notifyDataSetChanged();
                        weatherList.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });
    }
    //show zipcode listview weather
    class MyAsyncTask2 extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // super.onPreExecute();

            if (lists.size() == 0) {
                if (pd != null)
                    pd.dismiss();
                pd = ProgressDialog.show(WeatherActivity.this, null, "waiting for data");
                pd.setCancelable(true);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String path = "";
            //path = "http://api.openweathermap.org/data/2.5/forecast?q="
            // + city_str + "&appid=57e36352876e7d971d2747af1b0bdd35";
            path = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + name1 + ",us&appid=57e36352876e7d971d2747af1b0bdd35";
            String jsonString = HttpUtils.getJsonContent(path);

            return jsonString;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            doingData2(result);
        }

    }

    public void doingData2(String data) {
        System.out.println("data" + data);
        JSONObject temp1;
        try {

            temp1 = new JSONObject(data);

            lists.clear();

            JSONArray tempArr = temp1.getJSONArray("list");

            JSONObject temp = new JSONObject();
            for (int i = 0; i < day; i++) {
                temp = (JSONObject) tempArr.get(i);
                lists.add(temp);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error",
                    Toast.LENGTH_SHORT).show();
        }
        setListView1();
        pd.dismiss();
    }

    private void setListView1() {
        adapter = new MyAdapter(this, lists);
        weatherList.setAdapter(adapter);
        weatherList.setonRefreshListener(new MyListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {

                            Thread.sleep(1000);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getData2();

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {

                        adapter.notifyDataSetChanged();
                        weatherList.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });
    }
    //show zipcode current weather
    class MyAsyncTask3 extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String path = "";
            String path1 = "";
            path = "http://api.openweathermap.org/data/2.5/weather?q=" + name1 + ",us&appid=57e36352876e7d971d2747af1b0bdd35";
            String jsonString = HttpUtils.getJsonContent(path);

            return jsonString;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            doingData3(result);
        }

    }


    public void doingData3(String data) {
        System.out.println("data" + data);
        JSONObject temp;
        try {
            temp = new JSONObject(data);

            JSONArray posts = temp.getJSONArray(("weather"));//

            JSONObject tempObj = (JSONObject) posts.get(0);

            String icon1 = tempObj.getString("icon");
            icon.setImageResource(parseIcon(0, icon1));

            weather.setText(tempObj.getString("main") + "(" + tempObj.getString("description") + ")");
            JSONObject tempWeatherData = temp.getJSONObject("main");

            name.setText(temp.getString("name"));
            if (temperature == 1) {
                int tep = tempWeatherData.getInt("temp_max") - 273;
                maxtemperature.setText("" + tep + "℃");
            }
            if (temperature == 2) {
                double tep1 = (tempWeatherData.getInt("temp_max") - 273) * 1.8 + 32;
                int tep = (int) tep1;
                maxtemperature.setText("" + tep + "℉");
            }

            humidity.setText((String) tempWeatherData
                    .getString("humidity"));


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
