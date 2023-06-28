package com.police.demonstration.manage_demonstration.record;

import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;

public class record_check extends AppCompatActivity {

    TextView measeaureTime_s;
    TextView measeaureTime_e;
    TextView Location;
    TextView detail_location;
    TextView measureing_distance;
    TextView wind_per_second;
    TextView equal_1;
    TextView max_1;
    TextView equal_2;
    TextView max_2;
    TextView eqaal_3;
    TextView max_3;

    AppCompatImageButton back_button;

    AppCompatButton notify;

    AppCompatButton delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_check);

        DemonstrationInfo demonstrationInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);
        MeasurementInfo measurementInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_MEASUREMENT);

        measeaureTime_s = findViewById(R.id.recorded_time_s);
        measeaureTime_e = findViewById(R.id.recorded_time_e);
        Location = findViewById(R.id.Location);
        detail_location = findViewById(R.id.Detail_Location);
        measureing_distance = findViewById(R.id.measuring_distance);
        wind_per_second = findViewById(R.id.wind_per_second);
        equal_1 = findViewById(R.id.Mesuare_max);
        max_1 = findViewById(R.id.Mesuare_equal);
        equal_2 = findViewById(R.id.CV_eqaul);
        max_2 = findViewById(R.id.CV_max);
        eqaal_3 = findViewById(R.id.SNL_equal);
        max_3 = findViewById(R.id.SNL_max);
        back_button = findViewById(R.id.record_check_backbutton);
        notify = findViewById(R.id.notification);


        measeaureTime_s.setText(measurementInfo.getStartTime());
        measeaureTime_e.setText(measurementInfo.getEndTime());
        Location.setText(measurementInfo.getPlace());
        detail_location.setText(measurementInfo.getDetailPlace());
        measureing_distance.setText(measurementInfo.getDistance());
        wind_per_second.setText(measurementInfo.getWinterSpeed());
        equal_1.setText(measurementInfo.getMeasurementEquivalent());
        max_1.setText(measurementInfo.getMeasurementHighest());
        equal_2.setText(measurementInfo.getCorrectionEquivalent());
        max_2.setText(measurementInfo.getMeasurementHighest());
        eqaal_3.setText(demonstrationInfo.getStandardEquivalent());
        max_3.setText(demonstrationInfo.getStandardHighest());


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //여기에 고지창 띄우기
            }
        });




    }


}