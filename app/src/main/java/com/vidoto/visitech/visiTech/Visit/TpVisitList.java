package com.vidoto.visitech.visiTech.Visit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.vidoto.visitech.visiTech.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TpVisitList extends AppCompatActivity {

    /*
    * this.tp_visit = tp_visit;
        this.department = department;
    * */

    private Toolbar toolbar;
    private RecyclerView tpVisit_list_RV;

    private DatabaseReference tpVisitDatabaseReference;
    private FirebaseAuth mAuth;
    private TpVisit tpVisit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpvisit_list);

        toolbar = findViewById(R.id.tpVisit_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TpVisit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }
}
