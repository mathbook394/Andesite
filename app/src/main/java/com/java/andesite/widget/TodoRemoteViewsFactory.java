package com.java.andesite.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.java.andesite.R;
import com.java.andesite.db.SQLiteHelper;
import com.java.andesite.vo.TodoVO;

import java.util.ArrayList;
import java.util.List;

public class TodoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<TodoVO> todoList;
    private SQLiteHelper sqLiteHelper;

    public TodoRemoteViewsFactory(Context context) {
        this.context = context;
        todoList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        sqLiteHelper = new SQLiteHelper(context);
    }

    @Override
    public void onDataSetChanged() {
        // 데이터 갱신
        todoList = sqLiteHelper.getTodoNotDoneList();
    }

    @Override
    public void onDestroy() {
        todoList.clear();
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // 유효한 포지션인지 확인
        if (position < 0 || position >= todoList.size()) {
            return null;
        }

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item_todo);
        TodoVO todo = todoList.get(position);

        // 할 일 제목 설정
        rv.setTextViewText(R.id.widget_item_title, todo.getTitle());

        // 클릭 이벤트를 위한 Intent 설정
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("todo_id", todo.getId());
        rv.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}