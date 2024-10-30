package com.java.andesite;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.java.andesite.db.SQLiteHelper;
import com.java.andesite.vo.TodoVO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Add_Todo_Form extends DialogFragment {
    private Context context;
    private Dialog dialog;
    private Runnable onDismissListener;
    private int priority;

    public Add_Todo_Form(Context context) {
        this.context = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (onDismissListener != null) {
            onDismissListener.run(); // dismiss() 호출 시 리스너 실행
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_todo_form, container, false);
    }

    private void setupSpinner(Spinner spinner) {
        String[] items = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(4);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = Integer.parseInt(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void setOnDismissListener(Runnable listener) {
        this.onDismissListener = listener;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSpinner(view.findViewById(R.id.form_spinner));

        view.findViewById(R.id.form_submit_button).setOnClickListener(v -> formSubmit(view));
    }

    private void formSubmit(View view) {
        String formTitle = Objects.requireNonNull(((TextInputEditText) view.findViewById(R.id.form_title)).getText()).toString();
        String formContent = Objects.requireNonNull(((TextInputEditText) view.findViewById(R.id.form_content)).getText()).toString();
        long selectedDateMillis = ((CalendarView) view.findViewById(R.id.form_calendar_view)).getDate();

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(selectedDateMillis);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());

        if(formTitle.isEmpty()) {
            Toast.makeText(context, "빈 제목은 지정할 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoVO vo = new TodoVO();
        vo.setTitle(formTitle);
        vo.setContent(formContent);
        vo.setPriority(priority);
        vo.setDone(0);
        vo.setDate(formattedDate);

        try (SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase()) {
            String sql = "INSERT INTO todo (title, content, date, time, priority, done) VALUES (?, ?, ?, 'time', ?, ?)";
            db.execSQL(sql, new String[]{vo.getTitle(), vo.getContent(), vo.getDate(), String.valueOf(vo.getPriority()), String.valueOf(vo.getDone())});
        }
        MainActivity.pageVO.setTotalPage(MainActivity.pageVO.getTotalPage() + 1);
        this.dismiss();
    }
}