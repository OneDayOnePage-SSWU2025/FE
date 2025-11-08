package com.example.onedayonepaper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MainSelectActivity extends AppCompatActivity {

    private AppCompatButton btnAddGroup, btnGroup1, btnGroup2, btnMakeGroup, btnJoinGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main_select);

        btnAddGroup = findViewById(R.id.btnAddGroup);
        btnGroup1 = findViewById(R.id.btnGroup1);
        btnGroup2 = findViewById(R.id.btnGroup2);
        btnMakeGroup = findViewById(R.id.btnMakeGroup);
        btnJoinGroup = findViewById(R.id.btnJoinGroup);

        btnAddGroup.setOnClickListener(v -> {
            Intent intent = new Intent(MainSelectActivity.this, MainSelectActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        btnMakeGroup.setOnClickListener(v -> {
            Intent intent = new Intent(MainSelectActivity.this, MakeGroupActivity.class);
            startActivity(intent);
        });

        btnJoinGroup.setOnClickListener(v -> {
            Intent intent = new Intent(MainSelectActivity.this, JoinGroupActivity.class);
            startActivity(intent);
        });

        btnGroup1.setOnClickListener(v -> {
            Intent intent = new Intent(MainSelectActivity.this, GroupSelectedActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        btnGroup2.setOnClickListener(v -> {
            Intent intent = new Intent(MainSelectActivity.this, GroupSelected2Activity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}
