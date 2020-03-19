package com.example.technote.TN_Network;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.BaseExpandableAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class BoardUpload_Image extends AppCompatActivity
        implements View.OnClickListener{

    private static final String UPLOAD_URL = "http://yjpapp.com/insert_image.php";
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private ImageView imageView_image_upload;
    private ImageView imageView_image_list[] = new ImageView[5];
    private EditText etTitle, etContent, etPrice;
    private TextView wonText,etSubject;
    private Uri[] fileUriPath = new Uri[5] ;
    private LinearLayout choice_subject;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null,mChildListContent2 = null ,mChildListContent3 = null;
    private ExpandableListView mListView;
    private File[] imageFile = new File[5];

    private int image_count = 0, imageNum, images_size = 0;
    Toolbar image_upload_toolbar;
    private boolean uploadComplete = false, subjectSelect = false;
    private String[] filePath = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_upload_image);

        initView(); //findViewById()

        setSupportActionBar(image_upload_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바에 왼쪽버튼 추가하기
        getSupportActionBar().setTitle("사진 등록");

        // 가격을 입력하면 '\' 글씨가 검은색이 돼고, 입력하지 않은 상태면 회색 상태로 되게한다.
        TextWatcher watcher = new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                //텍스트 변경 후 발생할 이벤트를 작성.
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //텍스트의 길이가 변경되었을 경우 발생할 이벤트를 작성.
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //텍스트가 변경될때마다 발생할 이벤트를 작성.
                if (etPrice.isFocusable())
                {
                    //mXMLBuyCount EditText 가 포커스 되어있을 경우에만 실행됩니다.
                    if(s.length()==0){
                        wonText.setTextColor(Color.parseColor("#C0C0C0"));
                    }else {
                        wonText.setTextColor(Color.parseColor("#000000"));
                    }
                }
            }
        };
        etPrice.addTextChangedListener(watcher);//\표시가 가격을 입력하면 검은글씨 입력하지 않으면 흐린글씨로 설정

        requestStoragePermission(); // 사용자에게 저장공간 접근을 허용하는 것을 묻는 함수

        //리스너 설정
        imageView_image_upload.setOnClickListener(this);
        choice_subject.setOnClickListener(this);
        for(int i=0;i<5;i++){
            imageView_image_list[i].setOnClickListener(this);
        }
    }
    @Override // 툴바 메뉴 설정하기
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_network_board_image_upload,menu);
        return true;
    }
    //툴바 아이콘 클릭 이벤트
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true; // 다른 버튼이 중복 클릭 되지않게 한다.
            case R.id.upload: //등록 버튼을 누르면
                //네트워크 상태 체크
                ConnectivityManager cm =
                        (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if(subjectSelect && etTitle.length() != 0 && etContent.length() != 0 && isConnected) { // 모든 조건 충족
                    uploadMultipart();
                    uploadComplete = true;
                    createNotificationChannel();
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext(),"0")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentTitle("TechNote")
                                    .setSmallIcon(R.drawable.tech_note_icon)
                                    .setContentText("업로드 됐습니다.");
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                    notificationManagerCompat.notify(0,mBuilder.build());
                    setDialogMessage("등록 완료 됐습니다.");

                }else{ // 예외처리
                    if(!isConnected){
                        setDialogMessage("네트워크 상태를 확인하세요.");
                    }else if(!subjectSelect){
                        setDialogMessage("품목을 선택하세요.");
                    }else if (etTitle.length() == 0 && subjectSelect){
                        setDialogMessage("제목을 입력하세요.");
                    }else if(etContent.length() == 0 && etTitle.length() != 0 && subjectSelect){
                        setDialogMessage("상품 설명을 입력하세요.");
                    }else if(image_count==0){
                        setDialogMessage("이미지는 최소 한장이상 선택해주세요.");
                    }
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override // 이전 버튼 리스너
    public void onBackPressed() {
        if(etContent.getText().toString().length()!=0 || subjectSelect  || etTitle.getText().toString().length()!=0){
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardUpload_Image.this);
            alert_confirm.setMessage("작성중인 게시물이 있습니다. 작성을 취소 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            // 'YES'
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 'No'
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }else{
            finish();
        }
    }
    @Override
    public void onClick(View view) { //ViewClick에 대한 클릭 이벤트.
        if(view == imageView_image_upload){ // 맨 왼쪽 카메라 이미지뷰를 클릭한다.
            if(image_count<=5){
                MultiImageSelector.create(this)
                        .multi()
                        .count(5-image_count)
                        .start(this,IMAGE_REQUEST_CODE);
                /*
                ImagePicker.create(this)
                        .folderMode(true)
                        .multi()
                        .showCamera(true)
                        .limit(5-image_count)
                        .start();

                 */

                //intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                //intent.setAction(Intent.ACTION_GET_CONTENT); // 업로드 할 이미지를 선택하는 인텐트 창이 뜸
                //startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE);

            }else{
                Toast.makeText(this, "이미지를 더 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(view == choice_subject){ //카테고리를 누르면 진행되는 코드
            loadSelectSubject();
        }else if(view == imageView_image_list[0]){ //첫 번째 업로드 이미지를 클릭했을 때 삭제하는 코드
            if(image_count>0){
                imageNum = 0;
                deleteImage();
            }
        }else if(view == imageView_image_list[1]) { //두 번째 업로드 이미지를 삭제하는 코드
            if(image_count>1){
                imageNum = 1;
                deleteImage();
            }
        }else if(view == imageView_image_list[2]) { //세 번째 업로드 이미지를 삭제하는 코드
            if(image_count>2){
                imageNum = 2;
                deleteImage();
            }
        }else if(view == imageView_image_list[3]) { //네 번째 업로드 이미지를 삭제하는 코드
            if(image_count>3){
                imageNum = 3;
                deleteImage();
            }
        }else if(view == imageView_image_list[4]) { //다섯 번째 업로드 이미지를 삭제하는 코드
            if(image_count>4){
                imageNum = 4;
                deleteImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE) {
            // Get a list of picked images
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            //List<Image> images = ImagePicker.getImages(data);
            images_size += path.size();
            Log.d("ImagePickerResult",path.get(0));
            Log.d("ImagePickerResult",String.valueOf(images_size));


            for(int i = 0;i<path.size();i++){
                imageFile[image_count] = new File(path.get(i));
                filePath[image_count] = path.get(i);
                imageView_image_list[image_count].setImageBitmap(pathToBitmap(path.get(i)));
                imageView_image_list[image_count].setScaleType(ImageView.ScaleType.FIT_XY); // 이미지 비율맞게 꽉 채움
                image_count++;
            }


        }
    }
    public void uploadMultipart() { //업로드 버튼을 누르면 실행되는 함수
        String title = etTitle.getText().toString().trim();
        String price = etPrice.getText().toString().trim() + "원";
        String subject = etSubject.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        ContentResolver contentResolver = getContentResolver();
        //이미지의 실제 경로를 String Array인 path[i]에 저장

        for(int i = 0; i<image_count;i++){
            //imageFile[i] = new File(getPath(fileUriPath[i]));
            //imageFile[i] = new File(contentResolver.canonicalize(fileUriPath[i]).d);
        }

        //Uploading code
        try {
            //Creating a multi part request
            if(image_count==1){
                AndroidNetworking.upload(UPLOAD_URL)
                        .addMultipartFile("image", imageFile[0])
                        .addMultipartParameter("title", title) //Adding text parameter to the request
                        .addMultipartParameter("price",price)
                        .addMultipartParameter("subject",subject)
                        .addMultipartParameter("content",content)
                        .addMultipartParameter("image_count","1")
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {
                            }
                        })
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("UploadResult","onResponse");
                            }
                            @Override
                            public void onError(ANError anError) {
                                Log.d("UploadResult","OnError : " + anError.toString());
                            }
                        });
            }else if(image_count==2){
                AndroidNetworking.upload(UPLOAD_URL)
                        .addMultipartFile("image", imageFile[0])
                        .addMultipartFile("image2", imageFile[1])
                        .addMultipartParameter("title", title) //Adding text parameter to the request
                        .addMultipartParameter("price",price)
                        .addMultipartParameter("subject",subject)
                        .addMultipartParameter("content",content)
                        .addMultipartParameter("image_count","2")
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
            }else if(image_count==3){
                AndroidNetworking.upload(UPLOAD_URL)
                        .addMultipartFile("image", imageFile[0])
                        .addMultipartFile("image2", imageFile[1])
                        .addMultipartFile("image3", imageFile[2])
                        .addMultipartParameter("title", title) //Adding text parameter to the request
                        .addMultipartParameter("price",price)
                        .addMultipartParameter("subject",subject)
                        .addMultipartParameter("content",content)
                        .addMultipartParameter("image_count","3")
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
            }else if(image_count == 4){
                AndroidNetworking.upload(UPLOAD_URL)
                        .addMultipartFile("image", imageFile[0])
                        .addMultipartFile("image2", imageFile[1])
                        .addMultipartFile("image3", imageFile[2])
                        .addMultipartFile("image4", imageFile[3])
                        .addMultipartParameter("title", title) //Adding text parameter to the request
                        .addMultipartParameter("price",price)
                        .addMultipartParameter("subject",subject)
                        .addMultipartParameter("content",content)
                        .addMultipartParameter("image_count","4")
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
            }else if(image_count == 5){
                AndroidNetworking.upload(UPLOAD_URL)
                        .addMultipartFile("image", imageFile[0])
                        .addMultipartFile("image2", imageFile[1])
                        .addMultipartFile("image3", imageFile[2])
                        .addMultipartFile("image4", imageFile[3])
                        .addMultipartFile("image5", imageFile[4])
                        .addMultipartParameter("title", title) //Adding text parameter to the request
                        .addMultipartParameter("price",price)
                        .addMultipartParameter("subject",subject)
                        .addMultipartParameter("content",content)
                        .addMultipartParameter("image_count","5")
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
            }
            image_count = 0;
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) { //이미지를 선택하면
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

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //이전에 사용자가 권한을 거부 한 경우 코드가 이 블록에 올 것입니다.
            //여기서 왜 이 권한이 필요한지 설명 할 수 있습니다.
            //이 권한이 필요한 이유를 여기에서 설명하십시오.
        }
        //마지막으로 허가를 요청하십시오.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "파일 접근이 허용됐습니다", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void setDialogMessage(String s){
        AlertDialog.Builder alert_confirm_subject = new AlertDialog.Builder(BoardUpload_Image.this);
        alert_confirm_subject.setMessage(s).setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                       finish();
                    }
                });
        AlertDialog alert_subject = alert_confirm_subject.create();
        alert_subject.show();
    }
    protected void deleteImage(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardUpload_Image.this);
        alert_confirm.setMessage("업로드 이미지를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i =imageNum;i<image_count-1;i++){
                            filePath[i] = filePath[i+1];
                            imageView_image_list[i].setImageBitmap(pathToBitmap(filePath[i+1]));
                        }
                        imageView_image_list[image_count-1].setImageResource(R.drawable.network_board_upload_image_list);
                        imageView_image_list[image_count-1].setScaleType(ImageView.ScaleType.FIT_START);
                        filePath[image_count-1] = null;
                        image_count--;
                        // 'YES'
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }
    public void initView(){
        imageView_image_upload = (ImageView)findViewById(R.id.upload_click_image);
        imageView_image_list[0] = (ImageView)findViewById(R.id.image1);
        imageView_image_list[1] = (ImageView)findViewById(R.id.image2);
        imageView_image_list[2] = (ImageView)findViewById(R.id.image3);
        imageView_image_list[3] = (ImageView)findViewById(R.id.image4);
        imageView_image_list[4] = (ImageView)findViewById(R.id.image5);
        etPrice = (EditText)findViewById(R.id.etPrice);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);
        etSubject = (TextView)findViewById(R.id.subject_textview);
        wonText = (TextView)findViewById(R.id.wonText);
        image_upload_toolbar = (Toolbar)findViewById(R.id.image_upload_toolbar);
        choice_subject = (LinearLayout)findViewById(R.id.subject_layout);
        mListView = (ExpandableListView)findViewById(R.id.elv_list);
    }
    public void loadSelectSubject(){
        mGroupList = new ArrayList<String>();

        mChildList = new ArrayList<ArrayList<String>>();

        mChildListContent = new ArrayList<String>();
        mChildListContent2 = new ArrayList<String>();
        mChildListContent3 = new ArrayList<String>();

        mGroupList.add("디지털/가전"); mGroupList.add("도서/티켓/음반/문구"); mGroupList.add("생활/가공식품");

        mChildListContent.add("TV");
        mChildListContent.add("컴퓨터/노트북");
        mChildListContent.add("컴퓨터기기");
        mChildListContent.add("모니터");
        mChildListContent.add("핸드폰");
        mChildListContent.add("가전");
        mChildListContent.add("기타 : 디지털/가전");

        mChildListContent2.add("도서");
        mChildListContent2.add("티켓");
        mChildListContent2.add("음반");
        mChildListContent2.add("문구");
        mChildListContent2.add("기타 : 도서/티켓/음반/문구");

        mChildListContent3.add("생활");
        mChildListContent3.add("가구");
        mChildListContent3.add("옷");
        mChildListContent3.add("가공식품");
        mChildListContent3.add("기타 : 생활/가공식품");

        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent2);
        mChildList.add(mChildListContent3);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("카테고리 선택");

        ExpandableListView myList = new ExpandableListView(this);

        final BaseExpandableAdapter be = new BaseExpandableAdapter(this, mGroupList, mChildList);
        myList.setAdapter(be);

        builder.setView(myList);
        myList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Toast.makeText(getApplicationContext(), " " + groupPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        final android.app.AlertDialog dialog = builder.create();
        dialog.show();
        myList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //Toast.makeText(getApplicationContext(),be.getChild(groupPosition,childPosition),
                //                    //      Toast.LENGTH_SHORT).show();
                etSubject.setText(be.getChild(groupPosition,childPosition));
                etSubject.setTextColor(Color.parseColor("#000000"));
                subjectSelect = true;
                dialog.dismiss();
                return false;
            }
        });

        myList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition,
                //Toast.LENGTH_SHORT).show();
            }
        });

        myList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition,
                //Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public Bitmap pathToBitmap(String path) {
        Bitmap bitmap=null;
        try {
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }
}