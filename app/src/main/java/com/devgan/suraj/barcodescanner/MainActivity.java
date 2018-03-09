package com.devgan.suraj.barcodescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView txtresult;
    Button scan;
    Uri uri, imageUri;
    Bitmap mbitmap;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan = (findViewById(R.id.scan));
        img = (findViewById(R.id.imgview));
        txtresult = (findViewById(R.id.txtResult));
        scan.setVisibility(View.GONE);

    }


    public void btnScan(View view) {
        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        Frame frame = new Frame.Builder()
                .setBitmap(mbitmap).build();
        SparseArray<Barcode> barcodeSparseArray = detector.detect(frame);
        if (barcodeSparseArray.size()>0) {
            Barcode result = barcodeSparseArray.valueAt(0);
//            txtresult.setText(result.rawValue);
            String serviceOTP=scanTxt(result.rawValue);
            shareWhatsapp(serviceOTP);
        }else {
            Toast.makeText(this, "Select a QR code Or QR Code is Not Clear", Toast.LENGTH_SHORT).show();
        }



    }


    public void SelectImg(View view) {

        Intent i = new Intent();
        i.setType("image/jpeg");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Qr Code"),PICK_IMAGE);
        txtresult.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            uri = data.getData();
            try {
                mbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                img.setImageBitmap(mbitmap);
                scan.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {

            }



        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,101,0,"Scan QR Code");
        menu.add(2,102,0,"Generate QR Code");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        switch (i){
            case 101:
                startActivity(new Intent(this,CameraActivity.class));
                break;
            case 102:
                startActivity(new Intent(this,GenerateActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    String scanTxt(String raw){
        StringBuilder Id=new StringBuilder();
        for (int i=0;i<=raw.length()-1;i++) {
            char d=raw.charAt(i);
            if (Character.isLetter(d)){
                int ASCII=d;
                ASCII=ASCII+i;
                if(ASCII==91) {
                    d='o';
                }else if(ASCII==92) {
                    d='p';
                }else if(ASCII==93) {
                    d='q';
                }else if(ASCII==94) {
                    d='r';
                }else if(ASCII==95) {
                    d='s';
                }else if(ASCII==96) {
                    d='t';
                }else {
                    d=(char)ASCII;
                }
                Id.append(d);
            }else {
                Id.append(d);
            }
        }
        Toast.makeText(this, Id, Toast.LENGTH_SHORT).show();
        return String.valueOf(Id);
    }
    void shareWhatsapp(String text){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"hey");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(intent,"Share Using"));
    }
}
