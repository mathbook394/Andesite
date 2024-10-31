package com.java.andesite;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.java.andesite.db.SQLiteHelper;
import com.java.andesite.events.OnSwipeListener;
import com.java.andesite.events.TodoOnClick;
import com.java.andesite.onCreate.Main;
import com.java.andesite.vo.PageVO;
import com.java.andesite.vo.TodoVO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoOnClick, Main, OnSwipeListener {
    private SQLiteHelper sqLiteHelper;
    public static PageVO pageVO;
    private boolean loading = false;
    private Add_Todo_Form customDialog;
    private Add_Todo_Form myDialogFragment;
    private GestureDetector gestureDetector;
    private ArrayList<TodoVO> list;
    private String[] ids = {"0", "0", "0", "0", "0"};
    private int isDone_arrange = 0; //완료 한거 가져오려면 0, 아니면 -2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myDialogFragment = new Add_Todo_Form(this);
        sqLiteHelper = new SQLiteHelper(this);

        pageVO = new PageVO();
        sqLiteHelper.getTodoList_sort_count(pageVO.getDoneOrder());

        loadTodoList(pageVO.getNowPage());
        title_mainOnCreate();
        setupSpinner();
        customDialog = new Add_Todo_Form(this);
        customDialog.setOnDismissListener(() -> loading = false);

        gestureDetector = new GestureDetector(this, new GestureListener());

        // 특정 뷰에 터치 리스너 설정
        View swipeView = findViewById(R.id.main_swipe_event); // 스와이프를 감지할 뷰의 ID
        swipeView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        myDialogFragment.setOnDismissListener(() -> {
            loadTodoList(pageVO.getNowPage());
            sqLiteHelper.getTodoList_sort_count(pageVO.getDoneOrder());
        });
    }
    //====================================================================================

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void onSwipeRight() {
        if (pageVO.getNowPage() > 1) {
            pageVO.setNowPage(pageVO.getNowPage() - 1);
            Log.e("information", String.valueOf(pageVO.getNowPage()));
        } else {
            Toast.makeText(this, "첫번째 페이지 입니다", Toast.LENGTH_SHORT).show();
        }
        loadTodoList(pageVO.getNowPage());
    }

    @Override
    public void onSwipeLeft() {
        if (pageVO.getTotalPage() < pageVO.getNowPage() * 5) {
            Toast.makeText(this, "마지막 페이지입니다", Toast.LENGTH_SHORT).show();
        } else {
            pageVO.setNowPage(pageVO.getNowPage() + 1);
            Log.e("information", "nowPage: "+String.valueOf(pageVO.getNowPage()));
            loadTodoList(pageVO.getNowPage());
        }
        Log.e("information", "totalPage: "+String.valueOf(pageVO.getTotalPage()));
    }
    //====================================================================================

    private void loadTodoList(int pageNum) {
        list = sqLiteHelper.getTodoList_sort(pageNum, pageVO.getDateOrder(), "desc", pageVO.getDoneOrder(), isDone_arrange);
        updateTodoList(list);
    }

    @Override
    public void mainTodoOnclick(View view) {
        if (view instanceof CardView) {
            LinearLayout linearLayout = (LinearLayout) ((CardView) view).getChildAt(0);
            TextView description = (TextView) linearLayout.getChildAt(1);

            if (description.getVisibility() == View.GONE) {
                description.setVisibility(View.VISIBLE);
                description.setAlpha(0f);
                description.animate().alpha(1f).setDuration(300).start();
                description.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            } else {
                description.animate().alpha(0f).setDuration(300).withEndAction(() -> description.setVisibility(View.GONE)).start();
                description.getLayoutParams().height = 0;
            }
            description.requestLayout();
        }
    }

    @Override
    public void title_mainOnCreate() {
        TextView textview = findViewById(R.id.title_main);
        ViewGroup.LayoutParams params = textview.getLayoutParams();
        int height = params.height - 5;
        textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, height * 0.2f);
        textview.setShadowLayer(1.5f, 2, 2, Color.GRAY);
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.main_arrange_spinner);
        String[] items = {"날짜 순(오름차순)", "날짜 순(내림차순)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                pageVO.setNowPage(1);
                pageVO.setDateOrder(position == 0 ? "asc" : "desc");
                loadTodoList(pageVO.getNowPage());
                sqLiteHelper.getTodoList_sort_count(pageVO.getDoneOrder());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void updateTodoList(ArrayList<TodoVO> list) {
        for (int i = 0; i < pageVO.getNumPerPage(); i++) {
            if (i < list.size()) {
                updateTodo(list.get(i), i);
                ids[i] = String.valueOf(list.get(i).getId());
            } else {
                hideCardView(i);
            }
        }
        setCardViewVisibility(list);
    }

    private void updateTodo(TodoVO vo, int index) {
        TextView textView_title = findViewById(getResources().getIdentifier("taskTitle" + (index + 1), "id", getPackageName()));
        TextView textView_desc = findViewById(getResources().getIdentifier("todoDesc" + (index + 1), "id", getPackageName()));
        CheckBox checkBox = findViewById(getResources().getIdentifier("main_done_checkbox" + (index + 1), "id", getPackageName()));

        textView_title.setText(vo.getTitle() + "\n(~" + vo.getDate() + ")");
        textView_desc.setText(vo.getContent());
        checkBox.setChecked(vo.getDone() == 1);
    }

    private void hideCardView(int index) {
        int cardViewId = getResources().getIdentifier("main_cardView" + (index + 1), "id", getPackageName());
        CardView cardView = findViewById(cardViewId);
        cardView.setVisibility(View.GONE);
    }

    private void setCardViewVisibility(ArrayList<TodoVO> list) {
        for (int j = 0; j < 5; j++) {
            int cardViewId = getResources().getIdentifier("main_cardView" + (j + 1), "id", getPackageName());
            CardView cardView = findViewById(cardViewId);
            cardView.setVisibility(j < list.size() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public synchronized void add_button_mainOnCreate(View view) {
        if (loading) return;
        loading = true;
        myDialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
        loading = false;
    }

    @Override
    public void mainCheckBoxOnClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        int index = Integer.parseInt(getResources().getResourceEntryName(checkBox.getId()).replace("main_done_checkbox", "")) - 1;
        int todoId = Integer.parseInt(ids[index]);
        sqLiteHelper.setDone(todoId, checkBox.isChecked() ? 1 : 0);
        pageVO.setNowPage(1);
        loadTodoList(pageVO.getNowPage());
        sqLiteHelper.getTodoList_sort_count(pageVO.getDoneOrder());
    }

    @Override
    public void done_arrange_mainCheckBoxOnClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        pageVO.setDoneOrder(checkBox.isChecked() ? 1 : 0);
        Log.e("error", "done_arrange_mainCheckBoxOnClick: "+checkBox.isChecked());
        isDone_arrange = checkBox.isChecked() ? -2 : 0;

        pageVO.setNowPage(1);
        loadTodoList(pageVO.getNowPage());
        pageVO.setTotalPage(sqLiteHelper.getTodoList_sort_count_arranged());
    }
}