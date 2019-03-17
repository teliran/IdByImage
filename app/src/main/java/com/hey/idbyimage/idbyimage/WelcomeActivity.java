package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        next = findViewById(R.id.nextbtn);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(v.getId()==R.id.nextbtn) {
            SharedPreferences pref = getSharedPreferences("imagePref", Context.MODE_PRIVATE);
            if (pref.getAll().size() == CountImages()) {
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(this, InstructionActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private int CountImages() {
        Field[] fields = R.drawable.class.getFields();
        List<Integer> drawables = new ArrayList<Integer>();
        for (Field field : fields) {
            // Take only those with name starting with "p"
            if (field.getName().startsWith("p")) {
                try {
                    drawables.add(field.getInt(null));
                } catch (IllegalAccessException e) {
                    Toast.makeText(WelcomeActivity.this,"Problem counting images",Toast.LENGTH_SHORT).show();
                }
            }
        }
        return drawables.size()-1;
    }
}
