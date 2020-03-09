package com.example.technote.TN_Network;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.technote.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BoardVideoUpload extends AppCompatActivity implements View.OnClickListener {
    private int VIDEO_REQUST_CODE = 2;
    private final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE = 12345; //Some random number

    private static final String UPLOAD_URL = "http://yjpapp.com/insert_video.php";
    private ImageView imageView_video_upload;

    private Toolbar toolbar;
    private EditText etTitle, etContent;
    private Uri videoFilePath, thumbnailFilePath;
    private Bitmap thumbNailBitmap;
    private String imagePath, thumbnailPath;
    private Context context;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_network_board_video_upload);

        context = getApplicationContext();
        initView();
        imageView_video_upload.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바에 왼쪽버튼 추가하기
        getSupportActionBar().setTitle("동영상 등록");

        if (ActivityCompat.checkSelfPermission(BoardVideoUpload.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(BoardVideoUpload.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BoardVideoUpload.this, permissions, REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //이미지를 선택하고 난 뒤 실행되는 함수
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoFilePath = data.getData();

            thumbNailBitmap = ThumbnailUtils.createVideoThumbnail(getVideoPath(videoFilePath),MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            imageView_video_upload.setImageBitmap(thumbNailBitmap);
            imageView_video_upload.setScaleType(ImageView.ScaleType.FIT_XY); // 이미지 비율맞게 꽉 채움
            thumbnailFilePath = getImageUri(getApplicationContext(),thumbNailBitmap);
        }
    }
    @Override
    public void onClick(View view) {
        if (view == imageView_video_upload) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT); // 업로드 할 비디오를 선택하는 인텐트 창이 뜸
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), VIDEO_REQUST_CODE);
        }
    }
    @Override // 툴바 메뉴 설정하기
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_network_board_video_upload,menu);
        return true;
    }

    //툴바 아이콘 클릭 이벤트
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true; // 다른 버튼이 중복 클릭 되지않게 한다.
            case R.id.video_upload: //등록 버튼을 누르면
                if(etTitle.length() ==0){
                    AlertDialog.Builder alert_confirm_subject = new AlertDialog.Builder(BoardVideoUpload.this);
                    alert_confirm_subject.setMessage("제목을 입력하세요.").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    return;
                                }
                            });
                    AlertDialog alert_subject = alert_confirm_subject.create();
                    alert_subject.show();
                    if(etContent.length() == 0) {
                        AlertDialog.Builder alert_confirm_title = new AlertDialog.Builder(BoardVideoUpload.this);
                        alert_confirm_title.setMessage("내용을 입력하세요.").setCancelable(false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 'YES'
                                        return;
                                    }
                                });
                    }
                }else{
                    uploadMultipart();
                    AlertDialog.Builder alert_confirm_image = new AlertDialog.Builder(BoardVideoUpload.this);
                    alert_confirm_image.setMessage("등록 완료 됐습니다.").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    finish();
                                    return;
                                }
                            });
                    AlertDialog alert_image = alert_confirm_image.create();
                    alert_image.show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getVideoPath(Uri uri) { //Video uri값을 String 값으로 변환하는 함수
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }
    public String getImagePath(Uri uri) { //Image uri값을 String 값으로 변환하는 함수
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) { // Bitmap을 Uri로 변경하는 함수
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("getImageUri",path);
        return Uri.parse(path);
    }
    public void uploadMultipart() { //업로드 버튼을 누르면 실행되는 함수
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        //이미지의 실제 경로를 String Array인 imagePath[i]에 저장
        imagePath = getVideoPath(videoFilePath);
        thumbnailPath = getImagePath(thumbnailFilePath);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .setUtf8Charset()
                    .addFileToUpload(imagePath, "video") //Adding file
                    .addFileToUpload(thumbnailPath,"thumbnail")
                    .addParameter("title", title) //Adding text parameter to the request
                    .addParameter("content",content)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("UploadState", exc.getMessage());
        }
    }

    public void initView(){
        etTitle = (EditText)findViewById(R.id.video_upload_etTitle);
        etContent = (EditText)findViewById(R.id.video_upload_etContent);
        imageView_video_upload = (ImageView)findViewById(R.id.imageview_video_upload);
        toolbar = findViewById(R.id.video_upload_toolbar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(BoardVideoUpload.this, "Permission granted!!!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(BoardVideoUpload.this, "Necessary permissions not granted...", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
