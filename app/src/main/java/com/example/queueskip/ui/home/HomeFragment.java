package com.example.queueskip.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.queueskip.Database.Local.CartDatabase;
import com.example.queueskip.Database.ModelDB.Cart;
import com.example.queueskip.LoginActivity;
import com.example.queueskip.R;
import com.example.queueskip.SignupActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
/*
public class HomeFragment extends Fragment {
    private Button scan;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //removed comment
        scan = (Button)view.findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ScanActivity.class));
            }
        });

        return view;

    }
}


 */

public class HomeFragment extends Fragment {

private HomeViewModel homeViewModel;
    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID=1001;
    private Button closeBtn;
    private TextView productNameTxt;
    private TextView productPriceTxt;
    private Button addBTn;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context=getContext().getApplicationContext();


        cameraPreview = (SurfaceView)view.findViewById(R.id.cameraPreview);
        txtResult= (TextView)view.findViewById(R.id.txtResult);

        barcodeDetector=new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getContext(),barcodeDetector).setRequestedPreviewSize(640,480).build();
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
       // final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



        //add event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED){
                    //request permission
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;

                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes=detections.getDetectedItems();
                if(qrcodes.size() != 0){
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.stop();//check
                            //create vibrate
                            Vibrator vibrator=(Vibrator)getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            //txtResult.setText(qrcodes.valueAt(0).displayValue);
                            //----------------
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.product_dialog);
                            closeBtn=dialog.findViewById(R.id.button_close);
                            productNameTxt=dialog.findViewById(R.id.product_name_dialog);
                            productPriceTxt=dialog.findViewById( R.id.product_price_dialog );
                            addBTn=dialog.findViewById( R.id.Add);


                            productNameTxt.setText(qrcodes.valueAt(0).displayValue);
                            int s = productNameTxt.getText().toString().indexOf('P');
                            int e = productNameTxt.getText().toString().indexOf('E');

                            productPriceTxt.setText(productNameTxt.getText().toString().substring(s,e));

                            addBTn.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Cart cart=new Cart(productNameTxt.getText().toString());
                                    cart.setName( (String) productNameTxt.getText() );
                                    //cart.setPrice(Integer.parseInt( (String ) productPriceTxt.getText()  ));
                                    CartDatabase.getInstance(getActivity().getApplicationContext()).cartDAO().insertToCart(cart); //?

                                    Toast.makeText(getActivity(),"Item added successfully",Toast.LENGTH_SHORT).show();

                                  /*  int items1 = CartDatabase.getInstance(getActivity().getApplicationContext()).cartDAO().countCartItems();
                                    String items = String.valueOf(items1);
                                    Toast.makeText(getActivity(),"Items:" + items,Toast.LENGTH_SHORT).show();
                                    */


                                }
                            } );


                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                dialog.cancel();
                                                                
                                                            }//end of onClick
                                                        }//end of OnClickListener
                            );


                            dialog.show();
                            //-----------------
                            /*
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                            alertDialog.setMessage(qrcodes.valueAt(0).displayValue);


                            alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }//end of onClick
                                    }//end of OnClickListener
                            );

                            alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }//end of onClick
                                    }//end of OnClickListener
                            );
                            alertDialog.show();

                             */
                        }
                    });
                }
            }
        });
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED){

                        return;

                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            break;
        }
    }
}




