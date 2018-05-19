package de.tum.in.tumcampusapp.component.ui.tufilm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import de.tum.in.tumcampusapp.R;
import de.tum.in.tumcampusapp.component.other.generic.activity.BaseActivity;
import de.tum.in.tumcampusapp.utils.Const;

public class show_ticket extends BaseActivity {

    private TextView moviedetailTextView;
    private TextView ticketsnumberTextView;
    private ImageView ticketqrcode;

    public show_ticket() {
        super(R.layout.activity_show_ticket);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviedetailTextView = (TextView)findViewById(R.id.moviedetail);
        ticketsnumberTextView = (TextView)findViewById(R.id.ticketnumber);
        ticketqrcode = (ImageView) findViewById(R.id.ticket_qrcode);
        //get data from last activity
        Intent intent = getIntent();
        String data = getIntent().getStringExtra("movie_data");
        moviedetailTextView.setText(data);
        ticketsnumberTextView.setText("87237489273984");//data from backend

        String text="KingsMan 08.05 Kailiang Dong "; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ticketqrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}

