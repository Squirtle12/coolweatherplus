package com.example.aaa.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aaa.coolweather.service.AutoUpdateService;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button sure;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sure=(Button)findViewById(R.id.sure);
        sure.setOnClickListener(this);
        editText=(EditText)findViewById(R.id.edit_text);

    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sure:
                String time=editText.getText().toString();
                if(time.length()==0) {
                    Toast.makeText(this,"输入不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                String hour="小时";
                String minute="分钟";
                if (!time.contains(hour)||!time.contains(minute))
                {
                    Toast.makeText(this,"输入格式错误",Toast.LENGTH_SHORT).show();
                    break;
                }

                String []times=time.split(" ");
                int hours=Integer.valueOf(times[0].substring(0,times[0].length()-2));
                int minutes=Integer.valueOf(times[1].substring(0,times[1].length()-2));
                if (hours==0&&minutes==0)
                {
                    Toast.makeText(this,"时间不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                SharedPreferences.Editor editor= PreferenceManager.
                        getDefaultSharedPreferences(this)
                        .edit();
                editor.putString("times",hours+"/"+minutes);
                editor.apply();
                //Intent intent=new Intent(this, AutoUpdateService.class);
                //startService(intent);
                Intent intent2=new Intent(this,WeatherActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
