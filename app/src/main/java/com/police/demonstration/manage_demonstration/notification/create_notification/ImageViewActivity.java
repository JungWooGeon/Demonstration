package com.police.demonstration.manage_demonstration.notification.create_notification;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.police.demonstration.R;

public class ImageViewActivity extends AppCompatActivity {
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private float lastTouchX, lastTouchY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.imageView);

        Uri imageUrl = getIntent().getData();
        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }

        // 이미지뷰에 대한 초기 설정
        imageView.setOnTouchListener((v, event) -> {
            // 제스처 이벤트를 처리하는 ScaleGestureDetector에 이벤트 전달
            scaleGestureDetector.onTouchEvent(event);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 이미지뷰를 터치한 순간의 좌표 저장
                    lastTouchX = event.getRawX();
                    lastTouchY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 현재 좌표와 이전 좌표의 차이를 구하여 이미지뷰 이동
                    float dx = event.getRawX() - lastTouchX;
                    float dy = event.getRawY() - lastTouchY;
                    imageView.setX(imageView.getX() + dx);
                    imageView.setY(imageView.getY() + dy);

                    // 이동한 후 현재 좌표를 이전 좌표로 갱신
                    lastTouchX = event.getRawX();
                    lastTouchY = event.getRawY();
                    break;
            }
            return true;
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(@NonNull ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();

                // 최소 0.5, 최대 2배
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 3.0f));

                // 이미지에 적용
                imageView.setScaleX(scaleFactor);
                imageView.setScaleY(scaleFactor);

                return true;
            }
        });
    }
}
