package com.vidoto.visitech.visiTech.Friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.vidoto.visitech.visiTech.Chat.ChatActivity;
import com.vidoto.visitech.visiTech.Model.Friends;
import com.vidoto.visitech.visiTech.Model.Visit;
import com.vidoto.visitech.visiTech.Profile.ProfileActivity;
import com.vidoto.visitech.visiTech.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hasnat.sweettoast.SweetToast;

public class FriendsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView friend_list_RV;

    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth mAuth;

    private DatabaseReference visitDatabaseReference;
    private Visit visit;
    private long maxid;
    private Button btnAddVisit, btnDelVisit;
    private EditText edID;

    String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        toolbar = findViewById(R.id.friends_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends").child(current_user_id);
        friendsDatabaseReference.keepSynced(true); // for offline

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        userDatabaseReference.keepSynced(true); // for offline

        visitDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Visit");

        // Setup recycler view
        friend_list_RV = findViewById(R.id.friendList);
        friend_list_RV.setHasFixedSize(true);
        friend_list_RV.setLayoutManager(new LinearLayoutManager(this));

//        showPeopleList();
        showVisitsList();

        //***********************************************************************************************************************************************
        visit = new Visit();
        btnAddVisit=(Button)findViewById(R.id.btnAddVisit);
        btnDelVisit=(Button)findViewById(R.id.btnDelVisit);
        edID=(EditText)findViewById(R.id.edID);

        visitDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid=(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visit.setTp_visit("Problema");
                visit.setCall_erp("124");
                visit.setCustomer("Marchesan");
                visit.setProduct("Plantadeira");

                visitDatabaseReference.child(String.valueOf(maxid+1)).setValue(visit);
                SweetToast.info(FriendsActivity.this, "Visita Cadastrada com suscesso. ID: " + (maxid+1));

            }
        });

        btnDelVisit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = String.valueOf(edID.getText());
                visitDatabaseReference.child(id).removeValue();
                SweetToast.info(FriendsActivity.this, "Visita Removida com suscesso. ID: " + (id));
            }
        });

        //***********************************************************************************************************************************************


    }

    /**
     *  FirebaseUI for Android — UI Bindings for Firebase
     *
     *  Library link- https://github.com/firebase/FirebaseUI-Android
     */
