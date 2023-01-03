package com.example.quizzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity {
    private String selectedTopicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        final LinearLayout java=findViewById(R.id.java);
        final LinearLayout python=findViewById(R.id.python);

        final Button startBtn=findViewById(R.id.startQuiz);


        java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopicName="java";

                java.setBackgroundResource(R.drawable.round_back_white_stroke10);

            }
        });
        python.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopicName="python";

                python.setBackgroundResource(R.drawable.round_back_white_stroke10);

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTopicName.isEmpty()){
                    Toast.makeText(OptionActivity.this,"Please select the topic",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(OptionActivity.this,QuizActivity.class);
                    intent.putExtra("selectedTopic",selectedTopicName);
                    startActivity(intent);
                }

            }
        });

    }


}