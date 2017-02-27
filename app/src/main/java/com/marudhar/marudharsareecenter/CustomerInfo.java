package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class CustomerInfo extends AppCompatActivity {


    BufferedReader inReader;
    File cust_file;
    TableLayout searchTbl;

     public void fillTable(TableLayout searchTbl, String name, String address, String tin)
     {
         TableRow temp = new TableRow(this);

         TextView tv1 = new TextView(this);
         TextView tv2 = new TextView(this);
         TextView tv3 = new TextView(this);
         tv1.setText(name);

         tv1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
         //tv1.setLayoutParams(tvtmp.getLayoutParams());

         tv2.setText(address);
         tv2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
         //tv2.setBackgroundDrawable(tvtmp.getBackground());
         //tv2.setLayoutParams(tvtmp.getLayoutParams());
         tv3.setText(tin);
         tv3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
         //tv3.setBackgroundDrawable(tvtmp.getBackground());
         //tv3.setLayoutParams(tvtmp.getLayoutParams());
         temp.addView(tv1);
         temp.addView(tv2);
         temp.addView(tv3);
         //temp.setLayoutParams(tmpTblrow.getLayoutParams());
         searchTbl.addView(temp);
         searchTbl.setShrinkAllColumns(true);
     }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchTbl = (TableLayout) findViewById(R.id.searchInfoTableLayout);
        //setSupportActionBar(toolbar);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.CustomerInfo, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.CustomerInfo);

        cust_file = new File(directory , GlobalVar.customer_details);
        if(cust_file.exists()) {
            cust_file.setReadable(true);

            try
            {
                FileReader fd = new FileReader(cust_file);

                inReader = new BufferedReader(fd);
                try {
                    String fileStr = inReader.readLine();
                    while (fileStr != null) {
                        StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                        String name = tokens.nextToken();
                        String address = tokens.nextToken();
                        String tin = tokens.nextToken();
                        fillTable(searchTbl, name, address, tin);
                        fileStr = inReader.readLine();
                    }
                } catch (IOException e) {
                }
            }catch (FileNotFoundException e) {}
        }

        Button searchItem = (Button) findViewById(R.id.SearchItem);
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firmNameorTin = (EditText) findViewById(R.id.NameOrTIN);

                if(firmNameorTin.getText().toString().isEmpty()) {
                      Snackbar.make(view, "Enter Name or TIN first", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }
                else
                {
                    searchTbl.removeViews(1, searchTbl.getChildCount()-1);
                    String str = firmNameorTin.getText().toString();
                    try
                    {
                        FileReader fd = new FileReader(cust_file);

                        {
                            inReader = new BufferedReader(fd);
                            try
                            {
                                String fileStr = inReader.readLine();
                                while (fileStr!= null)
                                {
                                    StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                                    String name = tokens.nextToken();
                                    String address = tokens.nextToken();
                                    String tin = tokens.nextToken();
                                    if (name.matches("(.*)" + str + "(.*)") || tin.matches("(.*)" + str + "(.*)"))
                                    {
                                        fillTable(searchTbl,name,address,tin);
                                        Snackbar.make(view, fileStr, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                    fileStr = inReader.readLine();
                                }
                            }catch (IOException e) {}
                        }
                    }catch (FileNotFoundException e){}
                }

            }
        });

        EditText searchText = (EditText) findViewById(R.id.NameOrTIN);
        searchText.setOnKeyListener(new EditText.OnKeyListener(){
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)  {
                EditText firmNameorTin = (EditText) findViewById(R.id.NameOrTIN);


                {
                    searchTbl.removeViews(1, searchTbl.getChildCount()-1);
                    String str = firmNameorTin.getText().toString();
                    try
                    {
                        FileReader fd = new FileReader(cust_file);

                        {
                            inReader = new BufferedReader(fd);
                            try
                            {
                                String fileStr = inReader.readLine();
                                while (fileStr!= null)
                                {
                                    StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                                    String name = tokens.nextToken();
                                    String address = tokens.nextToken();
                                    String tin = tokens.nextToken();
                                    if (name.matches("(.*)" + str + "(.*)") || tin.matches("(.*)" + str + "(.*)"))
                                    {
                                        fillTable(searchTbl,name,address,tin);
                                        Snackbar.make(view, fileStr, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                    fileStr = inReader.readLine();
                                }
                            }catch (IOException e) {}
                        }
                    }catch (FileNotFoundException e){}
                }
                return false;
            }

        });
    }

    public void onPause() {
        try {
             if(inReader != null) {
                 inReader.close();
             }
        }catch (IOException e){

        }

        super.onPause();
    }


}