//    private void showPeopleList(){
//        FirebaseRecyclerOptions<Friends> recyclerOptions = new FirebaseRecyclerOptions.Builder<Friends>()
//                .setQuery(friendsDatabaseReference, Friends.class)
//                .build();
//
//        FirebaseRecyclerAdapter<Friends, FriendsVH> recyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsVH>(recyclerOptions) {
//            @Override
//            protected void onBindViewHolder(@NonNull final FriendsVH holder, int position, @NonNull Friends model) {
//                holder.date.setText("Friendship date -\n" + model.getDate());
//
//                final String userID = getRef(position).getKey();
//
//                userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                        final String userName = dataSnapshot.child("user_name").getValue().toString();
//                        String userThumbPhoto = dataSnapshot.child("user_thumb_image").getValue().toString();
//                        String active_status = dataSnapshot.child("active_now").getValue().toString();
//
//                        // online active status
//                        holder.active_icon.setVisibility(View.GONE);
//                        if (active_status.contains("active_now")){
//                            holder.active_icon.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.active_icon.setVisibility(View.GONE);
//                        }
//
//                        holder.name.setText(userName);
//                        Picasso.get()
//                                .load(userThumbPhoto)
//                                .networkPolicy(NetworkPolicy.OFFLINE) // for Offline
//                                .placeholder(R.drawable.default_profile_image)
//                                .into(holder.profile_thumb);
//
//
//                        //click item, 2 options in a dialogue will be appear
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                CharSequence options[] =  new CharSequence[]{"Send Message", userName+"'s profile"};
//                                AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (which == 0){
//                                            // user active status validation
//                                            if (dataSnapshot.child("active_now").exists()){
//
//                                                Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
//                                                chatIntent.putExtra("visitUserId", userID);
//                                                chatIntent.putExtra("userName", userName);
//                                                startActivity(chatIntent);
//
//                                            } else {
//                                                userDatabaseReference.child(userID).child("active_now")
//                                                        .setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
//                                                        chatIntent.putExtra("visitUserId", userID);
//                                                        chatIntent.putExtra("userName", userName);
//                                                        startActivity(chatIntent);
//                                                    }
//                                                });
//
//
//                                            }
//
//                                        }
//
//                                        if (which == 1){
//                                            Intent profileIntent = new Intent(FriendsActivity.this, ProfileActivity.class);
//                                            profileIntent.putExtra("visitUserId", userID);
//                                            startActivity(profileIntent);
//                                        }
//
//                                    }
//                                });
//                                builder.show();
//
//                            }
//                        });
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//
//
//            }
//
//            @NonNull
//            @Override
//            public FriendsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_single_profile_display, viewGroup, false);
//                return new FriendsVH(view);
//            }
//        };
//
//        friend_list_RV.setAdapter(recyclerAdapter);
//        recyclerAdapter.startListening();
//    }

    private void showVisitsList(){
        FirebaseRecyclerOptions<Visit> recyclerOptions = new FirebaseRecyclerOptions.Builder<Visit>()
                .setQuery(visitDatabaseReference, Visit.class)
                .build();

        FirebaseRecyclerAdapter<Visit, VisitVH> recyclerAdapter = new FirebaseRecyclerAdapter<Visit, VisitVH>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final VisitVH holder, int position, @NonNull Visit model) {
//                holder.date.setText("Friendship date -\n" + model.getDate());

                final String userID = getRef(position).getKey();

                visitDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        final String tp_visit = dataSnapshot.child("tp_visit").getValue().toString();
                        String product = dataSnapshot.child("product").getValue().toString();
                        String customer = dataSnapshot.child("customer").getValue().toString();
                        String call_erp = dataSnapshot.child("call_erp").getValue().toString();

                        // online active status
//                        holder.active_icon.setVisibility(View.GONE);
//                        if (active_status.contains("active_now")){
//                            holder.active_icon.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.active_icon.setVisibility(View.GONE);
//                        }

                        holder.tp_visit.setText(tp_visit);
                        holder.product.setText(product);
                        holder.customer.setText(customer);
                        holder.call_erp.setText(call_erp);

//                        Picasso.get()
//                                .load(userThumbPhoto)
//                                .networkPolicy(NetworkPolicy.OFFLINE) // for Offline
//                                .placeholder(R.drawable.default_profile_image)
//                                .into(holder.profile_thumb);


                        //click item, 2 options in a dialogue will be appear
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                CharSequence options[] =  new CharSequence[]{"Send Message", userName+"'s profile"};
//                                AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (which == 0){
//                                            // user active status validation
//                                            if (dataSnapshot.child("active_now").exists()){
//
//                                                Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
//                                                chatIntent.putExtra("visitUserId", userID);
//                                                chatIntent.putExtra("userName", userName);
//                                                startActivity(chatIntent);
//
//                                            } else {
//                                                userDatabaseReference.child(userID).child("active_now")
//                                                        .setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
//                                                        chatIntent.putExtra("visitUserId", userID);
//                                                        chatIntent.putExtra("userName", userName);
//                                                        startActivity(chatIntent);
//                                                    }
//                                                });
//
//
//                                            }
//
//                                        }
//
//                                        if (which == 1){
//                                            Intent profileIntent = new Intent(FriendsActivity.this, ProfileActivity.class);
//                                            profileIntent.putExtra("visitUserId", userID);
//                                            startActivity(profileIntent);
//                                        }
//
//                                    }
//                                });
//                                builder.show();
//
//                            }
//                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }

            @NonNull
            @Override
            public VisitVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_simple_text, viewGroup, false);
                return new VisitVH(view);
            }
        };

        friend_list_RV.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }

//    public static class FriendsVH extends RecyclerView.ViewHolder{
//        public TextView name;
//        TextView date;
//        CircleImageView profile_thumb;
//        ImageView active_icon;
//
//        public FriendsVH(View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.all_user_name);
//            date = itemView.findViewById(R.id.all_user_status);
//            profile_thumb = itemView.findViewById(R.id.all_user_profile_img);
//            active_icon = itemView.findViewById(R.id.activeIcon);
//        }
//    }

    public static class VisitVH extends RecyclerView.ViewHolder{
        public TextView tp_visit;
        TextView product;
        TextView customer;
        TextView call_erp;

        public VisitVH(View itemView) {
            super(itemView);
            tp_visit = itemView.findViewById(R.id.all_tp_visit);
            product = itemView.findViewById(R.id.all_product);
            customer = itemView.findViewById(R.id.all_customer);
            call_erp = itemView.findViewById(R.id.all_call_erp);
        }
    }

}
