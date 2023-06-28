package com.police.demonstration.manage_demonstration.notification.create_notification;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityNotificationDocumentV2Binding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NotificationDocumentV2 extends AppCompatActivity {


    private ActivityNotificationDocumentV2Binding binding;
    private Uri uri;
    private int num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification_document_v2);
        binding.setActivity(this);

        uri = Uri.parse(getIntent().getStringExtra("Image_file_url"));
        num = getIntent().getIntExtra("number",1);
        String number = "[" + num + "]번고지";

        binding.title.setText(number);

        loadImage(uri);

        binding.backButton.setOnClickListener(v -> finish());

        binding.download.setOnClickListener(v -> saveImageToGallery(uri, num));
    }
    private void loadImage(Uri uri) {
        // 이미지 로딩이 시작되기 전에 로딩 화면을 표시
        binding.progressBar.setVisibility(View.VISIBLE);

        // Glide를 사용하여 이미지를 로딩
        Glide.with(this)
                .load(uri)
                .listener(new RequestListener<>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // 이미지 로딩 실패 시 경고 토스트 메시지 출력 후 화면 종료
                        binding.progressBar.setVisibility(View.GONE); // 로딩 화면 숨김
                        Toast.makeText(binding.getActivity(), getString(R.string.plz_retry_connect_network), Toast.LENGTH_SHORT).show();
                        finish();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 이미지 로딩 성공 시 프로그래스바 숨김, 화면 버튼 클릭 활성화
                        binding.progressBar.setVisibility(View.GONE); // 로딩 화면 숨김
                        binding.download.setEnabled(true); // '고지' 버튼 활성화

                        // 이미지 뷰 클릭 이벤트
                        binding.notificationImage.setOnClickListener(e -> {
                            // ImageViewActivity 로 전환
                            Intent intent = new Intent(binding.getActivity(), ImageViewActivity.class);
                            intent.setData(uri);
                            startActivity(intent);
                        });
                        return false;
                    }
                })
                .into(binding.notificationImage);
    }

    private void saveImageToGallery(Uri imageUri, int num) {
        // 갤러리에 저장할 때 사용할 파일명 생성
        String fileName = "demonstration_" + num + "_image.jpg";

        // 저장될 파일 경로 설정
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);

        // 이미지를 저장하기 위한 OutputStream 생성
        OutputStream outputStream;

        try {
            outputStream = new FileOutputStream(imageFile);

            // 이미지 파일로 복사
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            // 갤러리 갱신
            MediaScannerConnection.scanFile(this,
                    new String[]{imageFile.getAbsolutePath()},
                    new String[]{"image/jpeg"}, null);

            // 저장 완료 메시지 표시
            Toast.makeText(this, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
