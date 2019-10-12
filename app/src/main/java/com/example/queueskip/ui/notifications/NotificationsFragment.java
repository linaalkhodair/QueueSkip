//package com.example.queueskip.ui.notifications;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//
//import com.example.queueskip.LoginActivity;
//import com.example.queueskip.MainActivity;
//import com.example.queueskip.R;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class NotificationsFragment extends Fragment {
//
//    private NotificationsViewModel notificationsViewModel;
//    private Button logout;
//
//    private FirebaseAuth firebaseAuth;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
////        notificationsViewModel =
////                ViewModelProviders.of(this).get(NotificationsViewModel.class);
//        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
////        final TextView textView = root.findViewById(R.id.text_notifications);
////        notificationsViewModel.getText().observe(this, new Observer<String>() {
////            @Override
////            public void onChanged(@Nullable String s) {
////                textView.setText(s);
////            }
////        });
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        logout = (Button)view.findViewById(R.id.logoutBtn);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logout();
//            }
//        });
//        return view;
//    }
//    public void logout(){
//        firebaseAuth.signOut();
//        //finish();
//       // getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//        startActivity(new Intent(getContext(), LoginActivity.class));
//       // getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//    }
//}