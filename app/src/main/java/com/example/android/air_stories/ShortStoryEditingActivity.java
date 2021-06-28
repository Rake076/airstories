package com.example.android.air_stories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class ShortStoryEditingActivity extends AppCompatActivity{

        private IARE_Toolbar mToolbar;
        private AREditText mEditText;
        private boolean scrollerAtEnd;

        TextView title_textview;
        MaterialButton publish_btn;
        String title, description, story, type, genre, username = "abc";
        int userID = 0;

        ImageView imageView;
        Bitmap shortImage;

        Uri imageUri;

        MultipartBody.Part fileBody;

        Bitmap bmp;

        Retrofit retrofit;
        INodeJS myAPI;
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        MultipartBody.Part file;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.writing_activity);

            Intent intent = getIntent();
            initToolbar();

            title_textview = findViewById(R.id.title_textview);
            mEditText = findViewById(R.id.arEditText);



//            type = intent.getStringExtra("type");
            genre = intent.getStringExtra("genre");
            title = intent.getStringExtra("title");
            description = intent.getStringExtra("description");
            shortImage = intent.getParcelableExtra("shortImage");
            bmp = intent.getParcelableExtra("shortImage");
//            username = intent.getStringExtra("username");
//            userID = intent.getIntExtra("userID", 0);
            story = intent.getStringExtra("story");
            int shortID = intent.getIntExtra("shortID", 0);


            mEditText.fromHtml(story);
            title_textview.setText(title);


            // Init API
            retrofit = RetrofitClient.getInstance();
            myAPI = retrofit.create(INodeJS.class);

            File coverImage = persistImage(bmp, "cover");
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), coverImage);
            fileBody = MultipartBody.Part.createFormData("cover", coverImage.getName(), requestFile);

            publish_btn = findViewById(R.id.publish_btn);
            publish_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mEditText.getText().toString().length() < 200) {
                        mEditText.setError("Story must be at least 200 characters long");
                    }
                    else {
                        story = mEditText.getHtml();
                        editShortStory(shortID, title, story, genre, description, fileBody);
                        finish();
                    }

                }
            });

        }


    private void editShortStory(int shortID, String title, String shortStory, String shortGenre, String shortDescription, MultipartBody.Part fileBody) {
        compositeDisposable.add(myAPI.editShortStory(shortID, title, shortStory, shortGenre, shortDescription, fileBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if(responseBody.toString().contains("successfully")){
                            Toast.makeText(getApplicationContext(), "Success: " + responseBody.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Short Story has been edited successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }


    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }



        private void initToolbar() {
            mToolbar = this.findViewById(R.id.areToolbar);
            IARE_ToolItem bold = new ARE_ToolItem_Bold();
            IARE_ToolItem italic = new ARE_ToolItem_Italic();
            IARE_ToolItem underline = new ARE_ToolItem_Underline();
            IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
            IARE_ToolItem quote = new ARE_ToolItem_Quote();
            IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
            IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
            IARE_ToolItem hr = new ARE_ToolItem_Hr();
//        IARE_ToolItem link = new ARE_ToolItem_Link();
//        IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
//        IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
            IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
            IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
            IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
//        IARE_ToolItem image = new ARE_ToolItem_Image();
//        IARE_ToolItem video = new ARE_ToolItem_Video();
            IARE_ToolItem at = new ARE_ToolItem_At();
            mToolbar.addToolbarItem(bold);
            mToolbar.addToolbarItem(italic);
            mToolbar.addToolbarItem(underline);
            mToolbar.addToolbarItem(strikethrough);
            mToolbar.addToolbarItem(quote);
            mToolbar.addToolbarItem(listNumber);
            mToolbar.addToolbarItem(listBullet);
            mToolbar.addToolbarItem(hr);
//        mToolbar.addToolbarItem(link);
//        mToolbar.addToolbarItem(subscript);
//        mToolbar.addToolbarItem(superscript);
            mToolbar.addToolbarItem(left);
            mToolbar.addToolbarItem(center);
            mToolbar.addToolbarItem(right);
//        mToolbar.addToolbarItem(image);
//        mToolbar.addToolbarItem(video);
            mToolbar.addToolbarItem(at);

            mEditText = this.findViewById(R.id.arEditText);
            mEditText.setToolbar(mToolbar);

//        setHtml();

            initToolbarArrow();
        }


        private void setHtml() {
            String html = "<p style=\"text-align: center;\"><strong>New Feature in 0.1.2</strong></p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">In this release, you have a new usage with ARE.</span></p>\n" +
                    "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">AREditText + ARE_Toolbar, you are now able to control the position of the input area and where to put the toolbar at and, what ToolItems you'd like to have in the toolbar. </span></p>\n" +
                    "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">You can not only define the Toolbar (and it's style), you can also add your own ARE_ToolItem with your style into ARE.</span></p>\n" +
                    "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: left;\"><span style=\"color: #ff00ff;\"><em><strong>Why not give it a try now?</strong></em></span></p>";
            mEditText.fromHtml(html);
        }



        private void initToolbarArrow() {
            final ImageView imageView = this.findViewById(R.id.arrow);
            if (this.mToolbar instanceof ARE_ToolbarDefault) {
                ((ARE_ToolbarDefault) mToolbar).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int scrollX = ((ARE_ToolbarDefault) mToolbar).getScrollX();
                        int scrollWidth = ((ARE_ToolbarDefault) mToolbar).getWidth();
                        int fullWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();

                        if (scrollX + scrollWidth < fullWidth) {
                            imageView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                            scrollerAtEnd = false;
                        } else {
                            imageView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24);
                            scrollerAtEnd = true;
                        }
                    }
                });
            }





            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (scrollerAtEnd) {
                        ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(-Integer.MAX_VALUE, 0);
                        scrollerAtEnd = false;
                    } else {
                        int hsWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();
                        ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(hsWidth, 0);
                        scrollerAtEnd = true;
                    }
                }
            });
        }



        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int menuId = item.getItemId();
//        if (menuId == com.chinalwb.are.R.id.action_save) {
//            String html = this.mEditText.getHtml();
//            DemoUtil.saveHtml(this, html);
//            return true;
//        }
//        if (menuId == R.id.action_show_tv) {
//            String html = this.mEditText.getHtml();
//            Intent intent = new Intent(this, TextViewActivity.class);
//            intent.putExtra(HTML_TEXT, html);
//            startActivity(intent);
//            return true;
//        }
            return super.onOptionsItemSelected(item);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
//        mToolbar.onActivityResult(requestCode, resultCode, data);
        }



}
