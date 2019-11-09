package com.example.queueskip.ui.profile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.queueskip.EditProfile;
import com.example.queueskip.HistoryActivity;
import com.example.queueskip.Items;
import com.example.queueskip.R;
import com.example.queueskip.User;
import com.example.queueskip.ui.dashboard.DashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class profile_fragment extends Fragment {

    DatabaseReference ref;
    private TextView email;
    private TextView pass;
    private TextView username;
    private FirebaseAuth firebaseAuth;
    private Button edit;
    private ImageView history;


    String email2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

      //  ViewModelProviders.of(this).get(DashboardViewModel.class);
        ViewModelProviders.of(this);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ref= FirebaseDatabase.getInstance().getReference().child("User");

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        //TextView
        email = view.findViewById(R.id.emailProf);
        pass = view.findViewById(R.id.passProf);
        username = view.findViewById(R.id.usernameProf);
        history = view.findViewById(R.id.historyIc);

        //edit Button
        edit = view.findViewById(R.id.editBtn);


        email.setText(user.getEmail());
        email2 = user.getEmail();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   User userDB = snapshot.getValue(User.class);

                   if(userDB.getEmail().equals(email2)){
                       pass.setText(userDB.getPassword());
                       email.setText(userDB.getEmail());
                       username.setText("Hello, "+userDB.getUsername());
                   }
                    //Toast.makeText(getActivity(),"SUCCESSFULL PROFILE", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfile.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HistoryActivity.class));
            }
        });

        return view;
    }

}
