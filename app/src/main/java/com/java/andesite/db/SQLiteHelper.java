package com.java.andesite.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.java.andesite.MainActivity;
import com.java.andesite.onCreate.Main;
import com.java.andesite.vo.TodoVO;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TODO = "todo";

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        if (!isDataExists(db)) {
            insertInitialData(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 업그레이드 로직 필요 시 구현
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TODO + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, date TEXT, time TEXT, priority INTEGER, done INTEGER)");
    }

    private void insertInitialData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_TODO + " (title, content, date, time, priority, done) VALUES " +
                "('시험문제 출제하기', '오늘은 시험문제 출제하는날~', '2024-10-29', 'time', 5, 0), " +
                "('수학공부하기', '오늘은 시험문제?', '2024-11-23', 'time', 5, 0), " +
                "('Finish Project', 'Complete the final draft of the report', '2024-10-29', '17:00', 3, 0), " +
                "('Exercise', 'Go for a 5km run', '2024-10-30', '07:00', 1, 1), " +
                "('Study Session', 'Prepare for the upcoming exam', '2024-11-01', '14:00', 2, 0), " +
                "('Team Meeting', 'Discuss project milestones', '2024-11-03', '10:00', 3, 1), " +
                "('Clean Room', 'Organize the room and dust the shelves', '2024-11-05', '09:00', 1, 0), " +
                "('Read Book', 'Read 50 pages of a novel', '2024-11-06', '20:00', 2, 1)");
    }

    public ArrayList<TodoVO> getTodoNotDoneList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TodoVO> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_TODO + " WHERE done = 0 ORDER BY date ASC, priority DESC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            TodoVO vo = new TodoVO();
            vo.setId(cursor.getInt(0));
            vo.setTitle(cursor.getString(1));
            vo.setContent(cursor.getString(2));
            vo.setDate(cursor.getString(3));
            vo.setTime(cursor.getString(4));
            vo.setPriority(cursor.getInt(5));
            vo.setDone(cursor.getInt(6));
            list.add(vo);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void getTodoList_sort_count(String date_sort, String priority_sort, String done_sort) {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String orderBy = buildOrderByClause(date_sort, priority_sort, done_sort);
            String sql = "SELECT COUNT(*) FROM " + TABLE_TODO +
                    " WHERE done = " + (done_sort.equalsIgnoreCase("done") ? "1" : "0") +
                    " ORDER BY " + orderBy;
            cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()) {
                result = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        MainActivity.pageVO.setTotalPage(result);
    }
    public int getTodoList_sort_count_arranged() {
        int result;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String sql = "SELECT COUNT(*) FROM " + TABLE_TODO;
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getInt(0);
            } else {
                result = 0; // 결과가 없을 경우 0으로 설정
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
            return -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return result;
    }
    public ArrayList<TodoVO> getTodoList_sort(int num, String date_sort, String priority_sort, String done_sort, int isDone_arrange) {
        ArrayList<TodoVO> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String orderBy = buildOrderByClause(date_sort, priority_sort, done_sort);
            int offset = (num - 1) * 5;
            String sql;
            if(isDone_arrange == -2) {
                sql = "SELECT id, title, content, date, time, priority, done FROM " + TABLE_TODO +
                        " ORDER BY " + orderBy + " LIMIT 5 OFFSET ?";
            } else {
                sql = "SELECT id, title, content, date, time, priority, done FROM " + TABLE_TODO +
                        " WHERE done = " + (done_sort.equalsIgnoreCase("done") ? "1" : "0") +
                        " ORDER BY " + orderBy + " LIMIT 5 OFFSET ?";
            }

            cursor = db.rawQuery(sql, new String[]{String.valueOf(offset)});

            if (cursor.moveToFirst()) {
                do {
                    TodoVO vo = new TodoVO();
                    vo.setId(cursor.getInt(0));
                    vo.setTitle(cursor.getString(1));
                    vo.setContent(cursor.getString(2));
                    vo.setDate(cursor.getString(3));
                    vo.setTime(cursor.getString(4));
                    vo.setPriority(cursor.getInt(5));
                    vo.setDone(cursor.getInt(6));
                    result.add(vo);
                } while (cursor.moveToNext());
            } else {
                TodoVO vo = new TodoVO();
                vo.setTitle("error");
                result.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TodoVO errorVO = new TodoVO();
            errorVO.setTitle("Error: " + e.getMessage());
            result.add(errorVO);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return result;
    }

    private String buildOrderByClause(String date_sort, String priority_sort, String done_sort) {
        StringBuilder orderBy = new StringBuilder();
        if (!date_sort.isEmpty()) {
            orderBy.append("date ").append(date_sort.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        }
        if (!priority_sort.isEmpty()) {
            if (orderBy.length() > 0) orderBy.append(", ");
            orderBy.append("priority ").append("DESC");
        }
        if (!done_sort.isEmpty()) {
            if (orderBy.length() > 0) orderBy.append(", ");
            orderBy.append("done ").append(done_sort.equalsIgnoreCase("done") ? "ASC" : "DESC");
        }
        return orderBy.toString();
    }

    private boolean isDataExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_TODO + " LIMIT 1", null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public void setDone(int id, int done) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.e("done", String.valueOf(id));
        try {
            db.execSQL("UPDATE " + TABLE_TODO + " SET done = ? WHERE id = ?", new String[]{String.valueOf(done), String.valueOf(id)});
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        Log.i("done", String.valueOf(id));
        db.close();
    }
}