package com.example.madhav.mavme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by madhav on 10/25/16.
 */
public class BaseActivity extends AppCompatActivity {

    private Context context;

    public void loadMenu(int page) {

        // inflating menu view
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.menu, null);
        this.context = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        // stretching to fit entire action ar
        actionBar.setCustomView(v, layoutParams);
        Toolbar parent = (Toolbar) v.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        // adding button functionality
        addListenerOnMenu(v);
        addColors(v, page);
    }


    public void addColors(View v, int page) {
        ArrayList<ImageButton> icons = new ArrayList<ImageButton>();
        ArrayList<TextView> labels = new ArrayList<TextView>();


        icons.add((ImageButton) v.findViewById(R.id.icon1));
        icons.add((ImageButton) v.findViewById(R.id.icon2));
        icons.add((ImageButton) v.findViewById(R.id.icon3));
        icons.add((ImageButton) v.findViewById(R.id.icon4));

        labels.add((TextView) v.findViewById(R.id.label1));
        labels.add((TextView) v.findViewById(R.id.label2));
        labels.add((TextView) v.findViewById(R.id.label3));
        labels.add((TextView) v.findViewById(R.id.label4));

        for (int i = 0; i < icons.size(); i++) {
            if (page == i) {
                icons.get(i).getBackground().setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY));
                labels.get(i).setTextColor(Color.parseColor("#0000ff"));
            } else {
                icons.get(i).getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffa500"), PorterDuff.Mode.MULTIPLY));
                labels.get(i).setTextColor(Color.parseColor("#ffa500"));
            }
        }


    }

    public void addListenerOnMenu(View v) {

        final ImageButton item1 = (ImageButton) v.findViewById(R.id.icon1);
        final ImageButton item2 = (ImageButton) v.findViewById(R.id.icon2);
        final ImageButton item3 = (ImageButton) v.findViewById(R.id.icon3);
        final ImageButton item4 = (ImageButton) v.findViewById(R.id.icon4);
        final ImageButton item5 = (ImageButton) v.findViewById(R.id.icon5);


        item1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("com.example.madhav.com.mavme.HomeDisplay");
                startActivity(i);
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("com.example.madhav.com.mavme.GroupListDisplay");
                startActivity(i);
            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("com.example.madhav.mavme.SearchDisplay");
                startActivity(i);
            }
        });

        item4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("com.example.madhav.mavme.ProfileDisplay");
                startActivity(i);
            }
        });

        item5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.alertDialog(context, "Logout successful.", new Utils.dialogHandler() {
                    @Override
                    public void onButtonClick(boolean click) {
                        Intent i = new Intent("com.example.madhav.mavme.LoginDisplay");
                        startActivity(i);
                    }
                });
            }
        });
    }


}
