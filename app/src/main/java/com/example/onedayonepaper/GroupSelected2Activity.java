package com.example.onedayonepaper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class GroupSelected2Activity extends AppCompatActivity {

    private AppCompatButton btnAddGroup, btnGroup1, btnGroup2, btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_selectd2);

        btnAddGroup = findViewById(R.id.btnAddGroup);
        btnGroup1 = findViewById(R.id.btnGroup1);
        btnGroup2 = findViewById(R.id.btnGroup2);
        btnCreate = findViewById(R.id.btnCreate);

        btnAddGroup.setOnClickListener(v -> {
            Intent intent = new Intent(GroupSelected2Activity.this, MainSelectActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        btnGroup2.setOnClickListener(v -> {
            Intent intent = new Intent(GroupSelected2Activity.this, GroupSelectedActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        btnGroup1.setOnClickListener(v -> {
            Intent intent = new Intent(GroupSelected2Activity.this, GroupSelected2Activity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        btnCreate.setOnClickListener(v -> {
            //TODO
        });
    }
}
