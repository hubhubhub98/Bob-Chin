package com.example.bobchin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMeetingActivity extends AppCompatActivity implements View.OnClickListener {
    double latitude;
    double longitude;

    EditText titleEditText;
    EditText maxPeopleEditText;
    EditText contentEditText;

    Spinner spinnerFromAge;
    Spinner spinnerToAge;
    Spinner spinnerTimeHour;
    Spinner spinnerTimeMinute;
    Spinner spinnerDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        Button mapButton = findViewById(R.id.map_button);
        Button findBobchinButton = findViewById(R.id.button_find_bobchin);
        mapButton.setOnClickListener(this);
        findBobchinButton.setOnClickListener(this);

        spinnerFromAge = findViewById(R.id.spinner_form_age);
        spinnerToAge = findViewById(R.id.spinner_to_age);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFromAge.setAdapter(adapter);
        spinnerToAge.setAdapter(adapter);

        spinnerTimeHour = findViewById(R.id.spinner_time_hour);
        spinnerTimeMinute = findViewById(R.id.spinner_time_minute);
        spinnerDuration = findViewById(R.id.spinner_duration);

        adapter = ArrayAdapter.createFromResource(this, R.array.hour, android.R.layout.simple_spinner_item);
        spinnerTimeHour.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.minute, android.R.layout.simple_spinner_item);
        spinnerTimeMinute.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.duration, android.R.layout.simple_spinner_item);
        spinnerDuration.setAdapter(adapter);

        Intent intent = getIntent();

        latitude = intent.getDoubleExtra("Latitude", 0.0);
        longitude = intent.getDoubleExtra("Longitude", 0.0);

        TextView geoTextView = findViewById(R.id.text_geo);
        geoTextView.setText("선택한 좌표 : Latitude = " + latitude + ", Longitude = " + longitude);

        titleEditText = findViewById(R.id.edit_text_title);
        maxPeopleEditText = findViewById(R.id.edit_text_max_people);
        contentEditText = findViewById(R.id.edit_text_content);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.map_button) {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(intent);
            finish();
        }
        else if (view.getId() == R.id.button_find_bobchin) {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            SimpleDateFormat monthFormat = new SimpleDateFormat("mm", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

            String year = yearFormat.format(currentTime);
            String month = monthFormat.format(currentTime);
            String day = dayFormat.format(currentTime);

            // 서버로 보낼 값들
            String title;
            String content;
            int ageMax;
            int ageMin;
            String location;
            String startTime;
            double duration;
            int maxPeople;

            title = titleEditText.getText().toString();
            content = contentEditText.getText().toString();
            ageMax = Integer.parseInt(spinnerToAge.getSelectedItem().toString());
            ageMin = Integer.parseInt(spinnerFromAge.getSelectedItem().toString());
            location = longitude + ", " + latitude;
            startTime = year +  "-" + month + "-" + day + " " + spinnerTimeHour.getSelectedItem().toString() + ":" + spinnerTimeMinute.getSelectedItem().toString() + ":00";
            duration = Double.parseDouble(spinnerDuration.getSelectedItem().toString());
            maxPeople = Integer.parseInt(maxPeopleEditText.getText().toString());

            BobChin bobChin = (BobChin)getApplicationContext();
            // 서버 통신
            try {
                HttpPost httpPost = new HttpPost();
                String result = httpPost.execute("http://bobchin/api/addmeet.php", "token="+bobChin.getUserInfoObj().getUserAccessToken()+"&meetname="+title+"&meetmsg="+content+"&agemax="+ageMax+"&agemin="+ageMin+"&location="+location+"&starttime="+startTime+"&duration="+duration+"&maxpeople="+maxPeople).get();
                Toast.makeText(getApplicationContext(), "결과 : " + result, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 메인으로 돌아가는 코드
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbar,params);

        return true;
    }
}