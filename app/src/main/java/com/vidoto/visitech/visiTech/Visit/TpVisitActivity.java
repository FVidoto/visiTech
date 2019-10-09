package com.vidoto.visitech.visiTech.Visit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import xyz.hasnat.sweettoast.SweetToast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vidoto.visitech.visiTech.Model.TpVisit;
import com.vidoto.visitech.visiTech.R;

public class TpVisitActivity extends AppCompatActivity {

    private TextView tvIdTpVisit;
    private EditText edTpVisit, edDepartament;
    private Button btnSave, btnDel;
    private String sIdTpVisit, sTpVisit, sDepartament, sCurrent_user_id;
    private long maxid;
    private TpVisit tpVisit;


    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private DatabaseReference tpVisitDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp_visit);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        tpVisitDatabaseReference = FirebaseDatabase.getInstance().getReference().child("tpvisit");
        tpVisitDatabaseReference.keepSynced(true); // for offline

        tvIdTpVisit = findViewById(R.id.tvIdTpVisit);
        edTpVisit = findViewById(R.id.inputTpVisit);
        edDepartament = findViewById(R.id.inputDept);
        btnSave = findViewById(R.id.saveButton);
        btnDel = findViewById(R.id.delButton);




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sIdTpVisit = tvIdTpVisit.getText().toString();
                sTpVisit = edTpVisit.getText().toString();
                sDepartament = edDepartament.getText().toString();

                saveTpVisit(sTpVisit, sDepartament);

            }
        });



    }

    private void saveTpVisit(String vTpVisit, String vDepartament) {
        /*Cadastro de novo tipo de Visita*/
        tpVisit = new TpVisit();

        tpVisitDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid=(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tpVisit.setTp_visit(vTpVisit);
        tpVisit.setDepartment(vDepartament);

        tpVisitDatabaseReference.child(String.valueOf(maxid+1)).setValue(tpVisit);
        SweetToast.info(TpVisitActivity.this, "Tipo de Visita Cadastrada com suscesso. ID: " + (maxid+1));
        /*fim - Cadastro de novo tipo de Visita*/
        Snackbar.make(view, "Adicionado Novo Item", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
