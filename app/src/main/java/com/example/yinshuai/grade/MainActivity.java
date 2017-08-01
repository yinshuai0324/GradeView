package com.example.yinshuai.grade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GradeView gradeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gradeView= (GradeView) findViewById(R.id.gradeview);
        gradeView.setOnGradeClickItemListener(new GradeView.OnGradeClickItemListener() {
            @Override
            public void currentItem(int pos) {
                Toast.makeText(MainActivity.this,"当前评分"+pos+"分",Toast.LENGTH_LONG).show();
            }
        });
    }
}
