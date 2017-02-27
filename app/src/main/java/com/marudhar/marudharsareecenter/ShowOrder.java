package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class ShowOrder extends AppCompatActivity {

    private AdapterView.OnItemSelectedListener spinnerTouchListner;
    private TableLayout custOrderTbl;
    private static Context contxt;
    private String phoneNumber = "";
    private String [] inventotyFileData;
    private int inventoryCount;


    private String getCustomerDetails(String customerName)
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.CustomerInfo, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.CustomerInfo);
        File cust_file = new File(directory , GlobalVar.customer_details);
        String [] fields = new String[8];
        if(cust_file.exists())
        {
            try
            {
                FileReader fd = new FileReader(cust_file);
                BufferedReader inReader = new BufferedReader(fd);
                String fileStr = inReader.readLine();
                while(fileStr != null)
                {
                    StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                    int index = 0;
                    while (tokens.hasMoreTokens()) {
                        fields[index] = tokens.nextToken();
                        index++;
                    }

                    if(fields[0].contentEquals(customerName))
                    {

                        String customerDetails = fields[0] + "?" +
                                                 fields[1] + "?" +
                                                 fields[2] + "-" + fields[3];



                        phoneNumber = fields[4];
                        inReader.close();
                        return customerDetails;

                    }

                    fileStr = inReader.readLine();
                }
            }catch(Exception e){}

        }
        return "";
    }


    public Bitmap takeScreenshot1() {
        ScrollView rootView = (ScrollView)findViewById(R.id.ScrollOrderConfirm);
        RelativeLayout v = (RelativeLayout)rootView.getChildAt(0);
        EditText e = (EditText)v.getChildAt(2);

        e.setCursorVisible(false);
        e.buildDrawingCache();
        Bitmap b = Bitmap.createBitmap(e.getDrawingCache());

        //Bitmap b = Bitmap.createBitmap(e.getWidth() , 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        e.draw(c);
        return b;
    }

    public Bitmap takeScreenshot() {

        Date d = new Date();
        CharSequence s  = DateFormat.format("EEEE, MMMM d, yyyy hh:mm a", d.getTime());

        ScrollView tmp = (ScrollView)findViewById(R.id.ScrollOrderConfirm);
        RelativeLayout v = (RelativeLayout)tmp.getChildAt(0);

        Button OrderConfirm = (Button)findViewById(R.id.orderConfrim);
        OrderConfirm.setVisibility(View.INVISIBLE);
        v.getChildAt(2).setVisibility(View.INVISIBLE);

        int count = v.getChildCount();
        int height = 0;
        for (int i = 0; i < count; i++) {
            View view = v.getChildAt(i);
            height += view.getHeight();
        }

        Bitmap b = changeColor(Bitmap.createBitmap(v.getWidth() , v.getHeight(), Bitmap.Config.ARGB_8888));

        Canvas c = new Canvas(b);



        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.BLACK);
        // text size in pixels
        paint.setTextSize(30);
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);


        v.draw(c);


        StringTokenizer customerDetails = new StringTokenizer(getCustomerDetails(((EditText)v.getChildAt(2)).getText().toString()), "?");

        c.drawText(customerDetails.nextToken(), 15, 30, paint);
        customerDetails.nextToken();
        c.drawText(customerDetails.nextToken(), 15, 70, paint);
        c.drawText(s.toString(), 17, 110, paint);
        return b;
    }

    public Bitmap changeColor(Bitmap myBitmap)
    {
        int [] allpixels = new int [myBitmap.getHeight()*myBitmap.getWidth()];

        myBitmap.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());

        for(int i = 0; i < allpixels.length; i++)
        {
            if(allpixels[i] == Color.TRANSPARENT)
            {
                allpixels[i] = Color.WHITE;

            }
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }

    public void saveBitmap(Bitmap bitmap, File imagePath) {

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public void fillTable(Context contxt, TableLayout tbl, String name, String quantity, String price, String remark, int index) {

        TextView layoutCopy = (TextView)findViewById(R.id.showOrderDesignImage);
        TableRow temp = new TableRow(this);

        TextView itemName = new TextView(this);
        EditText itemPrice = new EditText(this);
        EditText itemQuantity = new EditText(this);
        TextView itemRemark = new TextView(this);
        Spinner updateDelete = new Spinner(this);

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPhoto, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPhoto);
        String picturePath = directory.toString() + "/" + name + "1";
        File imageFile = new File(directory, name + "1");

        int pad = 9;

        final List<String> list=new ArrayList<String>();
        list.add("-");
        list.add("Update");
        list.add("Remove");

        itemName.setText(name);
        itemName.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        itemName.setTextSize(22);
        itemName.setPadding(pad, pad+1, pad, pad+8);

        itemQuantity.setText(quantity);
        itemQuantity.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        itemQuantity.setTextSize(22);
        itemQuantity.setPadding(pad, pad+1, pad, pad+8);
        itemQuantity.setSingleLine(true);
        //itemQuantity.setEnabled(false);
        itemQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        itemQuantity.setCursorVisible(false);
        itemQuantity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((EditText)v).setCursorVisible(true);
            }
        });


        itemPrice.setText(price);
        itemPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        itemPrice.setTextSize(22);
        itemPrice.setPadding(pad, pad+1, pad, pad+8);

        itemPrice.setSingleLine(true);
        //itemQuantity.setEnabled(false);
        itemPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        itemPrice.setCursorVisible(false);
        itemPrice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((EditText)v).setCursorVisible(true);
            }
        });

        ArrayAdapter<String> adp1=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                // do whatever you want with this text view
                textView.setTextSize(4);
                return view;
            }
        };


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


        updateDelete.setAdapter(adp1);
        updateDelete.setPadding(0,9,0,0);
        updateDelete.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        updateDelete.setOnItemSelectedListener(spinnerTouchListner);
        updateDelete.setTag(R.id.KEY_1,temp);
        updateDelete.setTag(R.id.KEY_2,index);
        updateDelete.setTag(R.id.KEY_3, itemQuantity);
        updateDelete.setTag(R.id.KEY_4, itemName);

        temp.addView(itemName);
        //temp.addView(itemPrice);
        temp.addView(itemQuantity);
        temp.addView(itemPrice);
        temp.addView(itemRemark);
        temp.addView(updateDelete);
        tbl.addView(temp);
        tbl.setShrinkAllColumns(true);

    }

    private void readDesignFile()
    {
        inventotyFileData = new String[200];
        inventoryCount = 0;
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPath, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPath);
        File inventoryData = new File(directory, GlobalVar.inventoryDataFile);


        if (inventoryData.exists()) {
            try {
                FileReader fd = new FileReader(inventoryData);

                BufferedReader inReader = new BufferedReader(fd);
                try {
                    String fileStr = inReader.readLine();
                    while (fileStr != null) {
                        inventotyFileData[inventoryCount] = fileStr;
                        inventoryCount++;
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

    public void updateInventory(String itemName, String itemQty){

        ContextWrapper contextWrapper = new ContextWrapper(contxt);
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPath, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPath);
        File inventoryData = new File(directory, GlobalVar.inventoryDataFile);

        try {
            File tmpFile = new File(directory, "tmp_design_file.txt");

            if (tmpFile.exists()) {
                tmpFile.delete();
                tmpFile.createNewFile();
                tmpFile.setWritable(true);
            } else {
                tmpFile.createNewFile();
                tmpFile.setWritable(true);
            }

            FileOutputStream designFileStream = new FileOutputStream(tmpFile, true);
            for(int index = 0; index < inventoryCount; index++)
            {
                if(inventotyFileData[index].startsWith(itemName + "?"))
                {
                    StringTokenizer tokens = new StringTokenizer(inventotyFileData[index], "?");
                    int count = tokens.countTokens();
                    String [] tmp = new String[count];
                    for(int i = 0; i < count; i++ )
                    {
                        tmp[i] = tokens.nextToken();
                    }

                    int qty = Integer.parseInt(tmp[2]) - Integer.parseInt(itemQty);
                    tmp[2] = Integer.toString(qty);

                    inventotyFileData[index] = tmp[0];
                    for(int i = 1; i < count; i++ )
                    {
                        inventotyFileData[index] = inventotyFileData[index] + "?" + tmp[i];
                    }
                    inventotyFileData[index] = inventotyFileData[index] + "?";

                }
                String fileStr = inventotyFileData[index] + "\n";
                byte[] by_new = fileStr.getBytes();
                designFileStream.write(by_new);

            }

            designFileStream.close();

            if(tmpFile.renameTo(inventoryData)) {
                inventoryData = new File(directory, GlobalVar.inventoryDataFile);
            }
        } catch (Exception e) {
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ORDER CONFIRM");
        setSupportActionBar(toolbar);

        contxt = this;
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Intent myIntent = getIntent();
        String [] itemName = myIntent.getStringArrayExtra("ItemName");
        final String [] itemQty = myIntent.getStringArrayExtra("ItemQty");
        String [] itemPrice = myIntent.getStringArrayExtra("ItemPrice");
        String [] itemRemark = myIntent.getStringArrayExtra("ItemRemark");
        int count = myIntent.getIntExtra("count", 0);
        final String customerName = myIntent.getStringExtra("Customer Name");

        EditText custName = (EditText)findViewById(R.id.customerName);
        custName.setText(customerName);
        custName.setTextSize(30);
        custName.setEnabled(false);
        //custName.setCursorVisible(false);

        readDesignFile();

        spinnerTouchListner = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

                Spinner sp = (Spinner)parent;
                TableRow tr = (TableRow)sp.getTag(R.id.KEY_1);
                final EditText itemQtyTmp = (EditText)sp.getTag(R.id.KEY_3);
                int index = (int)sp.getTag(R.id.KEY_2);

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if(sp.getSelectedItemPosition() == 1)
                {
                    itemQty[index] = itemQtyTmp.getText().toString();
                    itemQtyTmp.setCursorVisible(false);
                    //itemQty[index] = Integer.toString(0);
                }else if(sp.getSelectedItemPosition() == 2)
                {
                    custOrderTbl.removeView(tr);
                    itemQty[index] = Integer.toString(0);

                    if(custOrderTbl.getChildCount() == 1)
                    {
                        Button orderConfrim = (Button) findViewById(R.id.orderConfrim);
                        navigateUpTo(new Intent(getBaseContext(), Store.class));

                    }
                }
                sp.setSelection(0);
            }

            public void onNothingSelected(AdapterView<?> parent){

            }
        };

        Button orderConfrim = (Button)findViewById(R.id.orderConfrim);
        orderConfrim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                //File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

                File directory = new File(GlobalVar.CustomerOrderDetails);

                Date d = new Date();
                CharSequence s  = DateFormat.format("EEEE, MMMM d, yyyy '_' hh:mm a", d.getTime());
                String str1 = GlobalVar.orderFileName + "_" + customerName + "_" + s + ".txt";

                File orderFile = new File(directory , str1);
                try {
                    if (orderFile.exists()) {
                        orderFile.delete();
                    }

                    orderFile.createNewFile();
                    orderFile.setWritable(true);
                    FileOutputStream orderFileStream = new FileOutputStream(orderFile, true);

                    for (int i = 1; i < custOrderTbl.getChildCount(); i++) {
                        TableRow tmpRow = (TableRow) custOrderTbl.getChildAt(i);
                        TextView itemName = (TextView) tmpRow.getChildAt(0);
                        TextView itemQty = (TextView) tmpRow.getChildAt(1);
                        TextView itemPrice = (TextView) tmpRow.getChildAt(2);
                        TextView itemRemark = (TextView) tmpRow.getChildAt(3);

                        String remark = (String)itemRemark.getTag(R.id.KEY_1);
                        if(remark ==  null)
                        {
                            remark = " ";
                        }

                        remark = remark.replace("\n", "]");
                        String str = itemName.getText().toString() + "?" +
                                     itemQty.getText().toString() + "?" +
                                     itemPrice.getText().toString() + "?" +
                                     remark + "?" +
                                     "NO" + "?" +
                                     "NO" + "\n";

                        byte[] by_new = str.getBytes();
                        orderFileStream.write(by_new);

                        updateInventory(itemName.getText().toString(), itemQty.getText().toString());
                    }
                    orderFileStream.close();

                    Bitmap bitmap = takeScreenshot();
                    String filepath = Environment.getExternalStorageDirectory() + "/screenshot.png";
                    File imagePath = new File(filepath);
                    saveBitmap(bitmap, imagePath);


                    Uri imageUri = Uri.parse(filepath);


                    Intent sendIntentCopy = new Intent();
                    sendIntentCopy.setAction(Intent.ACTION_SEND);
                    //sendIntent.setData(Uri.parse("sms:+917760985241"));
                    String mscphoneNumber = "919886644280";

                    sendIntentCopy.putExtra("jid", PhoneNumberUtils.stripSeparators(mscphoneNumber) + "@s.whatsapp.net");
                    sendIntentCopy.putExtra(Intent.EXTRA_TEXT, "New order for " + customerName + " received");
                    sendIntentCopy.putExtra(Intent.EXTRA_STREAM, imageUri);
                    sendIntentCopy.setType("image/*");
                    sendIntentCopy.setPackage("com.whatsapp");

                    sendIntentCopy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    sendIntentCopy.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    sendIntentCopy.putExtra("keep", false);
                    startActivityForResult(sendIntentCopy, 1);


                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    //sendIntent.setData(Uri.parse("sms:+917760985241"));
                    if(phoneNumber.startsWith("91") == false)
                    {
                        phoneNumber = "91" + phoneNumber;
                    }
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(phoneNumber) + "@s.whatsapp.net");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Your order is placed. \nThank you\nMarudhar Saree Ceneter");

                    sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    sendIntent.setType("image/*");
                    sendIntent.setPackage("com.whatsapp");

                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    sendIntent.putExtra("keep", false);
                    startActivityForResult(sendIntent, 1);




                }catch (Exception e){}
            }
        });

        custOrderTbl = (TableLayout)findViewById(R.id.showOrderTableLayOut);
        custOrderTbl.removeViews(1, custOrderTbl.getChildCount() - 1);

        for(int i=0; i<count; i++)
        {

            fillTable(this.getApplicationContext(), custOrderTbl, itemName[i], itemQty[i], itemPrice[i], itemRemark[i], i);
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navigateUpTo(new Intent(getBaseContext(), Store.class));

    }
}
