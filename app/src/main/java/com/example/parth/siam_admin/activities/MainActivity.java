package com.example.parth.siam_admin.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parth.siam_admin.R;
import com.example.parth.siam_admin.listeners.OnGetDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    int count = 0;
    Button updateUserScore;
    Button btUpdateLB;
    Button btUpdateCredits;
    Button btEnable;
    Button btDisable;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference DbRRoot;
    DatabaseReference DbRLeaderBoard;
    DatabaseReference DbRQuestions;
    DatabaseReference DbRUsers;
    DatabaseReference DbRCurrentQuestionNumber;
    DatabaseReference DbRIsActive;
    DatabaseReference DbRMonthlyLeaderBoard;
    long todayCredits;
    long questionNumber;
    long scoreAdd;
    long week;
    String answer;

    private DataSnapshot userDataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btUpdateLB = (Button) findViewById(R.id.bt_leaderboard);
        updateUserScore = (Button) findViewById(R.id.bt_main);
        btUpdateCredits = (Button) findViewById(R.id.bt_upd_credits);
        btEnable = (Button) findViewById(R.id.bt_enable);
        btDisable = (Button) findViewById(R.id.bt_disable);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DbRRoot = firebaseDatabase.getReference();
        DbRLeaderBoard = DbRRoot.child("leaderBoard");
        DbRQuestions = DbRRoot.child("questions");
        DbRUsers = DbRRoot.child("Users");
        DbRCurrentQuestionNumber = DbRRoot.child("current");
        DbRIsActive = DbRRoot.child("isActive");
        DbRMonthlyLeaderBoard = DbRRoot.child("monthlyLeaderBoard");

        btEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbRIsActive.setValue(1);
                makeToast("Event is Enabled");
            }
        });

        btDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbRIsActive.setValue(0);
                makeToast("Event is Disabled");
            }
        });

        //user ke attempted Questions Mai Score Dal rha hai
        updateUserScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Last place where WORK happens
                final OnGetDataListener<String[]> updateLeaderBoardAndCredits
                        = new OnGetDataListener<String[]>() {
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                    @Override
                    public void onStart() {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Getting Winner Emails ");
                        progressDialog.show();

                    }

                    @Override
                    public void onSuccess(String[] emails) {
                        //user ke attempted Questions Mai Score Update
                        final int max = (emails.length);
                        if (max == 0) {
                            makeToast("no winner");
                            progressDialog.dismiss();
                        }
                        for (int i = 0; i < max; i++) {
                            if (i < 20) {
                                scoreAdd = 30 - i;
                            } else {
                                scoreAdd = 10;
                            }

                            final String email = emails[i].replace('.', ',');
//                            if (i < 20) {
////first 20 winners increment Credits
//                                DbRUsers.child(email).child("credits")
//                                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                count++;
//                                                long credits = dataSnapshot.getValue(Long.class);
//                                                DbRUsers.child(email).child("credits")
//                                                        .setValue(credits + todayCredits);
//                                                DbRUsers.child(email).child("todays credits")
//                                                        .setValue(0);
//                                                if (count == 20) {
////                                                  //task finishes
//                                                    DbRIsActive.setValue(1);
//                                                    progressDialog.dismiss();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//                                                Toast.makeText(MainActivity.this,
//                                                        "error in updating Credits of " + email, Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                            }


////                            //Increment score of all correct users
//                            DbRLeaderBoard.child(email)
//                                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            String tempEmail = dataSnapshot.getKey();
//                                            StringAndLong tempStringAndLong = dataSnapshot
//                                                    .getValue(StringAndLong.class);
//
//                                            long score = tempStringAndLong.getScore()
//                                                    + scoreOfUsersMap.get(tempEmail);
////                                            makeToast(String.valueOf(x));
//                                            DbRLeaderBoard.child(email).child("score").setValue(score);
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//                                            Toast.makeText(MainActivity.this,
//                                                    "error in updating Score of " + email, Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                            DbRMonthlyLeaderBoard.child(email)
//                                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            String tempEmail = dataSnapshot.getKey();
//                                            StringAndLong tempStringAndLong = dataSnapshot
//                                                    .getValue(StringAndLong.class);
//
//                                            long score = tempStringAndLong.getScore()
//                                                    + scoreOfUsersMap.get(tempEmail);
//                                            DbRMonthlyLeaderBoard.child(email).child("score").setValue(score);
//                                            if (tempEmail.equals(lastEmail)) {
//                                                  //task finishes
//                                                makeToast("Task Done");
//                                                DbRIsActive.setValue(1);
//                                                progressDialog.dismiss();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//                                            Toast.makeText(MainActivity.this,
//                                                    "error in updating Score of " + email, Toast.LENGTH_SHORT).show();
//                                        }
//                                    });

                            DbRUsers.child(email).child("attemptedQuestions")
                                    .child(String.valueOf(questionNumber)).child("score").setValue(scoreAdd);

                        }
                        progressDialog.dismiss();

                    }
                };

                final OnGetDataListener<Long> getEmailAfterQuestionCredits =
                        new OnGetDataListener<Long>() {
                            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                            @Override
                            public void onStart() {
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Getting Current Question credits");
                                progressDialog.show();
                            }

                            @Override
                            public void onSuccess(Long questionNumber) {
                                progressDialog.dismiss();
                                DbRCurrentQuestionNumber.setValue(questionNumber + 1);
                                getWinnerEmails(questionNumber, updateLeaderBoardAndCredits);
                            }
                        };

                OnGetDataListener<Long> questionCreditAfterCurrent =
                        new OnGetDataListener<Long>() {
                            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                            @Override
                            public void onStart() {
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Getting Current Question Number");
                                progressDialog.show();
                            }

                            @Override
                            public void onSuccess(Long questionNumber) {
                                progressDialog.dismiss();
                                getTodayQuestionCredits(questionNumber, getEmailAfterQuestionCredits);
//                                getWinnerEmails(questionNumber, updateLeaderBoardAndCredits);
//                                DbRCurrentQuestionNumber.setValue(questionNumber + 1);
                            }
                        };

                //The only function call
                DbRIsActive.setValue(0);
                getCurrentQuestionNumber(questionCreditAfterCurrent);
            }
        });

        //Dono leaderBoard Ko Update
        btUpdateLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnGetDataListener<HashMap<String, DoubleLong>> correctLeaderBoardAfterScoreSum
                        = new OnGetDataListener<HashMap<String, DoubleLong>>() {
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                    @Override
                    public void onStart() {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Updating LeaderBoard");
                        progressDialog.show();
                    }

                    //Dono leaderBoard Ko Update
                    @Override
                    public void onSuccess(HashMap<String, DoubleLong> keyScoreMap) {

                        for (Map.Entry<String, DoubleLong> entry : keyScoreMap.entrySet()) {
                            DbRLeaderBoard.child(entry.getKey()).child("score")
                                    .setValue(entry.getValue().getWeeklyScore());

                            DbRMonthlyLeaderBoard.child(entry.getKey()).child("score")
                                    .setValue(entry.getValue().getMonthlyScore());
                        }
                        progressDialog.dismiss();
                    }
                };

                getUserScoreSum(correctLeaderBoardAfterScoreSum);
            }
        });

        btUpdateCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Updating Credits");
                progressDialog.setCancelable(false);
                progressDialog.show();
                for (DataSnapshot users : userDataSnapshot.getChildren()) {
                    String email = users.getKey();
                    long todaysCredits = 0;
                    DataSnapshot tCreditsDataSnap = users.child("todays credits");
                    try {

                        if (tCreditsDataSnap.getValue() != null) {
                            todaysCredits = tCreditsDataSnap.getValue(Long.class);
                        }
                        long credits = users.child("credits").getValue(Long.class);
                        long totalCredits = todaysCredits + credits;
                        //add data to DB
                        DbRUsers.child(email).child("credits")
                                .setValue(totalCredits);

                        DbRUsers.child(email).child("todays credits")
                                .setValue(0);
                    } catch (Exception e) {
                        makeToast("error in credits of " + email + "\n" + e);
                    }
                }
                progressDialog.dismiss();
                DbRIsActive.setValue(1);
                makeToast("task Completed");
            }
        });

    }

    private void getUserScoreSum(final OnGetDataListener<HashMap<String, DoubleLong>> correctLeaderBoardAfterScoreSum) {
        correctLeaderBoardAfterScoreSum.onStart();
        DbRUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren()
                userDataSnapshot = dataSnapshot;
                HashMap<String, DoubleLong> map = new HashMap<>();

                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    int questionCount = 0;
                    DoubleLong doubleLong = new DoubleLong();
                    String email = users.getKey();
                    long scoreSum = 0;
                    long weekSum = 0;
                    DataSnapshot attemptedQuestions = users.child("attemptedQuestions");
                    for (DataSnapshot question : attemptedQuestions.getChildren()) {
                        if (question != null) {
                            DataSnapshot questionScore = question.child("score");
                            if (questionScore.getValue() != null) {
                                try {
                                    scoreSum += questionScore.getValue(Long.class);
                                    if (Integer.parseInt(question.getKey()) >= (week - 1) * 7) {
                                        weekSum += questionScore.getValue(Long.class);
                                    }
                                } catch (Exception e) {
                                    makeToast(email + " has problem in credits");
                                }

                            }
                        }
                        questionCount++;
                    }
                    doubleLong.setMonthlyScore(scoreSum);
                    doubleLong.setWeeklyScore(weekSum);
                    map.put(email, doubleLong);
                }
                correctLeaderBoardAfterScoreSum.onSuccess(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCurrentQuestionNumber(final OnGetDataListener<Long> onGetDataListener) {
        onGetDataListener.onStart();
        DbRCurrentQuestionNumber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    long currentQuestion = dataSnapshot.getValue(Long.class);
                    makeToast("current Question no:" + String.valueOf(currentQuestion));
                    // get credit of today's question
                    questionNumber = currentQuestion;
                    week = 1 + (questionNumber / 7);
                    onGetDataListener.onSuccess(currentQuestion);
                } catch (Exception e) {
                    makeToast("problem in CURRENT in DB");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,
                        "error in getting current question number ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTodayQuestionCredits(final long questionNumber, final OnGetDataListener<Long> longOnGetDataListener) {
        longOnGetDataListener.onStart();
        DbRQuestions.child(String.valueOf(questionNumber)).child("credits")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            todayCredits = dataSnapshot.getValue(Long.class);
                        }catch (Exception e){
                            makeToast("Error in Question Credits in DB");
                        }finally {
                            longOnGetDataListener.onSuccess(questionNumber);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        makeToast("unable to get Question Credits\n try again");
                    }
                });


    }

    private void getWinnerEmails(final Long questionNumber, final OnGetDataListener<String[]> onGetDataListener) {
        onGetDataListener.onStart();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Answer");

// Set up the input
        final EditText input = new EditText(this);
        input.setPadding(10, 0, 10, 0);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setCancelable(false);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                answer = input.getText().toString();
//                answer = "t\u00B3t\u2081 = -1";
                makeToast(answer);
                DbRQuestions.child(String.valueOf(questionNumber)).child("attemptedBy")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                long count = dataSnapshot.getChildrenCount();
                                if (count == 0) {
                                    String emailArrayList[] = new String[0];
                                    onGetDataListener.onSuccess(emailArrayList);
                                } else {


                                    ArrayList<String> emailArrayList = new ArrayList<>();
                                    int i = 0;//position in attempted by
                                    int winnerCount = 0;
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        AnswerAndEmail stringAndLong = child.getValue(AnswerAndEmail.class);
                                        String email = stringAndLong.getEmail();
                                        String userAnswer = stringAndLong.getAnswer();
                                        if (userAnswer.equalsIgnoreCase(answer)) {
                                            //if user is winner
                                            emailArrayList.add(email);
                                            DbRQuestions.child(String.valueOf(questionNumber))
                                                    .child("attemptedBy").child(String.valueOf(i)).child("status")
                                                    .setValue("correct");

                                            DbRUsers.child(email.replace('.', ',')).child("attemptedQuestions")
                                                    .child(String.valueOf(questionNumber)).child("status")
                                                    .setValue("correct");

                                            DbRQuestions.child(String.valueOf(questionNumber))
                                                    .child("winnerEmail").child(String.valueOf(winnerCount))
                                                    .child("email")
                                                    .setValue(email);


                                            if (winnerCount < 20) {
                                                DbRUsers.child(email.replace('.', ',')).child("todays credits")
                                                        .setValue(todayCredits);
                                            }
                                            winnerCount++;

                                        } else {
                                            DbRQuestions.child(String.valueOf(questionNumber))
                                                    .child("attemptedBy").child(String.valueOf(i)).child("status")
                                                    .setValue("wrong");

                                            DbRUsers.child(email.replace('.', ',')).child("attemptedQuestions")
                                                    .child(String.valueOf(questionNumber)).child("status")
                                                    .setValue("wrong");

                                        }
                                        i++;
                                    }

                                    makeToast("winner Emails count : " + emailArrayList.size());
                                    String emails[] = emailArrayList.toArray(new String[emailArrayList.size()]);
                                    onGetDataListener.onSuccess(emails);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this,
                                        "error in getting winner emails of question number " + questionNumber
                                        , Toast.LENGTH_SHORT).show();

                            }
                        });

            }

        });
        builder.show();
    }

    private void makeToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    static class StringAndLong {
        String string;
        long aLong;

        public void setString(String string) {
            this.string = string;
        }

        public void setaLong(long aLong) {
            this.aLong = aLong;
        }

        public String getString() {
            return string;
        }

        public long getaLong() {
            return aLong;
        }
    }

    static class DoubleLong {
        private long monthlyScore;
        private long weeklyScore;

        DoubleLong(long monthlyScore, long weeklyScore) {
            this.weeklyScore = weeklyScore;
            this.monthlyScore = monthlyScore;
        }

        DoubleLong() {
            weeklyScore = 0;
            monthlyScore = 0;
        }

        public long getMonthlyScore() {
            return monthlyScore;
        }

        public long getWeeklyScore() {
            return weeklyScore;
        }

        public void setMonthlyScore(long monthlyScore) {
            this.monthlyScore = monthlyScore;
        }

        public void setWeeklyScore(long weeklyScore) {
            this.weeklyScore = weeklyScore;
        }
    }

    static class AnswerAndEmail {
        String email;
        String answer;

        public void setEmail(String email) {
            this.email = email;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getEmail() {
            return email;
        }

        public String getAnswer() {
            return answer;
        }
    }


}