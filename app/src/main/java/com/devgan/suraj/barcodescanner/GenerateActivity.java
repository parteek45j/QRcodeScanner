package com.devgan.suraj.barcodescanner;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenerateActivity extends AppCompatActivity {
    EditText text;
    ImageView imgcode;
    String text2Qr="";
    private static final String IMAGE_DIRECTORY = "/QRcodeImages";
    ImageButton buttonWhatsapp;
    Bitmap bitmap;
    File f;
    String path="";
    long date;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        text = (findViewById(R.id.edt));
        imgcode = (findViewById(R.id.itscode));
        buttonWhatsapp = (findViewById(R.id.whatsapp));
        buttonWhatsapp.setBackgroundResource(R.drawable.whatapp1);
        buttonWhatsapp.setEnabled(false);
        date= System.currentTimeMillis();

    }

    public void btngenerate(View view) {
        if (text.getText().length()==0){
            Toast.makeText(this, "Please Enter Text", Toast.LENGTH_SHORT).show();
            return;
        }
        buttonWhatsapp.setBackgroundResource(R.drawable.whatapp);
        buttonWhatsapp.setEnabled(true);
        text2Qr = text.getText().toString().trim();
        MultiFormatWriter formatWriter = new MultiFormatWriter();

        try {
            BitMatrix matrix = formatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder encoder = new BarcodeEncoder();
             bitmap = encoder.createBitmap(matrix);
            imgcode.setImageBitmap(bitmap);
            path = saveImage(bitmap);  //give read write permission
            Toast.makeText(GenerateActivity.this, "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, date + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    public void btnWhatspp(View view) {
        if (path.equals("")){
            Toast.makeText(this, "Generate QR Code First", Toast.LENGTH_SHORT).show();
        }else {

            Uri uri = Uri.parse(path);
            Log.e("path",path);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setPackage("com.whatsapp");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("image/jpg");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
