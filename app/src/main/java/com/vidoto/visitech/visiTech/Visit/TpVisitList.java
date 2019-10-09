package com.vidoto.visitech.visiTech.Visit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vidoto.visitech.visiTech.Model.TpVisit;
import com.vidoto.visitech.visiTech.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.hasnat.sweettoast.SweetToast;

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
    private long maxid;

    String current_user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpvisit_list);

        toolbar = findViewById(R.id.tpVisit_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TpVisit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        tpVisitDatabaseReference = FirebaseDatabase.getInstance().getReference().child("tpvisit").child(current_user_id);
        tpVisitDatabaseReference.keepSynced(true); // for offline

        // Setup recycler view
        tpVisit_list_RV = findViewById(R.id.tpVisitList);
        tpVisit_list_RV.setHasFixedSize(true);
        tpVisit_list_RV.setLayoutManager(new LinearLayoutManager(this));



//        showPeopleList();
        showtpVisitsList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // chamar a intent TpVisitActivity
            }
        });


    }

    private void showtpVisitsList(){
        FirebaseRecyclerOptions<TpVisit> recyclerOptions = new FirebaseRecyclerOptions.Builder<TpVisit>()
                .setQuery(tpVisitDatabaseReference, TpVisit.class)
                .build();

        FirebaseRecyclerAdapter<TpVisit, TpVisitVH> recyclerAdapter = new FirebaseRecyclerAdapter<TpVisit, TpVisitVH>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final TpVisitVH holder, int position, @NonNull TpVisit model) {
//                holder.date.setText("Friendship date -\n" + model.getDate());

                final String userID = getRef(position).getKey();

                tpVisitDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        String tp_visit = dataSnapshot.child("tp_visit").getValue().toString();
                        String department = dataSnapshot.child("department").getValue().toString();

                        holder.tp_visit.setText(tp_visit);
                        holder.department.setText(department);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @NonNull
            @Override
            public TpVisitVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_simple_text_list, viewGroup, false);
                return new TpVisitVH(view);
            }
        };

        tpVisit_list_RV.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }

    public static class TpVisitVH extends RecyclerView.ViewHolder{
        TextView tp_visit;
        TextView department;

        public TpVisitVH(View itemView) {
            super(itemView);
            tp_visit = itemView.findViewById(R.id.all_text_1);
            department = itemView.findViewById(R.id.all_text_2);
        }
    }

}
