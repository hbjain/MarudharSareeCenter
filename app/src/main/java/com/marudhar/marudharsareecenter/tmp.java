package com.marudhar.marudharsareecenter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.StringTokenizer;


public class tmp extends AppCompatActivity {

    private View.OnClickListener l;
    private static View mainView;
    private String [] orderFileName;
    private TableLayout custOrderTable;

    private void getOrderFile()
    {

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

        if(directory.isDirectory())
        {
            orderFileName = directory.list();
        }

    }

    private void DisplayOrderInfo(TableLayout orderTable, int index, String fileName)
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);


        StringTokenizer fields = new StringTokenizer(fileName, "_");
        if(fields.countTokens() > 1)
        {
            String tmp = fields.nextToken();
            String custName = fields.nextToken();
            String orderDate = fields.nextToken();
            tmp = fields.nextToken();
            StringTokenizer tmpFields = new StringTokenizer(tmp, ".");
            String orderTime = tmpFields.nextToken();
            TableRow row = new TableRow(this);
            TextView slno = new TextView(this);
            TextView name = new TextView(this);
            TextView date = new TextView(this);
            TextView time = new TextView(this);

            slno.setText(Integer.toString(index));
            slno.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
            slno.setPadding(3,3,3,3);
            slno.setTextSize(18);

            name.setText(custName);
            name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
            name.setPadding(3,3,3,3);
            name.setTextSize(18);

            date.setText(orderDate);
            date.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
            date.setPadding(3,3,3,3);
            date.setTextSize(18);

            time.setText(orderTime);
            time.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
            time.setPadding(3,3,3,3);
            time.setTextSize(18);

            row.addView(slno);
            row.addView(name);
            row.addView(date);
            row.addView(time);
            row.setTag(fileName);
            row.setOnClickListener(l);
            orderTable.addView(row);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmp123);

        custOrderTable = (TableLayout)findViewById(R.id.viewOrderTbl);

        getOrderFile();

        l = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

                File orderFile = new File(directory , (String)(v.getTag()));
                if(orderFile.exists())
                {
                    Intent i = new Intent(getApplicationContext(), ViewOrderForm.class);
                    i.putExtra("FileName",(String)(v.getTag()));
                    startActivity(i);
                }
            }
        };

        for(int index = 0; index < orderFileName.length; index++)
        {
            DisplayOrderInfo(custOrderTable, index, orderFileName[index]);
        }

    }
}
