package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AndroidException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    TextView textView;
    FloatingActionButton add;
    LottieAnimationView lottieAnimationView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.add_note){
            Intent intent = new Intent(getApplicationContext(),activity_notes_editor.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottieAnimationView = findViewById(R.id.animation_view);
        ListView listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView2);
        add = findViewById(R.id.floatingActionButton);
        lottieAnimationView.animate().alpha(0f).setDuration(3000);
        listView.animate().alpha(1f).setDuration(3000);
        add.animate().alpha(1f).setDuration(3000);
        textView.animate().translationYBy(-250f).alpha(1f).setDuration(3000);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        HashSet<String> hashSet = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(notes != null){
            textView.setVisibility(View.INVISIBLE);
        }

        if(hashSet==null){
            textView.setVisibility(View.VISIBLE);
        }else{
            notes = new ArrayList(hashSet);
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),activity_notes_editor.class);
                intent.putExtra("noteID",position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Permanently")
                        .setMessage("Do you want to delete the note permanently?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                HashSet<String> hashSet = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",hashSet).apply();
                            }
                        }).setNegativeButton("No",null)
                        .show();

                return true;
            }
        });
    }
    public void newNote(View view){
        textView.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(getApplicationContext(),activity_notes_editor.class);
        startActivity(intent);
    }
}