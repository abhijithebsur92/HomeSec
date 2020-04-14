package com.comp6441.homesec;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.homesec.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mStatus;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mStatus = database.getReference("status");

        final Button stopButton = findViewById(R.id.stop_button);

        mStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(int.class);
                if (value != null) {
                    stopButton.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError databaseError) {

            }
        });

        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mStatus == null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    mStatus = database.getReference("status");
                }

                mStatus.setValue(0);
                Toast.makeText(stopButton.getContext(), "Done", Toast.LENGTH_LONG).show();
            }
        });
    }
}
