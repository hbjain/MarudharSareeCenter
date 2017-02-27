package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.StringTokenizer;

public class ViewOrderForm extends AppCompatActivity {

    CheckBox.OnCheckedChangeListener completedListner;
    CheckBox.OnCheckedChangeListener cancelledListner;
    String [] orderList;
    int orderItemCount = 0;
    boolean isOrderListUpdated = false;
    String orderFileName = "";
    TableLayout orderDetail;
    //Boolean hideUpdateButton;

    private void fillTable(int index, TableLayout orderDetailTable, String inDesignName, String inQuantity, String inPrice, String inRemark, String inCompleted, String inCancelled)
    {
        TableRow temp = new TableRow(this);

        TextView designName = new TextView(this);
        TextView quantity = new TextView(this);
        TextView price = new TextView(this);
        TextView remark = new TextView(this);
        CheckBox completed = new CheckBox(this);
        CheckBox cancelled = new CheckBox(this);


        designName.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        quantity.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        price.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        remark.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        completed.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));
        cancelled.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cell_shape));

        designName.setText(inDesignName);
        designName.setPadding(5,5,5,5);
        designName.setTextSize(18);

        quantity.setText(inQuantity);
        quantity.setPadding(5,5,5,5);
        quantity.setTextSize(18);

        price.setText(inPrice);
        price.setPadding(5,5,5,5);
        price.setTextSize(18);

        remark.setText(inRemark);
        remark.setPadding(5,5,5,5);
        remark.setTextSize(18);

        if(inRemark != null && !(inRemark.isEmpty()) && (inRemark.contentEquals(" ") == false)) {
            remark.setText("Click for details");
            remark.setTag(R.id.KEY_1, inRemark);

            remark.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Context contxt = v.getContext();
                        String remark = (String) v.getTag(R.id.KEY_1);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(contxt);

                        remark = remark.replace("]", "\n");
                        dialogBuilder.setTitle("Remarks");
                        dialogBuilder.setMessage(remark);

                        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });

                        //Create alert dialog object via builder
                        AlertDialog alertDialogObject = dialogBuilder.create();


                        //Show the dialog
                        alertDialogObject.show();

                    }
                    return false;
                }
            });
        }

        completed.setPadding(5,5,5,5);
        completed.setTextSize(18);
        cancelled.setPadding(5,5,5,5);
        cancelled.setTextSize(18);


        if(inCompleted.matches("YES"))
        {
            completed.setChecked(true);
            completed.setEnabled(false);
            cancelled.setEnabled(false);

        }
        else
        {
            completed.setChecked(false);
        }

        if(inCancelled.matches("YES"))
        {
            cancelled.setChecked(true);
            completed.setEnabled(false);
            cancelled.setEnabled(false);
        }

        cancelled.setTag(R.id.KEY_1, index);
        completed.setTag(R.id.KEY_1, index);

        cancelled.setTag(R.id.KEY_2, completed);
        completed.setTag(R.id.KEY_2, cancelled);

        //completed.setTag(index);
        cancelled.setOnCheckedChangeListener(cancelledListner);
        completed.setOnCheckedChangeListener(completedListner);

        temp.addView(designName);
        temp.addView(quantity);
        temp.addView(price);
        temp.addView(remark);
        temp.addView(completed);
        temp.addView(cancelled);

        orderDetailTable.addView(temp);
        orderDetailTable.setShrinkAllColumns(true);
    }


    private void displayOrderForm(TableLayout orderDetail, String fileName)
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.CustomerOrderDetails);
        File orderFile = new File(directory , fileName);

        if(orderFile.exists())
        {
            try {
                orderItemCount = 0;
                FileReader fd = new FileReader(orderFile);
                BufferedReader inReader = new BufferedReader(fd);
                String fileStr = inReader.readLine();
                //fileStr = inReader.readLine();
                while(fileStr != null)
                {
                    StringTokenizer tmp = new StringTokenizer(fileStr, "?");
                    String designName = tmp.nextToken();
                    String quantity = tmp.nextToken();
                    String price = tmp.nextToken();
                    String remark = tmp.nextToken();
                    String completed = tmp.nextToken();
                    String cancelled = tmp.nextToken();
                    fillTable(orderItemCount, orderDetail, designName, quantity, price, remark, completed, cancelled);
                    orderList[orderItemCount] = fileStr;
                    orderItemCount = orderItemCount + 1;
                    fileStr = inReader.readLine();
                }
            }catch(Exception e){}
        }
        else
        {
            finish();
        }
    }

    private boolean checkIfOrderIsCompleted()
    {
        int nrOfTickedItem = 0;
        for(int i = 0; i< orderItemCount; i++) {
            StringTokenizer tmp = new StringTokenizer(orderList[i], "?");

            //String [] field
            String dummy = tmp.nextToken();
            dummy = tmp.nextToken();
            dummy = tmp.nextToken();
            dummy = tmp.nextToken();

            String completed = tmp.nextToken();
            String cancelled = tmp.nextToken();

            if(completed.contentEquals("YES") || cancelled.contentEquals("YES"))
            {
                nrOfTickedItem++;
            }
        }
        return (nrOfTickedItem == orderItemCount);
    }

    private void updateOrderFile(String orderFileName){

        if(isOrderListUpdated == true) {
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            //File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

            File directory = new File(GlobalVar.CustomerOrderDetails);
            File orderFile = new File(directory, orderFileName);

            boolean isOrderCompleted = checkIfOrderIsCompleted();

            try
            {
                orderFile.delete();

                if (isOrderCompleted)
                {
                    orderFileName = "completed" + orderFileName;
                    orderFile = new File(directory, orderFileName);
                }
                orderFile.createNewFile();

                FileOutputStream fileWriteStream = new FileOutputStream(orderFile, true);
                for(int i = 0; i < orderItemCount; i++)
                {
                    String str = orderList[i] + "\n";
                    fileWriteStream.write(str.getBytes());
                }
                fileWriteStream.close();
            }catch(Exception e){}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_form);

        orderDetail = (TableLayout)findViewById(R.id.orderDetailTable);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent myIntent = this.getIntent();
        orderFileName = myIntent.getStringExtra("FileName");

        StringTokenizer fields = new StringTokenizer(orderFileName, "_");
        if(fields.countTokens() > 1) {
            String tmp = fields.nextToken();
            String custName = fields.nextToken();
            TextView orderFormCustName = (TextView)findViewById(R.id.OrderFormCustomerName);
            orderFormCustName.setText(custName);
        }

        orderList = new String[50];

        cancelledListner = new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CheckBox cancelBox = (CheckBox)buttonView;

                int index = (int)cancelBox.getTag(R.id.KEY_1);
                CheckBox completedBox = (CheckBox)cancelBox.getTag(R.id.KEY_2);
                isOrderListUpdated = true;

                StringTokenizer str = new StringTokenizer(orderList[index],"?");
                String []tmp = new String[str.countTokens()];

                int i = 0;
                while(str.hasMoreTokens())
                {
                    tmp[i] = str.nextToken();
                    i++;
                }

                if(isChecked) {
                    tmp[i - 1] = "YES";
                    completedBox.setEnabled(false);
                }
                else
                {
                     tmp[i - 1] = "NO";
                    completedBox.setEnabled(true);
                }

                orderList[index] ="";
                for(int tmpIndex = 0; tmpIndex < i; tmpIndex++)
                {
                    if(tmpIndex == i - 1)
                    {
                        orderList[index] = orderList[index] + tmp[tmpIndex];
                    }
                    else {
                        orderList[index] = orderList[index] + tmp[tmpIndex] + "?";
                    }
                }
            }
        };

        completedListner = new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CheckBox completedBox = (CheckBox)buttonView;

                int index = (int)completedBox.getTag(R.id.KEY_1);
                CheckBox cancelledBox = (CheckBox)completedBox.getTag(R.id.KEY_2);
                isOrderListUpdated = true;

                StringTokenizer str = new StringTokenizer(orderList[index], "?");
                String []tmp = new String[str.countTokens()];

                int i = 0;
                while(str.hasMoreTokens())
                {
                    tmp[i] = str.nextToken();
                    i++;
                }

                if(isChecked) {
                    tmp[i - 2] = "YES";
                    cancelledBox.setEnabled(false);

                }
                else
                {
                    tmp[i - 2] = "NO";
                    cancelledBox.setEnabled(true);
                }
                orderList[index] = "";
                for(int tmpIndex = 0; tmpIndex < i; tmpIndex++)
                {
                    if(tmpIndex == i - 1)
                    {
                        orderList[index] = orderList[index] + tmp[tmpIndex];
                    }
                    else {
                        orderList[index] = orderList[index] + tmp[tmpIndex] + "?";
                    }
                }
            }
        };


        Button updateButton = (Button)findViewById(R.id.OrderFormUpdate);
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                updateOrderFile(orderFileName);
                finish();

            }
        });

        if(orderFileName.startsWith("completed"))
        {
            updateButton.setVisibility(View.INVISIBLE);
        }

        displayOrderForm(orderDetail, orderFileName);


    }
    protected void onPause()
    {
        //Intent myIntent = this.getIntent();
        //String orderFileName = myIntent.getStringExtra("FileName");
        super.onPause();
    }
}
