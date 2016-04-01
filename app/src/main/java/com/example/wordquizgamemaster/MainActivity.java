package com.example.wordquizgamemaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mPlayGameButton;
    private Button mHighScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayGameButton = (Button) findViewById(R.id.play_game_button);
        mPlayGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "Hello log");

                Toast t = Toast.makeText(
                        MainActivity.this,
                        "Hello Toast",
                        Toast.LENGTH_SHORT
                );
                t.show();

                showCustomChooseDifficultyDialog();

/*
                final String[] items = new String[]{"ง่าย", "ปานกลาง", "ยาก"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("เลือกระดับความยาก");
                //dialog.setMessage("Message");
                dialog.setIcon(R.drawable.abc);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "คุณเลือก: " + items[which]);

                        Intent i = new Intent(MainActivity.this, GameActivity.class);
                        i.putExtra(GameActivity.KEY_DIFFICULTY, which);
                        startActivity(i);
                    }
                });
                dialog.show();
*/

/*
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "คุณคลิกปุ่ม OK");
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "คุณคลิกปุ่ม Cancel");
                    }
                });
*/
            }
        });

        mHighScoreButton = (Button) findViewById(R.id.high_score_button);
        mHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(i);
            }
        });
    }

    private void showCustomChooseDifficultyDialog() {
        AlertDialog.Builder chooseDiffDialog = new AlertDialog.Builder(MainActivity.this);

        chooseDiffDialog.setTitle("เลือกระดับความยาก");
        chooseDiffDialog.setCancelable(true);

        final String[] items = new String[]{"ง่าย", "ปานกลาง", "ยาก"};
        DifficultyOptionsAdapter adapter = new DifficultyOptionsAdapter(
                this,
                R.layout.difficulty_row,
                new ArrayList<>(Arrays.asList(items))
        );

        chooseDiffDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra(GameActivity.KEY_DIFFICULTY, which);
                startActivity(intent);
            }
        });
        chooseDiffDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Music.play(this, R.raw.main);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.stop();
    }

    private static class DifficultyOptionsAdapter extends ArrayAdapter<String> {

        private Context context;
        private int itemLayoutId;
        private ArrayList<String> difficulties;

        public DifficultyOptionsAdapter(Context context, int itemLayoutId,
                                        ArrayList<String> difficulties) {
            super(context, itemLayoutId, difficulties);

            this.context = context;
            this.itemLayoutId = itemLayoutId;
            this.difficulties = difficulties;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(itemLayoutId, parent, false);

            TextView difficultyTextView = (TextView) row.findViewById(R.id.difficultyTextView);
            ImageView difficultyImageView = (ImageView) row.findViewById(R.id.difficultyImageView);

            String diff = difficulties.get(position);
            difficultyTextView.setText(diff);

            if (diff.equals("ง่าย")) {
                difficultyImageView.setImageResource(R.drawable.dog_easy);
            } else if (diff.equals("ปานกลาง")) {
                difficultyImageView.setImageResource(R.drawable.dog_medium);
            } else if (diff.equals("ยาก")) {
                difficultyImageView.setImageResource(R.drawable.dog_hard);
            }

            return row;
        }
    }
}
