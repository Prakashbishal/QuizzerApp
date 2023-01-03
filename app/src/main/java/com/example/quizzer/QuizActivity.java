package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {
    private TextView questions,question;

    private AppCompatButton option1,option2,option3,option4;

    private AppCompatButton nextBtn;

    private Timer quizTimer;

    private int totalTimeInMin=1;

    private int seconds=0;

    private List<QuestionsList> questionsLists;

    private int currentQuestionPosition=0;

    private String selectedOptionByUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final ImageView backBtn=findViewById(R.id.backBtn);
        final TextView timer=findViewById(R.id.timer);
        final TextView selectedTopicName=findViewById(R.id.topicName);

        questionsLists=new ArrayList<>();
        questions=findViewById(R.id.questions);
        question=findViewById(R.id.question);

        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);

        nextBtn=findViewById(R.id.nextbtn);

        final String getSelectedTopicName=getIntent().getStringExtra("selectedTopic");

        selectedTopicName.setText(getSelectedTopicName);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://quizzer-1bdd1-default-rtdb.firebaseio.com/");

        ProgressDialog progressDialog=new ProgressDialog(QuizActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                totalTimeInMin=Integer.parseInt(snapshot.child("time").getValue(String.class));

                for(DataSnapshot dataSnapshot:snapshot.child("getSelectedTopicName").getChildren()){
                    final String getQuestion=dataSnapshot.child("question").getValue(String.class);
                    final String getOption1=dataSnapshot.child("option1").getValue(String.class);
                    final String getOption2=dataSnapshot.child("option2").getValue(String.class);
                    final String getOption3=dataSnapshot.child("option3").getValue(String.class);
                    final String getOption4=dataSnapshot.child("option4").getValue(String.class);

                    final String getAnswer=dataSnapshot.child("answer").getValue(String.class);


                    QuestionsList questionsList=new QuestionsList(getQuestion,getOption1,getOption2,getOption3,getOption4,getAnswer);
                    questionsLists.add(questionsList);
                }

                progressDialog.hide();
                questions.setText((currentQuestionPosition+1)+"/"+questionsLists.size());

                question.setText(questionsLists.get(currentQuestionPosition).getQuestion());
                option1.setText(questionsLists.get(currentQuestionPosition).getOption1());
                option2.setText(questionsLists.get(currentQuestionPosition).getOption2());
                option3.setText(questionsLists.get(currentQuestionPosition).getOption3());
                option4.setText(questionsLists.get(currentQuestionPosition).getOption4());

                startTimer(timer);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()){
                    selectedOptionByUser=option1.getText().toString();

                    option1.setBackgroundResource(R.drawable.round_back_red10);
                    option1.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()){
                    selectedOptionByUser=option2.getText().toString();

                    option2.setBackgroundResource(R.drawable.round_back_red10);
                    option2.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedOptionByUser.isEmpty()){
                    selectedOptionByUser=option3.getText().toString();

                    option3.setBackgroundResource(R.drawable.round_back_red10);
                    option3.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }

            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedOptionByUser.isEmpty()){
                    selectedOptionByUser=option4.getText().toString();

                    option4.setBackgroundResource(R.drawable.round_back_red10);
                    option4.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()){
                    Toast.makeText(QuizActivity.this,"Please select an option",Toast.LENGTH_SHORT).show();
                }
                else {
                    changeNextQuestion();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizTimer.purge();
                quizTimer.cancel();

                startActivity(new Intent(QuizActivity.this,OptionActivity.class));
                finish();
            }
        });

    }

    private void changeNextQuestion(){
        currentQuestionPosition++;

        if ((currentQuestionPosition+1)==questionsLists.size()){
            nextBtn.setText("Submit Quiz");
        }

        if(currentQuestionPosition<questionsLists.size()){
            selectedOptionByUser="";

            option1.setBackgroundResource(R.drawable.round_back_white_stroke20);
            option1.setTextColor(Color.parseColor("#1F6BB8"));

            option2.setBackgroundResource(R.drawable.round_back_white_stroke20);
            option2.setTextColor(Color.parseColor("#1F6BB8"));

            option3.setBackgroundResource(R.drawable.round_back_white_stroke20);
            option3.setTextColor(Color.parseColor("#1F6BB8"));

            option4.setBackgroundResource(R.drawable.round_back_white_stroke20);
            option4.setTextColor(Color.parseColor("#1F6BB8"));

            questions.setText((currentQuestionPosition+1)+"/"+questionsLists.size());

            question.setText(questionsLists.get(currentQuestionPosition).getQuestion());
            option1.setText(questionsLists.get(currentQuestionPosition).getOption1());
            option2.setText(questionsLists.get(currentQuestionPosition).getOption2());
            option3.setText(questionsLists.get(currentQuestionPosition).getOption3());
            option4.setText(questionsLists.get(currentQuestionPosition).getOption4());
        }

        else {
            Intent intent=new Intent(QuizActivity.this,QuizResult.class);
            intent.putExtra("correct",getCorrectAnswers());
            intent.putExtra("incorrect",getInCorrectAnswers());
            startActivity(intent);

            finish();
        }
    }
    private void startTimer(TextView timerTextView){

        quizTimer=new Timer();

        quizTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (seconds==0){
                    totalTimeInMin--;
                    seconds=59;
                }
                else if (seconds==0 && totalTimeInMin==0)
                {
                    quizTimer.purge();
                    quizTimer.cancel();

                    Toast.makeText(QuizActivity.this,"Time Over",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(QuizActivity.this,QuizResult.class);
                    intent.putExtra("correct",getCorrectAnswers());
                    intent.putExtra("incorrect",getInCorrectAnswers());
                    startActivity(intent);

                    finish();
                }
                else {
                    seconds--;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String finalMinutes= String.valueOf(totalTimeInMin);
                        String finalSeconds= String.valueOf(seconds);

                        if(finalMinutes.length()==1){
                            finalMinutes="0"+finalMinutes;
                        }

                        if (finalSeconds.length()==1){
                            finalSeconds="0"+finalSeconds;
                        }

                        timerTextView.setText(finalMinutes+":"+finalSeconds);
                    }
                });
            }
        },1000,1000);
    }
    private int getCorrectAnswers(){
        int correctAnswers=0;

        for (int i=0;i<questionsLists.size();i++){
            final String getUserSelectedAnswer=questionsLists.get(i).getUserSelectedAnswer();
            final String getAnswer=questionsLists.get(i).getAnswer();

            if (getUserSelectedAnswer.equals(getAnswer)){
                correctAnswers++;
            }
        }

        return correctAnswers;
    }
    private int getInCorrectAnswers(){
        int correctAnswers=0;

        for (int i=0;i<questionsLists.size();i++){
            final String getUserSelectedAnswer=questionsLists.get(i).getUserSelectedAnswer();
            final String getAnswer=questionsLists.get(i).getAnswer();

            if (!getUserSelectedAnswer.equals(getAnswer)){
                correctAnswers++;
            }
        }

        return correctAnswers;
    }

    @Override
    public void onBackPressed() {
        quizTimer.purge();
        quizTimer.cancel();

        startActivity(new Intent(QuizActivity.this,OptionActivity.class));
        finish();
    }

    private void revealAnswer(){

        final String getAnswer=questionsLists.get(currentQuestionPosition).getAnswer();

        if(option1.getText().toString().equals(getAnswer)){
            option1.setBackgroundResource(R.drawable.round_back_green10);
            option1.setTextColor(Color.WHITE);
        }
        else if(option2.getText().toString().equals(getAnswer)){
            option2.setBackgroundResource(R.drawable.round_back_green10);
            option2.setTextColor(Color.WHITE);
        }
        else if(option3.getText().toString().equals(getAnswer)){
            option3.setBackgroundResource(R.drawable.round_back_green10);
            option3.setTextColor(Color.WHITE);
        }
        else if(option4.getText().toString().equals(getAnswer)){
            option4.setBackgroundResource(R.drawable.round_back_green10);
            option4.setTextColor(Color.WHITE);
        }
    }
}