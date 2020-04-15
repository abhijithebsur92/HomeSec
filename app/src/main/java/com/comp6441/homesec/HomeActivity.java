package com.comp6441.homesec;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.comp6441.homesec.util.NetworkUtils;
import com.comp6441.homesec.util.NotificationUtils;
import com.example.homesec.R;

import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mStatus;
    private Button mStopButton;
    private SharedPreferenceManager mManager;
    private EditText mEditText;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mManager = SharedPreferenceManager.getInstance(this);

        initViews();
        initListeners();
        initDatabase();
    }

    private void initViews() {
        mEditText = findViewById(R.id.ssid_editText);
    }

    private void initDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mStatus = database.getReference("status");

        mStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(int.class);
                if (value != null) {
                    if (value == 1 && isAlarmEnabled() && !isHomeNetwork()) {
                        mStopButton.setVisibility(View.VISIBLE);
                        NotificationUtils.sendNotification(HomeActivity.this);
                    } else {
                        mStopButton.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError databaseError) {

            }
        });
    }

    private boolean isAlarmEnabled() {
        if (mManager == null) {
            mManager = SharedPreferenceManager.getInstance(this);
        }
        return mManager.isAlarmEnabled();
    }

    private boolean isHomeNetwork() {
        if (mManager == null) {
            mManager = SharedPreferenceManager.getInstance(this);
        }
        String ssid = NetworkUtils.getCurrentSsid(HomeActivity.this);
        Set<String> ssids = mManager.getSSIDs();

        return ssids != null && ssids.contains(ssid);
    }

    private void initListeners() {
        initStopButtonListener();
        initSSIDTextListener();
        initCheckBoxListener();
        initAddButtonListener();
    }

    private void initCheckBoxListener() {
        final CheckBox checkBox = findViewById(R.id.enable_alarm_checkbox);
        if (mManager == null) {
            mManager = SharedPreferenceManager.getInstance(this);
        }
        checkBox.setChecked(mManager.isAlarmEnabled());

        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                mManager.setEnableAlarm(isChecked);
            }
        });
    }

    private void initSSIDTextListener() {
        TextView ssidTextView = findViewById(R.id.get_ssid_textview);
        ssidTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                String ssid = NetworkUtils.getCurrentSsid(HomeActivity.this);
                mEditText.setText(ssid);
            }
        });
    }

    private void initAddButtonListener() {
        ImageView addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mManager == null) {
                            mManager = SharedPreferenceManager.getInstance(HomeActivity.this);
                        }
                        Set<String> ssids = mManager.getSSIDs();
                        if (ssids == null) {
                            ssids = new HashSet<>();
                        }
                        if (mEditText.getText() != null && !TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                            ssids.add(mEditText.getText().toString().trim());
                        }
                        mManager.setSSIDs(ssids);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "SSID Added", Toast.LENGTH_LONG).show();
                                mEditText.setText("");
                            }
                        });
                    }
                });
            }
        });
    }

    private void initStopButtonListener() {
        mStopButton = findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mStatus == null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    mStatus = database.getReference("status");
                }

                mStatus.setValue(0);
                //Stop the alarm
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.cancel(0);
                }

                mStopButton.setVisibility(View.INVISIBLE);
                Toast.makeText(mStopButton.getContext(), "Done", Toast.LENGTH_LONG).show();
            }
        });
    }
}
