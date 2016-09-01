package jaquo.subtitlesinreallife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;

public class Hub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SeekBar unlock;
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setVisibility(View.INVISIBLE);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText.getText().length() == 5)  {
                    transferToAudience(mEditText.getText().toString());
                }
            }
        };
        mEditText.addTextChangedListener(textWatcher);
        unlock = (SeekBar) findViewById(R.id.myseek);
        unlock.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                byte progress;
                if (seekBar.getProgress() < 90) {
                    progress = (byte) seekBar.getProgress();
                    while (seekBar.getProgress() > 0) {
                        seekBar.setProgress(progress);
                        progress -= 0.1;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                if (seekBar.getProgress() >= 90) {
                    seekBar.setVisibility(View.INVISIBLE);
                    mEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        unlock.setProgress(0);
        unlock.setMax(100);
        if (unlock.getProgress() >= 80) {
            unlock.setVisibility(View.INVISIBLE);
            mEditText.setVisibility(View.VISIBLE);
            mEditText.setFocusable(true);
            mEditText.requestFocus();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_create) {
            transferToAudience("aztec");
        } else if (id == R.id.nav_settings) {
            transferToSettings();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void transferToSettings() {
        Intent intent = new Intent(this, SettingsAndCredits.class);
        startActivity(intent);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    unlock.setVisibility(View.VISIBLE);
                    unlock.setProgress(0);
                    mEditText.setVisibility(View.INVISIBLE);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    protected void onResume() {
        super.onResume();
        mEditText.setText("");
        mEditText.clearFocus();
    }


    private void transferToAudience(String roomID)   {
        Intent intent = new Intent(this, AudienceText.class);
        intent.putExtra("roomID", roomID);
        startActivity(intent);
    }
}
