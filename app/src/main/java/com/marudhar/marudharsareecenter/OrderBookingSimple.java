package com.marudhar.marudharsareecenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class OrderBookingSimple extends AppCompatActivity {

    private List<String> listItemName;
    private List<String> listOrigItemName;
    private List<String> listItemPrice;
    private List<String> listItemQty;

    private static List<String> cartItem;
    private static List<String> cartPrice;
    private static List<String> cartQty;
    private static List<String> cartReview;
    int customerRating;
    String customerName;
    private static Context contxt;
    private static TableLayout orderBookingSimpletbl;


    private void init()
    {
        listItemName = new ArrayList<String>();
        listItemPrice = new ArrayList<String>();
        listItemQty = new ArrayList<String>();
        listOrigItemName = new ArrayList<String>();

        cartItem = new ArrayList<String>();
        cartPrice = new ArrayList<String>();
        cartQty = new ArrayList<String>();
        cartReview = new ArrayList<String>();
    }
    private void readDesignFile()
    {
        File directory = new File(GlobalVar.InventoryPath);
        File inventoryData = new File(directory, GlobalVar.inventoryDataFile);

        if (inventoryData.exists()) {
            try {
                FileReader fd = new FileReader(inventoryData);

                BufferedReader inReader = new BufferedReader(fd);
                try {
                    String fileStr = inReader.readLine();
                    while (fileStr != null) {
                        StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                        String name = tokens.nextToken();
                        String price = tokens.nextToken();
                        String qty = tokens.nextToken();

                        String designRating = tokens.nextToken();
                        int inDesignRating = Integer.parseInt(designRating);
                        if(inDesignRating >= customerRating && Integer.parseInt(qty) != 0)
                        {
                            listItemName.add(name);
                            listOrigItemName.add(name);
                            listItemPrice.add(price);
                            listItemQty.add(qty);
                        }
                        fileStr = inReader.readLine();
                    }
                } catch (IOException e) {
                }
                try {
                    inReader.close();
                } catch (IOException e) {
                }
            } catch (FileNotFoundException e) {
            }
        }
    }

    public void fillTable(Context contxt, TableLayout tbl, String name, String quantity, String price, String remark) {

        TableRow temp = new TableRow(this);

        TextView itemName = new TextView(this);
        TextView itemPrice = new EditText(this);
        TextView itemQuantity = new EditText(this);
        TextView itemRemark = new TextView(this);

        int pad = 9;


        itemName.setText(name);
        itemName.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        itemName.setTextSize(22);
        itemName.setPadding(pad, pad+1, pad, pad+8);

        itemQuantity.setText(quantity);
        itemQuantity.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        itemQuantity.setTextSize(22);
        itemQuantity.setPadding(pad, pad+1, pad, pad+8);



        itemPrice.setText(price);
        itemPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        itemPrice.setTextSize(22);
        itemPrice.setPadding(pad, pad+1, pad, pad+8);


        itemRemark.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        itemRemark.setTextSize(22);
        itemRemark.setPadding(pad, pad+1, pad, pad+8);

        if(remark != null && !(remark.isEmpty()) && (remark.contentEquals(" ") == false)) {
            itemRemark.setText("Click for details");
            itemRemark.setTag(R.id.KEY_1, remark);

            itemRemark.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Context contxt = v.getContext();
                        String remark = (String) v.getTag(R.id.KEY_1);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(contxt);

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

        temp.addView(itemName);
        //temp.addView(itemPrice);
        temp.addView(itemQuantity);
        temp.addView(itemPrice);
        temp.addView(itemRemark);
        tbl.addView(temp);
        tbl.setShrinkAllColumns(true);

    }

    private int getIndex(List<String> inItemName, String name)
    {

        for(int index = 0; index < inItemName.size(); index++)
        {
            if((inItemName.get(index).contentEquals(name)))
            {
                return index;
            }
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_booking_simple);

        contxt = this.getApplicationContext();

        orderBookingSimpletbl = (TableLayout)findViewById(R.id.OrderBookingSimpleTableLayOut);

        Intent myIntent = this.getIntent();
        customerName = myIntent.getStringExtra("Customer Name");
        customerRating = Integer.parseInt(myIntent.getStringExtra("Customer Rating"));

        EditText custName = (EditText)findViewById(R.id.OrderBookingSimpleCustomerName);
        custName.setTextSize(22);
        custName.setText(customerName);
        custName.setEnabled(false);

        init();

        readDesignFile();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listItemName) {
            @Override
            public Filter getFilter()
            {
                Filter nameFilter = new Filter() {

                    private ArrayList<String> OrigItemName;
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {


                        if(OrigItemName == null)
                        {
                            OrigItemName = new ArrayList<String>(listItemName);
                        }
                        List<String>values = new ArrayList<String>(OrigItemName);

                        final FilterResults results = new FilterResults();

                        final int count = values.size();
                        final ArrayList<String> newValues = new ArrayList<>();

                        for (int i = 0; i < count; i++) {
                            final String value = values.get(i);

                            final String valueText = value.toLowerCase();

                            // First match against the whole, non-splitted value
                            if (valueText.contains(constraint)) {
                                newValues.add(value);
                            } else {
                                final String[] words = valueText.split(" ");
                                for (String word : words) {
                                    if (word.contains(constraint)) {
                                        newValues.add(value);
                                        break;
                                    }
                                }
                            }
                        }

                        results.values = newValues;
                        results.count = newValues.size();

                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {

                        if(results != null && results.count > 1)
                        {
                            List<String> tmp = (ArrayList<String>)results.values;
                            clear();

                            for(String str:tmp)
                            {
                                add(str);
                                notifyDataSetChanged();
                            }
                        }

                    }
                };

                return nameFilter;
            }
        };
        final AutoCompleteTextView itemName = (AutoCompleteTextView)
                findViewById(R.id.OrderBookSimpleItemName);
        itemName.setAdapter(adapter);
        itemName.setThreshold(1);

        itemName.setTag(listOrigItemName);

        final EditText itemPrice = (EditText)
                findViewById(R.id.OrderBookSimpleItemPrice);
        itemPrice.setTag(listItemPrice);

        final EditText itemQty = (EditText)
                findViewById(R.id.OrderBookSimpleItemQty);

        final EditText itemRemark = (EditText)
                findViewById(R.id.OrderBookSimpleItemRemark);

        final Button addItem = (Button)findViewById(R.id.orderBookingSimpleAdd);

        itemQty.setTag(listItemQty);

        itemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Item pos", Integer.toString(position));

                List<String> inItemPrice = (List<String>)itemPrice.getTag();
                List<String> inItemQty = (List<String>)itemQty.getTag();
                List<String> inItemName = (List<String>)itemName.getTag();

                String name = itemName.getText().toString();

                int index = getIndex(inItemName, name);
                if(index != -1) {
                    itemPrice.setText(inItemPrice.get(index));
                    itemQty.setText("0");
                    addItem.setTag(inItemQty.get(index));
                }
            }
        });

        addItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                String name = itemName.getText().toString();
                String price = itemPrice.getText().toString();
                String qty = itemQty.getText().toString();
                String remark = itemRemark.getText().toString();

                List<String> inItemPrice = (List<String>)itemPrice.getTag();
                List<String> inItemQty = (List<String>)itemQty.getTag();
                List<String> inItemName = (List<String>)itemName.getTag();

                String maxQty = (String)v.getTag();

                int index = getIndex(inItemName, name);

                if(index != -1 && qty != null && !(qty.isEmpty()) &&
                   maxQty != null && !(maxQty.isEmpty()))
                {
                    if(Integer.parseInt(qty) > Integer.parseInt(maxQty))
                    {
                        String displayText = "Max Quantity exceeded" + "(" + qty + ">" + maxQty + ")" + "Please select lower value";
                        Toast display = Toast.makeText(contxt, displayText, Toast.LENGTH_SHORT);
                        display.setGravity(Gravity.CENTER, 0, 0);
                        display.show();

                    }
                    else
                    {
                        itemPrice.setText("");
                        itemQty.setText("");
                        itemName.setText("");
                        itemRemark.setText("");
                        itemName.requestFocus();

                        cartItem.add(name);
                        cartPrice.add(price);
                        cartQty.add(qty);
                        cartReview.add(remark);

                        String displayText = name + " added to cart";
                        Toast display = Toast.makeText(contxt, displayText, Toast.LENGTH_SHORT);
                        display.setGravity(Gravity.CENTER, 0, 0);
                        display.show();

                        fillTable(contxt, orderBookingSimpletbl, name, price, qty, remark);

                        int currentQty = Integer.parseInt((String)inItemQty.get(index));

                        int selectQty = Integer.parseInt(qty);

                        String newQty = Integer.toString(currentQty - selectQty);

                        inItemQty.set(index, newQty);


                    }
                }

                return false;
            }
        });

        final Button next = (Button)findViewById(R.id.orderBookingSimpleNext);

        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(cartItem.size() > 0) {
                    Intent orderScroll = new Intent(contxt, OrderScroll.class);

                    String [] name = cartItem.toArray(new String[cartItem.size()]);
                    String [] qty = cartQty.toArray(new String[cartQty.size()]);
                    String [] price = cartPrice.toArray(new String[cartPrice.size()]);
                    String [] remark = cartReview.toArray(new String[cartReview.size()]);

                    orderScroll.putExtra("ItemName", name);
                    orderScroll.putExtra("ItemQty", qty);
                    orderScroll.putExtra("ItemPrice", price);
                    orderScroll.putExtra("ItemRemark", remark);
                    orderScroll.putExtra("count", cartItem.size());
                    orderScroll.putExtra("Customer Name", customerName);
                    startActivity(orderScroll);
                }

                return false;
            }
        });


    }
}
