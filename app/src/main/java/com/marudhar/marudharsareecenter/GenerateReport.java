package com.marudhar.marudharsareecenter;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class GenerateReport extends AppCompatActivity {

    private class CustomerOrder{
        private String customerName;
        private List<String> itemName;
        private List<Integer> itemQty;

        CustomerOrder(String inCustomerName)
        {
            itemName = new ArrayList<String>();
            itemQty = new ArrayList<Integer>();
            customerName = inCustomerName;
        }

        public String getCustomerName()
        {
            return this.customerName;
        }

        public int getItemSize()
        {
            return this.itemName.size();
        }

        public String getItemNameAtIndex(int index)
        {
            return this.itemName.get(index);
        }

        public int getItemQtyAtIndex(int index)
        {
            return this.itemQty.get(index);
        }


        public void addItem(String inItemName, String inItemQty)
        {
            int qty = Integer.parseInt(inItemQty);
            itemName.add(inItemName);
            itemQty.add(qty);

        }
    }

    private int getCutomerRecord(List<CustomerOrder> customerRecord, String customerName)
    {
        for(int index = 0 ; index < customerRecord.size(); index++)
        {
            if(customerRecord.get(index).getCustomerName().contentEquals(customerName))
            {
                return index;
            }
        }
        return -1;
    }

    private void createCustomerList(List<CustomerOrder> customerRecord, List<String> orderCustomerSortedList)
    {
        String previousCustomer = " ";
        for(int index = 0 ; index < orderCustomerSortedList.size(); index++)
        {
            String line = orderCustomerSortedList.get(index);
            StringTokenizer fields = new StringTokenizer(line, "?");
            String customerName = fields.nextToken();
            String itemName = fields.nextToken();
            String itemQty = fields.nextToken();
            if(!(customerName.contentEquals(previousCustomer)))
            {
                CustomerOrder record = new CustomerOrder(customerName);
                customerRecord.add(record);
                previousCustomer = customerName;
            }

            int custIndex = getCutomerRecord(customerRecord, customerName);

            CustomerOrder currentCustomerRecord = customerRecord.get(custIndex);
            currentCustomerRecord.addItem(itemName, itemQty);

        }
    }

    private void createDesignList(List<CustomerOrder> designRecord, List<String> orderSortedList)
    {
        String previousDesign = " ";
        for(int index = 0 ; index < orderSortedList.size(); index++)
        {
            String line = orderSortedList.get(index);
            StringTokenizer fields = new StringTokenizer(line, "?");
            String itemName = fields.nextToken();
            String itemQty = fields.nextToken();
            String customerName = "";
            while(fields.hasMoreTokens())
            {
                customerName = fields.nextToken();
            }
            if(!(itemName.contentEquals(previousDesign)))
            {
                CustomerOrder record = new CustomerOrder(itemName);
                designRecord.add(record);
                previousDesign = itemName;
            }

            int custIndex = getCutomerRecord(designRecord, itemName);

            CustomerOrder currentCustomerRecord = designRecord.get(custIndex);
            currentCustomerRecord.addItem(customerName, itemQty);

        }
    }

    void clearLayout(LinearLayout generateReportLayout)
    {
        for(int i = 0; i< generateReportLayout.getChildCount(); i++)
        {
            View v = generateReportLayout.getChildAt(i);
            if(v.getId() != R.id.generateReportOptionSpinner)
            {
                generateReportLayout.removeViewAt(i);
                i = 0;
            }
        }
    }

    void fillTable(int recordType, LinearLayout generateReportLayout, Context generateReportContext, List<CustomerOrder> inRecord)
    {
        for (int recordIndex = 0; recordIndex < inRecord.size(); recordIndex++)
        {
            //generateReportLayout.setDividerPadding(50);
            CustomerOrder record = inRecord.get(recordIndex);
            TableLayout tbl = new TableLayout(generateReportContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0,30);
            tbl.setLayoutParams(layoutParams);

            tbl.setColumnStretchable(0,true);


            String customerName = record.getCustomerName();

            TextView custName = new TextView(generateReportContext);

            custName.setText(customerName);
            custName.setTextSize(22);
            custName.setTextColor(Color.BLACK);
            //custName.set
            //custName.set
            generateReportLayout.addView(custName);

            TableRow tblRowHeader = new TableRow(generateReportContext);


            TextView tmp1 = new TextView(generateReportContext);
            TextView tmp2 = new TextView(generateReportContext);

            tmp1.setBackgroundResource(R.drawable.cell_shape);
            tmp2.setBackgroundResource(R.drawable.cell_shape);

            tmp1.setPadding(15,15,15,15);
            tmp2.setPadding(15,15,15,15);

            if(recordType == 0) {
                tmp1.setText("Design Name");
            }
            else
            {
                tmp1.setText("Customer Name");
            }
            tmp1.setTextColor(Color.BLACK);
            tmp2.setText("Quantity");
            tmp2.setTextColor(Color.BLACK);
            tblRowHeader.addView(tmp1);
            tblRowHeader.addView(tmp2);
            if(tblRowHeader.getChildCount() > 0)
            {
                tbl.addView(tblRowHeader);
            }


            for(int itemIndex = 0; itemIndex < record.getItemSize(); itemIndex++)
            {
                String itemName = record.getItemNameAtIndex(itemIndex);
                int itemQty = record.getItemQtyAtIndex(itemIndex);

                TableRow tblRow = new TableRow(generateReportContext);


                TextView tv1 = new TextView(generateReportContext);
                TextView tv2 = new TextView(generateReportContext);

                tv1.setBackgroundResource(R.drawable.cell_shape);
                tv2.setBackgroundResource(R.drawable.cell_shape);

                tv1.setPadding(15,15,15,15);
                tv2.setPadding(15,15,15,15);

                tv1.setText(itemName);
                tv1.setTextColor(Color.BLACK);
                tv2.setText(Integer.toString(itemQty));
                tv2.setTextColor(Color.BLACK);
                tblRow.addView(tv1);
                tblRow.addView(tv2);
                if(tblRow.getChildCount() > 0)
                {
                    tbl.addView(tblRow);
                }
                Log.e("GenerateReport", inRecord.size() + " " + record.getItemSize() + " " + customerName + " " + itemName + " " + itemQty);
            }

            if(tbl.getChildCount() > 0)
            {

                generateReportLayout.addView(tbl);
            }
        }
    }

    private void generateAndFillReport(List<CustomerOrder> customerRecord, List<CustomerOrder> designRecord, Context context)
    {
        String [] orderFileName;
        ContextWrapper contextWrapper = new ContextWrapper(context);
        //File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.CustomerOrderDetails);

        FilenameFilter tmp = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.startsWith("completed");
            }
        };

        if(directory.isDirectory()) {
            orderFileName = directory.list(tmp);

            if( orderFileName.length > 0) {


                try {

                    List<String> orderCustomerSortedList = new ArrayList<String>();
                    List<String> orderItemSorted = new ArrayList<String>();


                    for(int i = 0; i < orderFileName.length; i++)
                    {
                        StringTokenizer fields = new StringTokenizer(orderFileName[i], "_");
                        fields.nextToken();
                        String customerName = fields.nextToken();

                        File orderFile = new File(directory , orderFileName[i]);

                        if(orderFile.exists()) {
                            FileReader fd = new FileReader(orderFile);
                            BufferedReader inReader = new BufferedReader(fd);
                            String fileStr = inReader.readLine();
                            while(fileStr != null)
                            {
                                orderCustomerSortedList.add(customerName + "?" + fileStr);
                                orderItemSorted.add(fileStr + "?" + customerName);
                                fileStr = inReader.readLine();
                            }
                        }

                    }
                    Collections.sort(orderCustomerSortedList, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });

                    Collections.sort(orderItemSorted, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });

                    createCustomerList(customerRecord, orderCustomerSortedList);
                    createDesignList(designRecord, orderItemSorted);



                } catch (Exception e) { }
            }
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<CustomerOrder> customerRecord = new ArrayList<CustomerOrder>();
        List<CustomerOrder> designRecord = new ArrayList<CustomerOrder>();

        LinearLayout generateReportParent = (LinearLayout)findViewById(R.id.generateReportParent);

        Spinner generateReportOption = (Spinner)findViewById(R.id.generateReportOptionSpinner);

        generateAndFillReport(customerRecord, designRecord, this.getApplicationContext());

        generateReportOption.setTag(R.id.KEY_1, generateReportParent);
        generateReportOption.setTag(R.id.KEY_2, this.getApplicationContext());
        generateReportOption.setTag(R.id.KEY_3, customerRecord);
        generateReportOption.setTag(R.id.KEY_4, designRecord);

        generateReportOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout generateReportLayout = (LinearLayout)parent.getTag(R.id.KEY_1);
                Context generateReportContext = (Context)parent.getTag(R.id.KEY_2);


                if (position == 0)
                {
                    clearLayout(generateReportLayout);
                }
                else if(position == 1)
                {
                    List<CustomerOrder> customerRecord = (List<CustomerOrder>)parent.getTag(R.id.KEY_3);
                    clearLayout(generateReportLayout);
                    fillTable(0, generateReportLayout, generateReportContext, customerRecord);

                }
                else if(position == 2)
                {
                    List<CustomerOrder> designRecord = (List<CustomerOrder>)parent.getTag(R.id.KEY_4);
                    clearLayout(generateReportLayout);
                    fillTable(1, generateReportLayout, generateReportContext, designRecord);
                }

                //TableLayout table = new TableLayout(generateReportContext);
                //TableRow tableRow = new TableRow(generateReportContext);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
