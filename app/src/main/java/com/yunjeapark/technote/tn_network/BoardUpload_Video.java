package com.yunjeapark.technote.tn_network;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.yunjeapark.technote.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class BoardUpload_Video extends AppCompatActivity implements View.OnClickListener {
    private int VIDEO_REQUST_CODE = 2;
    private final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE = 12345; //Some random number

    private static final String UPLOAD_URL = "http://yjpapp.com/insert_video.php";
    private ImageView imageView_video_upload;

    private Toolbar toolbar;
    private EditText etTitle, etContent;
    private Uri videoFileUriPath, thumbnailFileUriPath;
    private Bitmap thumbNailBitmap;
    private Context context;
    private File videoFile, thumbnailFile;
    private boolean uploadComplete = false;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_network_board_upload_video);

        context = getApplicationContext();
        initView();
        imageView_video_upload.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바에 왼쪽버튼 추가하기
        getSupportActionBar().setTitle("동영상 등록");

        if (ActivityCompat.checkSelfPermission(BoardUpload_Video.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(BoardUpload_Video.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BoardUpload_Video.this, permissions, REQUEST_CODE);
        }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //이미지를 선택하고 난 뒤 실행되는 함수
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Log.d("VideoPath",getVideoPath(videoFileUriPath));
            Log.d("VideoPath","getData.getPath : " + data.getData().getPath());
            thumbNailBitmap = ThumbnailUtils.createVideoThumbnail(getVideoPath(videoFileUriPath),MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            imageView_video_upload.setImageBitmap(thumbNailBitmap);
            imageView_video_upload.setScaleType(ImageView.ScaleType.FIT_XY); // 이미지 비율맞게 꽉 채움
            thumbnailFileUriPath = bitmapToUri(getApplicationContext(),thumbNailBitmap);
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
                //네트워크 상태 체크
                ConnectivityManager cm =
                        (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if( etTitle.length() != 0 && etContent.length() != 0 && isConnected) { // 모든 조건 충족
                    uploadVideo();
                    uploadComplete = true;

                    createNotificationChannel();
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext(),"1")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentTitle("TechNote")
                                    .setSmallIcon(R.drawable.tech_note_icon)
                                    .setContentText("비디오가 업로드 됐습니다.");
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                    notificationManagerCompat.notify(0,mBuilder.build());
                    setDialogMessage("등록 완료 됐습니다.");

                }else{ // 예외처리
                    if(!isConnected){
                        setDialogMessage("네트워크 상태를 확인하세요.");
                    }else if (etTitle.length() == 0){
                        setDialogMessage("제목을 입력하세요.");
                    }else if(etContent.length() == 0 && etTitle.length() != 0){
                        setDialogMessage("동영상 설명을 입력하세요.");
                    }
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
    public Uri bitmapToUri(Context inContext, Bitmap inImage) { // Bitmap을 Uri로 변경하는 함수
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public void uploadVideo() { //업로드 버튼을 누르면 실행되는 함수
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        videoFile = new File(getVideoPath(videoFileUriPath));
        thumbnailFile = new File(getImagePath(thumbnailFileUriPath));

        try {
            AndroidNetworking.upload(UPLOAD_URL)
                    .addMultipartFile("video",videoFile)
                    .addMultipartFile("thumbnail",thumbnailFile)
                    .addMultipartParameter("title", title) //Adding text parameter to the request
                    .addMultipartParameter("content",content)
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            // do anything with progress
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            Log.d("UploadResult","onResponse");
                        }
                        @Override
                        public void onError(ANError error) {
                            Log.d("UploadResult","OnError : " + error.toString());
                        }
                    });
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("UploadState", exc.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(BoardUpload_Video.this, "Permission granted!!!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(BoardUpload_Video.this, "Necessary permissions not granted...", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    public void setDialogMessage(String s){
        AlertDialog.Builder alert_confirm_subject = new AlertDialog.Builder(BoardUpload_Video.this);
        alert_confirm_subject.setMessage(s).setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        if (uploadComplete){
                            finish();
                            return;
                        }else {
                            return;
                        }
                    }
                });
        AlertDialog alert_subject = alert_confirm_subject.create();
        alert_subject.show();
    }
    public void initView(){
        etTitle = (EditText)findViewById(R.id.video_upload_etTitle);
        etContent = (EditText)findViewById(R.id.video_upload_etContent);
        imageView_video_upload = (ImageView)findViewById(R.id.imageview_video_upload);
        toolbar = findViewById(R.id.video_upload_toolbar);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
