package com.example.technote.TN_Network;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.core.content.ContextCompat;

import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.BaseExpandableAdapter;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class BoardUpload_Image extends AppCompatActivity
        implements View.OnClickListener{

    private static final String UPLOAD_URL = "http://yjpapp.com/insert_image.php";
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private ImageView imageView_image_upload;
    private ImageView imageView_image_list[] = new ImageView[5];
    private EditText etTitle, etContent, etPrice;
    private TextView wonText,etSubject;
    private Bitmap bitmap;
    private Uri[] filePath = new Uri[5] ;
    private LinearLayout choice_subject;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null,mChildListContent2 = null ,mChildListContent3 = null;
    private ExpandableListView mListView;

    private int image_count = 0, imageNum;
    private String[] path = new String[10];
    Toolbar image_upload_toolbar;
    private boolean subjectCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_upload_image);

        image_upload_toolbar = (Toolbar)findViewById(R.id.image_upload_toolbar);
        setSupportActionBar(image_upload_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바에 왼쪽버튼 추가하기
        getSupportActionBar().setTitle("사진 등록");

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

        // 가격을 입력하면 '\' 글씨가 검은색이 돼고, 입력하지 않은 상태면 회색 상태로 되게한다.
        TextWatcher watcher = new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s) {
                //텍스트 변경 후 발생할 이벤트를 작성.
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                //텍스트의 길이가 변경되었을 경우 발생할 이벤트를 작성.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
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

        choice_subject = (LinearLayout)findViewById(R.id.subject_layout);
        Intent intent = getIntent();
        mListView = (ExpandableListView)findViewById(R.id.elv_list);

        requestStoragePermission(); // 사용자에게 저장공간 접근을 허용하는 것을 묻는 함수

        //리스너 설정
        imageView_image_upload.setOnClickListener(this);
        choice_subject.setOnClickListener(this);
        for(int i=0;i<5;i++){
            imageView_image_list[i].setOnClickListener(this);
        }
    }
    @Override // 이전 버튼 리스너
    public void onBackPressed() {
        if(etContent.getText().toString().length()!=0 || subjectCheck  || etTitle.getText().toString().length()!=0){
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
                if(etSubject.getText().toString() == "카테고리"){
                    AlertDialog.Builder alert_confirm_subject = new AlertDialog.Builder(BoardUpload_Image.this);
                    alert_confirm_subject.setMessage("품목을 설정하세요.").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    return;
                                }
                            });
                    AlertDialog alert_subject = alert_confirm_subject.create();
                    alert_subject.show();
                    if(etTitle.length() == 0 && etSubject.getText().toString() !="카테고리"){
                        AlertDialog.Builder alert_confirm_title = new AlertDialog.Builder(BoardUpload_Image.this);
                        alert_confirm_title.setMessage("제목을 입력하세요.").setCancelable(false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 'YES'
                                        return;
                                    }
                                });
                        AlertDialog alert_title = alert_confirm_title.create();
                        alert_title.show();
                        if(etContent.length() == 0 && etTitle.length() != 0 && etSubject.getText().toString() !="카테고리"){
                            AlertDialog.Builder alert_confirm_content = new AlertDialog.Builder(BoardUpload_Image.this);
                            alert_confirm_content.setMessage("내용을 입력하세요.").setCancelable(false).setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 'YES'
                                            return;
                                        }
                                    });
                            AlertDialog alert_content = alert_confirm_content.create();
                            alert_content.show();
                        }
                    }
                }else if(etTitle.length() == 0 && etSubject.getText().toString() !="카테고리") {
                    AlertDialog.Builder alert_confirm_title = new AlertDialog.Builder(BoardUpload_Image.this);
                    alert_confirm_title.setMessage("제목을 입력하세요.").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    return;
                                }
                            });
                    AlertDialog alert_title = alert_confirm_title.create();
                    alert_title.show();
                }else if(etContent.length() == 0 && etTitle.length() != 0 && etSubject.getText().toString() !="카테고리"){
                    AlertDialog.Builder alert_confirm_content = new AlertDialog.Builder(BoardUpload_Image.this);
                    alert_confirm_content.setMessage("상품설명을 입력하세요.").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    return;
                                }
                            });
                    AlertDialog alert_content = alert_confirm_content.create();
                    alert_content.show();
                }else if(image_count == 0){
                    AlertDialog.Builder alert_confirm_image = new AlertDialog.Builder(BoardUpload_Image.this);
                    alert_confirm_image.setMessage("사진을 최소 한장이상 등록하세요.").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    return;
                                }
                            });
                    AlertDialog alert_image = alert_confirm_image.create();
                    alert_image.show();
                }else{
                    uploadMultipart();
                    AlertDialog.Builder alert_confirm_image = new AlertDialog.Builder(BoardUpload_Image.this);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //이미지를 선택하고 난 뒤 실행되는 함수
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath[image_count] = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath[image_count]);
                setImageView(image_count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onClick(View view) { //view에 대한 클릭 이벤트. setOnClickListener에 적용된다.
        if(view == imageView_image_upload){ // 맨 왼쪽 카메라 이미지뷰를 클릭한다.
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT); // 업로드 할 이미지를 선택하는 인텐트 창이 뜸
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE);
        }else if(view == choice_subject){ //카테고리를 누르면 진행되는 코드
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
                    //      Toast.LENGTH_SHORT).show();
                    etSubject.setText(be.getChild(groupPosition,childPosition));
                    etSubject.setTextColor(Color.parseColor("#000000"));
                    subjectCheck = true;
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
    protected void deleteImage(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardUpload_Image.this);
        alert_confirm.setMessage("업로드 이미지를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i =imageNum;i<image_count-1;i++){
                            try {
                                filePath[i] = filePath[i+1];
                                imageView_image_list[i].setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), filePath[i+1]));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    public void uploadMultipart() { //업로드 버튼을 누르면 실행되는 함수
        String title = etTitle.getText().toString().trim();
        String price = etPrice.getText().toString().trim() + "원";
        String subject = etSubject.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        //이미지의 실제 경로를 String Array인 path[i]에 저장
        for(int i = 0; i<image_count;i++){
            path[i] = getPath(filePath[i]);
        }


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
                if(image_count == 0){
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .setUtf8Charset()
                        .addParameter("title", title) //Adding text parameter to the request
                        .addParameter("price",price)
                        .addParameter("subject",subject)
                        .addParameter("content",content)
                        .addParameter("image_count","0")
                        .setNotificationConfig(new UploadNotificationConfig().setCompletedMessage("업로드 완료"))
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
                }else if(image_count==1){
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .setUtf8Charset()
                        .addFileToUpload(path[0], "image") //Adding file
                        .addParameter("title", title) //Adding text parameter to the request
                        .addParameter("price",price)
                        .addParameter("subject",subject)
                        .addParameter("content",content)
                        .addParameter("image_count","1")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
            }else if(image_count==2){
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .setUtf8Charset()
                        .addFileToUpload(path[0], "image") //Adding file
                        .addFileToUpload(path[1],"image2")
                        .addParameter("title", title) //Adding text parameter to the request
                        .addParameter("price",price)
                        .addParameter("subject",subject)
                        .addParameter("content",content)
                        .addParameter("image_count","2")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
            }else if(image_count==3){
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .setUtf8Charset()
                        .addFileToUpload(path[0], "image") //Adding file
                        .addFileToUpload(path[1],"image2")
                        .addFileToUpload(path[2],"image3")
                        .addParameter("title", title) //Adding text parameter to the request
                        .addParameter("price",price)
                        .addParameter("subject",subject)
                        .addParameter("content",content)
                        .addParameter("image_count","3")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
            }else if(image_count == 4){
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .setUtf8Charset()
                        .addFileToUpload(path[0], "image") //Adding file
                        .addFileToUpload(path[1],"image2")
                        .addFileToUpload(path[2],"image3")
                        .addFileToUpload(path[3], "image4")
                        .addParameter("title", title) //Adding text parameter to the request
                        .addParameter("price",price)
                        .addParameter("subject",subject)
                        .addParameter("content",content)
                        .addParameter("image_count","4")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
            }else if(image_count == 5){
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .setUtf8Charset()
                        .addFileToUpload(path[0], "image") //Adding file
                        .addFileToUpload(path[1],"image2")
                        .addFileToUpload(path[2],"image3")
                        .addFileToUpload(path[3], "image4")
                        .addFileToUpload(path[4], "image5")
                        .addParameter("title", title) //Adding text parameter to the request
                        .addParameter("price",price)
                        .addParameter("subject",subject)
                        .addParameter("content",content)
                        .addParameter("image_count","5")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
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
    public void setImageView(int i){
        imageView_image_list[i].setImageBitmap(bitmap);
        imageView_image_list[i].setScaleType(ImageView.ScaleType.FIT_XY); // 이미지 비율맞게 꽉 채움\
        image_count++;
    }
}