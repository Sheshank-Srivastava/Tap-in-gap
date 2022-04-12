package com.iamtanshu.tapingap;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.iamtanshu.tapingap.model.LastView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    GridLayout colorGrid;
    TextView txtScore;
    Thread changeColor;
    View[] list = new View[4];
    int colors[] = new int[4];
    LastView lastView;
    int tapped = -1;
    int score = 0;
    boolean start = false;
    boolean gameover = false;
    long lasttaptimestamp = 0l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorGrid = findViewById(R.id.color_grid);
        txtScore = findViewById(R.id.txt_Score);
        int childCount = colorGrid
                .getChildCount();

        for (int i = 0; i < childCount; i++) {
            final int item = i;
            View container = (View) colorGrid.getChildAt(i);
            list[i] = container;
            colors[i] = ((ColorDrawable) container.getBackground()).getColor();

            container.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // your click code here
//                    Toast.makeText(MainActivity.this, "item clicked at " + item, Toast.LENGTH_SHORT).show();
                    if (start && tapped != item) {
                        gameover = true;
                        openExitDialog();
                    } else {
                        score++;
                        txtScore.setText("Score: " + score);
                        lastView.getView().setBackgroundColor(lastView.getColor());
                        

                    }
                    lasttaptimestamp = System.currentTimeMillis();
                    tapped = item;
                }
            });
        }
        for (long j : colors) {
            Log.d(TAG, "onCreate: Colors " + j);
        }
        lastView = LastView.getInstance();
        showDialog();

    }

    private void showDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Lets start Game")
                .setMessage("Tap  on Grey block  in time else you lose")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                        dialog.dismiss();
                        lasttaptimestamp = System.currentTimeMillis();
                        changeColor = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int i = 0;

                                while (true) {
                                    Random random = new Random();
                                    int rand = random.nextInt(4 - 1);
                                    Log.d(TAG, "run: last tap insec " + (System.currentTimeMillis() - lasttaptimestamp) + "    " + start);
                                    if (start && System.currentTimeMillis() - lasttaptimestamp > 900) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                openExitDialog();

                                            }
                                        });
                                        break;
                                    }
                                    tapped = rand;
                                    lastView.setViewToClick(rand);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            View view = list[rand];
//                                            if (lastView.getColor() != -1) {
//                                                lastView.getView().setBackgroundColor(lastView.getColor());
//                                            }
                                            lastView.setView(view);
                                            lastView.setColor(colors[rand]);
                                            view.setBackgroundColor(Color.rgb(220, 220, 220));
                                            lasttaptimestamp = System.currentTimeMillis();
                                        }
                                    });

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    start = true;
                                    i++;
                                    if (gameover) break;
                                }
                            }
                        });
                        changeColor.start();
                    }
                }).show();
    }

    private void openExitDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Game Over ")
                .setMessage("You Scored " + score + " points")
                .setCancelable(false)
                .setNegativeButton("exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameover = false;
                        lastView.setViewToClick(-1);
                        lastView.setView(null);
                        lastView.setColor(-1);
                        txtScore.setText("Score: 0");
                        score = 0;
                        for (int i = 0; i < list.length; i++) {
                            list[i].setBackgroundColor(colors[i]);
                        }
                        showDialog();
                        dialog.dismiss();
                    }
                }).show();
    }
}