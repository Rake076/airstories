package com.example.android.air_stories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class JournalWritingActivity extends AppCompatActivity {

    private IARE_Toolbar mToolbar;
    private AREditText mEditText;
    private boolean scrollerAtEnd;

    TextView textcount_textview;
    MaterialButton publish_btn;
    int userID = 0;

    EditText title_editText;
    Retrofit retrofit;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_journal_activity);

        title_editText = findViewById(R.id.journal_title_edit_text);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", 0);
        initToolbar();

        textcount_textview = findViewById(R.id.text_count);

        // Init API
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        mEditText = findViewById(R.id.arEditText);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mEditText.getText().length()<50){
                    textcount_textview.setTextColor(Color.parseColor("#FF1100"));
                } else if(mEditText.getText().length()>50){
                    textcount_textview.setTextColor(Color.parseColor("#000000"));
                }
                textcount_textview.setText(mEditText.getText().length() + "");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        publish_btn = findViewById(R.id.publish_btn);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = title_editText.getText().toString();
                String journal = mEditText.getText().toString();

                if(journal.length() < 50) {
                    mEditText.setError("Journal must be at least 50 characters long");
                }
                else if(title.length() < 1 || title.length() > 25){
                    title_editText.setError("Title must be between 1 to 25 characters");
                }
                else{
                    journal = mEditText.getHtml();
                    submitJournal(userID, journal, title);
                    finish();
                }
            }
        });
    }

    private void submitJournal(int userID, String journal, String title) {
        compositeDisposable.add(myAPI.submitJournals(userID, journal, title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.contains("successfully")) {
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Story has been uploaded! - " + s, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ));
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
            IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
            IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
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
            mToolbar.addToolbarItem(subscript);
            mToolbar.addToolbarItem(superscript);
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
