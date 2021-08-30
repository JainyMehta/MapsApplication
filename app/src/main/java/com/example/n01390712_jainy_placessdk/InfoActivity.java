package com.example.n01390712_jainy_placessdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        String JsonArray = intent.getStringExtra("JsonArray");


        try{
            JSONArray jArray = new JSONArray(JsonArray);
            TableLayout tv=(TableLayout) findViewById(R.id.table);
            tv.removeAllViewsInLayout();
            tv.setColumnShrinkable(1,true);
            int flag=1;

            // when i=-1, loop will display heading of each column
            // then usually data will be display from i=0 to jArray.length()
            for(int i=-1;i<jArray.length();i++){

                TableRow tr=getTableRows(i);

//                // this will be executed once
                if(flag==1){

                    TextView b3=new TextView(InfoActivity.this);
                    b3.setText("KEY");
                    b3.setTextColor(Color.BLUE);
                    b3.setTextSize(25);
                    b3.setGravity(Gravity.CENTER);
                    tr.addView(b3);

                    TextView b4=new TextView(InfoActivity.this);
                    b4.setTextSize(25);
                    b4.setText("VALUE");
                    b4.setGravity(Gravity.CENTER);
                    b4.setTextColor(Color.BLUE);
                    tr.addView(b4);

                    tv.addView(tr);

                    final View vline = new View(InfoActivity.this);
                    vline.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
                    vline.setBackgroundColor(Color.BLUE);
                    tv.addView(vline); // add line below heading
                    flag=0;
                } else {
                    JSONObject json_data = jArray.getJSONObject(i);

                      TextView b=new TextView(InfoActivity.this);
                      String str="Name";
                      b.setText(str);
                      b.setTextColor(Color.argb(100,0,64,64));
                      b.setTextSize(20);
                      //b.setGravity(Gravity.CENTER);
                      tr.addView(b);

                    TextView b2=new TextView(InfoActivity.this);
                    String str1=json_data.getString("name");
                    b2.setText(str1);
                    b2.setTextColor(Color.argb(100,0,128,128));
                    b2.setTextSize(20);
                    //b2.setGravity(Gravity.CENTER);
                    tr.addView(b2);

                    tv.addView(tr);
                    final View vline1 = new View(InfoActivity.this);
                    vline1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    vline1.setBackgroundColor(Color.GREEN);
                    tv.addView(vline1);  // add line below each row
                    tr=getTableRows(i);

                    TextView b13=new TextView(InfoActivity.this);
                    str="In Map";
                    b13.setText(str);
                    b13.setTextColor(Color.argb(100,0,64,64));
                    b13.setTextSize(20);
                    //b.setGravity(Gravity.CENTER);
                    tr.addView(b13);

                    Button b14=new Button(InfoActivity.this);
                    str1=json_data.getJSONObject("geometry").getJSONObject("location").getString("lat")+","+json_data.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    b14.setText("Visit");
                    b14.setOnClickListener(this);
                    b14.setTag(str1);
                    b14.setTextColor(Color.argb(100,0,128,128));
                    b14.setTextSize(20);
                    tr.addView(b14);

                    tv.addView(tr);
                    final View vline6 = new View(InfoActivity.this);
                    vline6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    vline6.setBackgroundColor(Color.GREEN);
                    tv.addView(vline6);  // add line below each row
                    tr=getTableRows(i);



                    TextView b5=new TextView(InfoActivity.this);
                    str="Business Status   ";
                    b5.setText(str);
                    b5.setTextColor(Color.argb(100,0,64,64));
                    b5.setTextSize(20);
                    //b5.setGravity(Gravity.CENTER);
                    tr.addView(b5);

                    TextView b6=new TextView(InfoActivity.this);
                    str1=json_data.getString("business_status");
                    b6.setText(str1);
                    b6.setTextColor(Color.argb(100,0,128,128));
                    b6.setTextSize(20);
                    //b6.setGravity(Gravity.CENTER);
                    tr.addView(b6);

                    tv.addView(tr);
                    final View vline2 = new View(InfoActivity.this);
                    vline2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    vline2.setBackgroundColor(Color.GREEN);
                    tv.addView(vline2);  // add line below each row
                    tr=getTableRows(i);


                    TextView b7=new TextView(InfoActivity.this);
                    str="Vicinity";
                    b7.setText(str);
                    b7.setTextColor(Color.argb(100,0,64,64));
                    b7.setTextSize(20);
                    //b7.setGravity(Gravity.CENTER);
                    tr.addView(b7);

                    TextView b8=new TextView(InfoActivity.this);
                    str1=json_data.getString("vicinity");
                    b8.setText(str1);
                    b8.setTextColor(Color.argb(100,0,128,128));
                    b8.setTextSize(20);
                    //b8.setGravity(Gravity.CENTER);
                    tr.addView(b8);

                    tv.addView(tr);
                    final View vline3 = new View(InfoActivity.this);
                    vline3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    vline3.setBackgroundColor(Color.GREEN);
                    tv.addView(vline3);   // add line below each row
                    tr=getTableRows(i);



                    TextView b9=new TextView(InfoActivity.this);
                    str="Business Type";
                    b9.setText(str);
                    b9.setTextColor(Color.argb(100,0,64,64));
                    b9.setTextSize(20);
                    //b9.setGravity(Gravity.CENTER);
                    tr.addView(b9);

                    TextView b10=new TextView(InfoActivity.this);
                    str1=json_data.getString("types");
                    b10.setText(str1);
                    b10.setTextColor(Color.argb(100,0,128,128));
                    b10.setTextSize(20);
                    //b10.setGravity(Gravity.CENTER);
                    tr.addView(b10);

                    tv.addView(tr);
                    final View vline4 = new View(InfoActivity.this);
                    vline4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    vline4.setBackgroundColor(Color.GREEN);
                    tv.addView(vline4);   // add line below each row
                    tr=getTableRows(i);


                    TextView b11=new TextView(InfoActivity.this);
                    str="Rating";
                    b11.setText(str);
                    b11.setTextColor(Color.argb(100,0,64,64));
                    b11.setTextSize(20);
                    //b11.setGravity(Gravity.CENTER);
                    tr.addView(b11);

                    TextView b12=new TextView(InfoActivity.this);
                    str1=json_data.getString("rating");
                    b12.setText(str1);
                    b12.setTextColor(Color.argb(100,0,128,128));
                    b12.setTextSize(20);
                    //b12.setGravity(Gravity.CENTER);
                    tr.addView(b12);
                    tv.addView(tr);

                    final View vline5 = new View(InfoActivity.this);
                    vline5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                    vline5.setBackgroundColor(Color.BLACK);
                    tv.addView(vline5);  // add line below each row
                }
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
            Toast.makeText(getApplicationContext(), "JsonArray fail", Toast.LENGTH_SHORT).show();
        }
    }
    public TableRow getTableRows(int cnt){
        TableRow tr=new TableRow(InfoActivity.this);

        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        if((cnt+2)%2==0) {
            tr.setBackgroundColor(Color.argb(100,195,195,195));
        }else{
            tr.setBackgroundColor(Color.WHITE);
        }
        return tr;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String str= (String) v.getTag();
        String[] location=str.split(",");
        intent.putExtra("lat",location[0]);
        intent.putExtra("lng",location[1]);
        intent.putExtra("latlng",str);
        setResult(Activity.RESULT_OK,intent);
        finish();//finishing activity
    }
}