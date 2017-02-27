package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;

public class OrderScroll extends AppCompatActivity {

    String [] itemName;
    String [] itemQty;
    String [] itemPrice;
    String [] itemRemark;
    String customerName;
    int itemCount;
    //Context context = ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_scroll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent myIntent = getIntent();
        itemName = myIntent.getStringArrayExtra("ItemName");
        itemQty = myIntent.getStringArrayExtra("ItemQty");
        itemPrice = myIntent.getStringArrayExtra("ItemPrice");
        itemRemark = myIntent.getStringArrayExtra("ItemRemark");
        itemCount = myIntent.getIntExtra("count", 0);
        customerName = myIntent.getStringExtra("Customer Name");


        final GridView gridview = (GridView) findViewById(R.id.orderItemGrid);
        gridview.setAdapter(new GridViewImageAdapter(this, itemName, itemQty, customerName, itemCount));

        Button next = (Button) findViewById(R.id.MoveToFinish);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemName = ((GridViewImageAdapter)(gridview.getAdapter())).getItemName();
                itemQty = ((GridViewImageAdapter)(gridview.getAdapter())).getItemQty();
                itemCount = ((GridViewImageAdapter)(gridview.getAdapter())).getItemCount();

                Intent showOrder = new Intent(getApplicationContext(), ShowOrder.class);
                showOrder.putExtra("ItemName", itemName);
                showOrder.putExtra("ItemQty", itemQty);
                showOrder.putExtra("ItemPrice", itemPrice);
                showOrder.putExtra("ItemRemark", itemRemark);
                showOrder.putExtra("count", itemCount);
                showOrder.putExtra("Customer Name", customerName);
                startActivity(showOrder);

            }
        });
    }
}
