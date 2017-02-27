package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class NewCustomer extends AppCompatActivity {


    File cust_file;
    TableLayout custTbl;
    View.OnClickListener tableCellListener;
    TableRow previousSelectedRow = null;
    List<String> custTable;
    boolean isCustTableBuilt = false;
    int displayedIndex = 201;
    boolean isCustTableUpdated = false;
    public Context contxt;

    private void buildCustTable()
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.CustomerInfo, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.CustomerInfo);
        cust_file = new File(directory , GlobalVar.customer_details);


        try {
            if (cust_file.exists()) {
                cust_file.setReadable(true);

                FileReader fd = new FileReader(cust_file);
                BufferedReader inReader = new BufferedReader(fd);

                String fileStr = inReader.readLine();
                while (fileStr != null) {
                    custTable.add(fileStr);
                    //custTable[custCount] = fileStr;
                    //custCount++;
                    fileStr = inReader.readLine();
                }

                inReader.close();

            }
        }catch(Exception e){}
    }


    private void writeCustTableToFile()
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.CustomerInfo, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.CustomerInfo);
        cust_file = new File(directory , GlobalVar.customer_details);


        try {
            if (isCustTableUpdated )
            {
                if(cust_file.exists())
                {
                    cust_file.delete();
                }
                cust_file.createNewFile();
                cust_file.setWritable(true);

                FileOutputStream custFileStream = new FileOutputStream(cust_file, true);

                for(int i = 0; i < custTable.size(); i++)
                {
                     String str = custTable.get(i) + "\n";
                     byte [] data = str.getBytes();
                     custFileStream.write(data);

                }
                custFileStream.close();
            }
        }catch(Exception e){}
    }

    private void fillTable(TableLayout inCustTbl,
                           String inFirmName,
                           String inCity,
                           String inPincode,
                           String inTin,
                           int inCustType)
    {
        TableRow temp = new TableRow(this);

        TextView firmName = new TextView(this);
        TextView tin = new TextView(this);
        TextView city = new TextView(this);
        TextView pinCode = new TextView(this);
        TextView custRating = new TextView(this);


        int [] colorValue= new int[3];
        colorValue[0] = Color.BLUE;
        colorValue[1] = Color.GREEN;
        colorValue[2 ] = Color.YELLOW;

        String [] customerRating = new String[4];
        customerRating[0] = "-";
        customerRating[1] = "A";
        customerRating[2] = "B";
        customerRating[3] = "C";

        firmName.setTextSize(20);
        tin.setTextSize(20);
        city.setTextSize(20);
        pinCode.setTextSize(20);
        custRating.setTextSize(20);

        firmName.setPadding(8,8,8,8);
        tin.setPadding(8,8,8,8);
        city.setPadding(8,8,8,8);
        pinCode.setPadding(8,8,8,8);
        custRating.setPadding(8,8,8,8);

        firmName.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        tin.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        city.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        pinCode.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        custRating.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));

        //Spinner cust_type = (Spinner) findViewById(R.id.spinner);
        //cust_type.setSelection(inCustType);

        firmName.setBackgroundColor(colorValue[(inCustType + 2) % 3]);
        firmName.getBackground().setAlpha(90);

        tin.setBackgroundColor(colorValue[(inCustType + 2) % 3]);
        tin.getBackground().setAlpha(90);

        city.setBackgroundColor(colorValue[(inCustType + 2) % 3]);
        city.getBackground().setAlpha(90);

        pinCode.setBackgroundColor(colorValue[(inCustType + 2) % 3]);
        pinCode.getBackground().setAlpha(90);

        custRating.setBackgroundColor(colorValue[(inCustType + 2) % 3]);
        custRating.getBackground().setAlpha(90);

        firmName.setText(inFirmName);
        firmName.setTag(temp);
        //firmName.setOnClickListener(tableCellListener);

        tin.setText(inTin);
        tin.setTag(temp);
        //tin.setOnClickListener(tableCellListener);

        city.setText(inCity);
        city.setTag(temp);
        //city.setOnClickListener(tableCellListener);

        pinCode.setText(inPincode);
        pinCode.setTag(temp);
        //pinCode.setOnClickListener(tableCellListener);

        custRating.setText(customerRating[inCustType]);
        custRating.setTag(temp);

        temp.addView(firmName);
        temp.addView(tin);
        temp.addView(city);
        temp.addView(pinCode);
        temp.addView(custRating);

        temp.setOnClickListener(tableCellListener);
        inCustTbl.addView(temp);
        inCustTbl.setShrinkAllColumns(true);
    }

    private void DisplayCustTable(String str) {
        custTbl = (TableLayout) findViewById(R.id.custTableLayout);
        custTbl.removeViews(1, custTbl.getChildCount() - 1);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        if(str.isEmpty())
        {
            Button updateItem = (Button) findViewById(R.id.UpdateItem);
            updateItem.setEnabled(false);

            Button addItem = (Button) findViewById(R.id.AddItem);
            addItem.setEnabled(true);

            displayedIndex = 201;

        }

        Collections.sort(custTable, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        for (int i = 0; i < custTable.size(); i++) {

            StringTokenizer tokens = new StringTokenizer(custTable.get(i), "?");
            String[] fields = new String[8];
            int index = 0;
            for (index = 0; index < 8; index++) {
                fields[index] = "";
            }

            index = 0;
            while (tokens.hasMoreTokens()) {
                fields[index] = tokens.nextToken();
                index++;
            }
            if (fields[0].matches("(.*)" + str + "(.*)")) {
                fillTable(custTbl, fields[0], fields[2], fields[3], fields[6], Integer.parseInt(fields[7]));
            }
        }
    }

    private void UpdateCustForm(String custName){

        int i = 0;

        while(i < custTable.size())
        {

            StringTokenizer strTokens = new StringTokenizer(custTable.get(i), "?");
            String[] token = new String[9];

            int index = 0;
            for (index = 0; index < 9; index++) {
                token[index] = "";
            }

            index = 0;
            while (strTokens.hasMoreTokens()) {
                token[index] = strTokens.nextToken();
                index++;
            }


            if (token[0].matches(custName)) {
                displayedIndex = i;

                Button updateItem = (Button) findViewById(R.id.UpdateItem);
                updateItem.setEnabled(true);

                Button addItem = (Button) findViewById(R.id.AddItem);
                addItem.setEnabled(false);

                EditText firmName = (EditText) findViewById(R.id.FirmName);
                EditText Address = (EditText) findViewById(R.id.Address);
                EditText city = (EditText) findViewById(R.id.City);
                EditText pincode = (EditText) findViewById(R.id.PinCode);
                EditText tin = (EditText) findViewById(R.id.TIN);
                EditText phoneNumberPri = (EditText) findViewById(R.id.PhNumPri);
                EditText phoneNumberSec = (EditText) findViewById(R.id.PhNumSec);
                Spinner cust_type = (Spinner) findViewById(R.id.spinner);

                firmName.setText(token[0]);
                Address.setText(token[1]);
                city.setText(token[2]);
                pincode.setText(token[3]);
                phoneNumberPri.setText(token[4]);
                phoneNumberSec.setText(token[5]);
                tin.setText(token[6]);

                cust_type.setSelection(Integer.parseInt(token[7]));

                i = custTable.size() + 1;

            }
            i++;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        contxt = this.getApplicationContext();

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.CustomerInfo, Context.MODE_PRIVATE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        File directory = new File(GlobalVar.CustomerInfo);
        cust_file = new File(directory , GlobalVar.customer_details);

        Spinner cust_type = (Spinner) findViewById(R.id.spinner);
        cust_type.setSelection(0);


        custTable = new ArrayList<String>();
        Button updateItem = (Button) findViewById(R.id.UpdateItem);
        updateItem.setEnabled(false);
        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(displayedIndex != 201) {
                    EditText firmName = (EditText) findViewById(R.id.FirmName);
                    EditText Address = (EditText) findViewById(R.id.Address);
                    EditText city = (EditText) findViewById(R.id.City);
                    EditText pincode = (EditText) findViewById(R.id.PinCode);
                    EditText tin = (EditText) findViewById(R.id.TIN);
                    EditText phoneNumberPri = (EditText) findViewById(R.id.PhNumPri);
                    EditText phoneNumberSec = (EditText) findViewById(R.id.PhNumSec);

                    Spinner cust_type = (Spinner) findViewById(R.id.spinner);

                    if (firmName.getText().toString().isEmpty() ||
                            Address.getText().toString().isEmpty() ||
                            tin.getText().toString().isEmpty() ||
                            cust_type.getSelectedItemPosition() == 0 ||
                            phoneNumberPri.getText().toString().isEmpty() ||
                            city.getText().toString().isEmpty() ||
                            pincode.getText().toString().isEmpty()
                            ) {
                        if (firmName.getText().toString().isEmpty()) {
                            firmName.requestFocus();
                            Snackbar.make(view, "Enter Name", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else if (Address.getText().toString().isEmpty()) {
                            Address.requestFocus();
                            Snackbar.make(view, "Enter Address", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else if (city.getText().toString().isEmpty()) {
                            Address.requestFocus();
                            Snackbar.make(view, "Enter City", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else if (pincode.getText().toString().isEmpty()) {
                            Address.requestFocus();
                            Snackbar.make(view, "Enter PinCode", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else if (phoneNumberPri.getText().toString().isEmpty()) {
                            phoneNumberPri.requestFocus();
                            Snackbar.make(view, "Enter Primary Phone Number", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else if (tin.getText().toString().isEmpty()) {
                            tin.requestFocus();
                            Snackbar.make(view, "Enter tin", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else if (cust_type.getSelectedItemPosition() == 0) {
                            cust_type.requestFocus();
                            Snackbar.make(view, "Enter CustomerInfo type", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    } else {

                        String phNumSec = "-";
                        if(phoneNumberSec.getText().toString().isEmpty() == false)
                        {
                            phNumSec = phoneNumberSec.getText().toString();
                        }
                        String str = firmName.getText().toString() + "?" +
                                Address.getText().toString() + "?" +
                                city.getText().toString() + "?" +
                                pincode.getText().toString() + "?" +
                                phoneNumberPri.getText().toString() + "?" +
                                phNumSec + "?" +
                                tin.getText().toString() + "?" +
                                Integer.toString(cust_type.getSelectedItemPosition());


                        //custTable[displayedIndex] = str;
                        custTable.add(displayedIndex, str);
                        isCustTableUpdated = true;


                        AlertDialog.Builder photobuilder = new AlertDialog.Builder(NewCustomer.this);
                        photobuilder.setTitle(firmName.getText().toString() + " details are updated");

                        photobuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface i, int j) {
                            }
                        });
                        AlertDialog dialog = photobuilder.create();
                        dialog.show();

                        firmName.setText("");
                        Address.setText("");
                        phoneNumberPri.setText("");
                        phoneNumberSec.setText("");
                        tin.setText("");
                        cust_type.setSelection(0);
                        city.setText("");
                        pincode.setText("");
                        firmName.requestFocus();



                        DisplayCustTable("");


                    }
                }

            }
        });

        if(cust_file.exists())
        {
            cust_file.setWritable(true);
        }
        else
        {
            try {
                cust_file.createNewFile();
                cust_file.setWritable(true);
            }catch(IOException e){
                }


        }


        tableCellListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                TableRow selectedRow = (TableRow)v;

                if(previousSelectedRow == null)
                {
                    previousSelectedRow = selectedRow;
                }
                else if(previousSelectedRow == selectedRow)
                {
                    UpdateCustForm(((TextView)(selectedRow.getChildAt(0))).getText().toString());
                }
                else
                {
                    previousSelectedRow = selectedRow;
                }


                Toast.makeText(getApplicationContext(), "Cell clicked", Toast.LENGTH_LONG).show();

            }
        };

        buildCustTable();

        DisplayCustTable("");

        Button addItem = (Button) findViewById(R.id.AddItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firmName = (EditText) findViewById(R.id.FirmName);
                EditText Address = (EditText) findViewById(R.id.Address);
                EditText city = (EditText) findViewById(R.id.City);
                EditText pincode = (EditText) findViewById(R.id.PinCode);
                EditText tin = (EditText) findViewById(R.id.TIN);
                EditText phoneNumberPri = (EditText) findViewById(R.id.PhNumPri);
                EditText phoneNumberSec = (EditText) findViewById(R.id.PhNumSec);

                Spinner cust_type = (Spinner) findViewById(R.id.spinner);

                if(firmName.getText().toString().isEmpty() ||
                        Address.getText().toString().isEmpty() ||
                        tin.getText().toString().isEmpty() ||
                        cust_type.getSelectedItemPosition() == 0 ||
                        phoneNumberPri.getText().toString().isEmpty() ||
                        city.getText().toString().isEmpty() ||
                        pincode.getText().toString().isEmpty()
                        ) {
                    if(firmName.getText().toString().isEmpty()){
                        firmName.requestFocus();
                        Snackbar.make(view, "Enter Name", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else if (Address.getText().toString().isEmpty()) {
                        Address.requestFocus();
                        Snackbar.make(view, "Enter Address", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else if (city.getText().toString().isEmpty()) {
                        Address.requestFocus();
                        Snackbar.make(view, "Enter City", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else if (pincode.getText().toString().isEmpty()) {
                        Address.requestFocus();
                        Snackbar.make(view, "Enter PinCode", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else if (phoneNumberPri.getText().toString().isEmpty()) {
                        phoneNumberPri.requestFocus();
                        Snackbar.make(view, "Enter Primary Phone Number", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else if (tin.getText().toString().isEmpty()) {
                        tin.requestFocus();
                        Snackbar.make(view, "Enter tin", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else if (cust_type.getSelectedItemPosition() == 0) {
                        cust_type.requestFocus();
                        Snackbar.make(view, "Enter CustomerInfo type", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
                else {

                    String phNumSec = "-";
                    if(phoneNumberSec.getText().toString().isEmpty() == false)
                    {
                        phNumSec = phoneNumberSec.getText().toString();
                    }

                    String str = firmName.getText().toString() + "?" +
                            Address.getText().toString() + "?" +
                            city.getText().toString() + "?" +
                            pincode.getText().toString() + "?" +
                            phoneNumberPri.getText().toString() + "?" +
                            phNumSec + "?" +
                            tin.getText().toString() + "?" +
                            Integer.toString(cust_type.getSelectedItemPosition());


                    //custTable[custCount] = str;
                    custTable.add(str);
                    //custCount = custCount +1;
                    isCustTableUpdated = true;

                    AlertDialog.Builder photobuilder = new AlertDialog.Builder(NewCustomer.this);
                    photobuilder.setTitle(firmName.getText().toString() + " added");

                    photobuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface i, int j) {
                        }
                    });

                    AlertDialog dialog = photobuilder.create();
                    dialog.show();

                    firmName.setText("");
                    Address.setText("");
                    phoneNumberPri.setText("");
                    phoneNumberSec.setText("");
                    tin.setText("");
                    cust_type.setSelection(0);
                    city.setText("");
                    pincode.setText("");
                    firmName.requestFocus();

                    DisplayCustTable("");

                }

            }
        });

        Button cancel = (Button) findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firmName = (EditText) findViewById(R.id.FirmName);
                EditText Address = (EditText) findViewById(R.id.Address);
                EditText city = (EditText) findViewById(R.id.City);
                EditText pincode = (EditText) findViewById(R.id.PinCode);
                EditText tin = (EditText) findViewById(R.id.TIN);
                EditText phoneNumberPri = (EditText) findViewById(R.id.PhNumPri);
                EditText phoneNumberSec = (EditText) findViewById(R.id.PhNumSec);

                Spinner cust_type = (Spinner) findViewById(R.id.spinner);

                firmName.setText("");
                Address.setText("");
                phoneNumberPri.setText("");
                phoneNumberSec.setText("");
                tin.setText("");
                city.setText("");
                pincode.setText("");
                firmName.requestFocus();
                cust_type.setSelection(0);

                DisplayCustTable("");

            }
        });

        EditText firmName = (EditText) findViewById(R.id.FirmName);
        firmName.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                EditText firmName = (EditText) findViewById(R.id.FirmName);

                DisplayCustTable(firmName.getText().toString());
                return false;
            }
        });

        Button delete = (Button) findViewById(R.id.DeleteItem);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displayedIndex != 201) {
                    //custTable[displayedIndex] = "";
                    custTable.remove(displayedIndex);
                    isCustTableUpdated = true;


                    EditText firmName = (EditText) findViewById(R.id.FirmName);
                    EditText Address = (EditText) findViewById(R.id.Address);
                    EditText city = (EditText) findViewById(R.id.City);
                    EditText pincode = (EditText) findViewById(R.id.PinCode);
                    EditText tin = (EditText) findViewById(R.id.TIN);
                    EditText phoneNumberPri = (EditText) findViewById(R.id.PhNumPri);
                    EditText phoneNumberSec = (EditText) findViewById(R.id.PhNumSec);

                    Spinner cust_type = (Spinner) findViewById(R.id.spinner);

                    firmName.setText("");
                    Address.setText("");
                    phoneNumberPri.setText("");
                    phoneNumberSec.setText("");
                    tin.setText("");
                    cust_type.setSelection(0);
                    city.setText("");
                    pincode.setText("");
                    firmName.requestFocus();

                    DisplayCustTable("");

                }

            }
        });
    }


    public void onPause() {

        writeCustTableToFile();
        super.onPause();
    }


}

