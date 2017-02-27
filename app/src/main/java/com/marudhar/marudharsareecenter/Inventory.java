package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.StringTokenizer;

public class Inventory extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    FileOutputStream designFileStream;
    @SuppressWarnings("WeakerAccess")
    File inventoryData;
    @SuppressWarnings("WeakerAccess")
    TableLayout inventoryTbl;
    @SuppressWarnings("WeakerAccess")
    View.OnTouchListener imgTouchListner;
    @SuppressWarnings("WeakerAccess")
    BufferedReader inReader;
    @SuppressWarnings("WeakerAccess")
    View.OnClickListener listnerObj;
    @SuppressWarnings("WeakerAccess")
    int buttonClikced = 0;
    @SuppressWarnings("WeakerAccess")
    String selectedDesign = "";
    @SuppressWarnings("WeakerAccess")
    String newDesignAdded = "";
    @SuppressWarnings("WeakerAccess")
    int res = 1;
    @SuppressWarnings("WeakerAccess")
    boolean cameraSelected = false;
    //Drawable selectedDesignBackGround;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void clearImages()
    {
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView);
        imageView1.setImageResource(R.drawable.ic_menu_camera);
        imageView1.setOnClickListener(null);

        ImageView imageView2 = (ImageView) findViewById(R.id.imageView1);
        imageView2.setImageResource(R.drawable.ic_menu_camera);
        imageView2.setOnClickListener(null);

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView2);
        imageView3.setImageResource(R.drawable.ic_menu_camera);
        imageView3.setOnClickListener(null);

        ImageView imageView4 = (ImageView) findViewById(R.id.imageView3);
        imageView4.setImageResource(R.drawable.ic_menu_camera);
        imageView4.setOnClickListener(null);
    }

    @SuppressWarnings("WeakerAccess")
    public void fillTable(TableLayout searchTbl, String name, String price, String quantity, int selection) {

        String [] designRating = new String[4];
        designRating[0] = "-";
        designRating[1] = "A";
        designRating[2] = "B";
        designRating[3] = "C";

        TableRow temp = new TableRow(this);

        TextView tv1 = new TextView(this);
        TextView tv2 = new TextView(this);
        TextView tv3 = new TextView(this);
        TextView tv4 = new TextView(this);

        tv1.setText(name);
        tv1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        tv1.setTextSize(22);
        tv1.setPadding(3,3,3,3);

        tv2.setText(price);
        tv2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        tv2.setTextSize(22);
        tv2.setPadding(3,3,3,3);

        tv3.setText(quantity);
        tv3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        tv3.setTextSize(22);
        tv3.setPadding(3,3,3,3);

        tv4.setText(designRating[selection]);
        tv4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cell_shape));
        tv4.setTextSize(22);
        tv4.setPadding(3,3,3,3);

        temp.addView(tv1);
        temp.addView(tv2);
        temp.addView(tv3);
        temp.addView(tv4);
        temp.setTag(selection);
        temp.setOnClickListener(listnerObj);
        searchTbl.addView(temp);
        searchTbl.setShrinkAllColumns(true);
    }

    @SuppressWarnings("WeakerAccess")
    public void setInitialInventoryTable() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPath, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPath);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        inventoryTbl.removeViews(1, inventoryTbl.getChildCount() - 1);
        inventoryData = new File(directory, GlobalVar.inventoryDataFile);

        //inventoryData.delete();
        if (inventoryData.exists()) {
            inventoryData.setWritable(true);
            try {
                FileReader fd = new FileReader(inventoryData);

                inReader = new BufferedReader(fd);
                try {
                    String fileStr = inReader.readLine();
                    while (fileStr != null) {


                        StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                        String name = tokens.nextToken();
                        String price = tokens.nextToken();
                        String quantity = tokens.nextToken();
                        int selection = Integer.parseInt(tokens.nextToken());
                        fillTable(inventoryTbl, name, price, quantity, selection);
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
        } else {
            try {
                inventoryData.createNewFile();
                inventoryData.setWritable(true);
            } catch (IOException e) {
            }
        }

        EditText designName = (EditText) findViewById(R.id.DesignName);
        EditText itemprice = (EditText) findViewById(R.id.ItemPrice);
        EditText qty = (EditText) findViewById(R.id.Quantity);
        Spinner itemProductRating = (Spinner) findViewById(R.id.ProductRating);

        itemprice.setText("");
        itemprice.setEnabled(true);
        qty.setText("");
        qty.setEnabled(true);
        designName.setText("");
        designName.setEnabled(true);
        itemProductRating.setSelection(0);

        clearImages();


    }

    private void confirmItem(String itemName)
    {
        AlertDialog.Builder itemConfirm = new AlertDialog.Builder(this);
        itemConfirm.setTitle(itemName + " Added ");
        itemConfirm.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface i, int j) {
               UpdatePhotos();
            }
        });
        AlertDialog dialog = itemConfirm.create();
        dialog.show();
    }

    @SuppressWarnings("WeakerAccess")
    public void UpdatePhotos() {
        AlertDialog.Builder photobuilder = new AlertDialog.Builder(this);


        photobuilder.setTitle("Upload photos").setItems(R.array.Photo,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0 ) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), res);
                    cameraSelected = false;
                }
                else
                {
                    //ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                    //File directory = contextWrapper.getDir(GlobalVar.InventoryPhoto, Context.MODE_PRIVATE);


                    //File destFile = new File(directory, newDesignAdded + res);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    String filepath = Environment.getExternalStorageDirectory().getPath() +"/tmp";
                    //String filepath = "/sdcard/tmp";
                    Uri intentUri;

                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= 24) {
                        intentUri = Uri.parse(filepath);
                    }
                    else{
                        intentUri = Uri.fromFile(new File(filepath));
                    }



                   // cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp/tmp")));
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, intentUri);
                    startActivityForResult(cameraIntent, res);
                    cameraSelected = true;
                }

            }
        });
        photobuilder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface i, int j) {
                        newDesignAdded = "";
                        res = 0;
                        clearImages();
                    }
                });


        AlertDialog dialog = photobuilder.create();
        dialog.show();


    }

    @SuppressWarnings("WeakerAccess")
    public void showpicture()
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPhoto, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPhoto);

        String picturePath = directory.toString() + "/" + newDesignAdded;

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView);
        GlobalVar.height = 200;
        GlobalVar.width = 200;
        //GlobalVar.loadBitmap(getApplicationContext(),picturePath + "1",imageView1);
        imageView1.setImageBitmap(BitmapFactory.decodeFile(picturePath + "1"));
        imageView1.setTag(picturePath + "1");
        imageView1.setOnTouchListener(imgTouchListner);

        ImageView imageView2 = (ImageView) findViewById(R.id.imageView1);
        GlobalVar.height = 50;
        GlobalVar.width = 50;
        //GlobalVar.loadBitmap(getApplicationContext(),picturePath + "2",imageView2);
        imageView2.setImageBitmap(BitmapFactory.decodeFile(picturePath + "2"));
        imageView2.setTag(picturePath + "2");
        imageView2.setOnTouchListener(imgTouchListner);

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView2);
        GlobalVar.height = 200;
        GlobalVar.width = 200;
        //GlobalVar.loadBitmap(getApplicationContext(),picturePath + "3",imageView3);
        imageView3.setImageBitmap(BitmapFactory.decodeFile(picturePath + "3"));
        imageView3.setTag(picturePath + "3");
        imageView3.setOnTouchListener(imgTouchListner);

        ImageView imageView4 = (ImageView) findViewById(R.id.imageView3);
        GlobalVar.height = 0;
        GlobalVar.width = 0;
        //GlobalVar.loadBitmap(getApplicationContext(),picturePath + "4",imageView4);
        imageView4.setImageBitmap(BitmapFactory.decodeFile(picturePath + "4"));
        imageView4.setTag(picturePath + "4");
        imageView4.setOnTouchListener(imgTouchListner);

    }

    @SuppressWarnings("WeakerAccess")
    public void copyFile(String sourceFile, String itemName) throws IOException {

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPhoto, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPhoto);
        File destFile = new File(directory, itemName);

        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picturePath = "";
        if (requestCode <= 4 && resultCode == RESULT_OK) {
            if(data != null) {
                Uri selectedImage = data.getData();


                if (selectedImage != null) {

                    picturePath = GlobalVar.getPath(Inventory.this, selectedImage);


                }
            }else {

                picturePath = Environment.getExternalStorageDirectory().getPath() +"/tmp";
            }
            if(picturePath.isEmpty() == false){

                try {
                        copyFile(picturePath, newDesignAdded + requestCode);
                } catch (IOException e) {}

                if (requestCode == 1) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    imageView.setTag(picturePath);
                    imageView.setOnTouchListener(imgTouchListner);
                } else if (requestCode == 2) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    imageView.setTag(picturePath);
                    imageView.setOnTouchListener(imgTouchListner);
                } else if (requestCode == 3) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    imageView.setTag(picturePath);
                    imageView.setOnTouchListener(imgTouchListner);
                } else if (requestCode == 4) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView3);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    imageView.setTag(picturePath);
                    imageView.setOnTouchListener(imgTouchListner);
                }

            }


            if(requestCode < 4) {

                res = res + 1;
                UpdatePhotos();
                /*
                data.setType("image/*");
                data.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(data,
                        "Select Picture"), requestCode + 1);
                */
            }
            else
            {
                newDesignAdded = "";
                res = 0;
                clearImages();
            }

        }


    }


    @SuppressWarnings("WeakerAccess")
    public void UpdateOrDelItem(String itemDesignName, String str, boolean delete) {

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPath, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPath);

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

            designFileStream = new FileOutputStream(tmpFile, true);
            FileReader fd = new FileReader(inventoryData);
            inReader = new BufferedReader(fd);
            String fileStr = inReader.readLine();
            while (fileStr != null) {
                StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                String name = tokens.nextToken();
                if (name.contentEquals(itemDesignName)) {
                    if (delete == false) {
                        byte[] by_new = str.getBytes();
                        designFileStream.write(by_new);
                    }
                } else {
                    fileStr = fileStr + "\n";
                    byte[] by_new = fileStr.getBytes();
                    designFileStream.write(by_new);
                }
                fileStr = inReader.readLine();

            }
            inReader.close();
            designFileStream.close();

            if(tmpFile.renameTo(inventoryData)) {
                inventoryData = new File(directory, GlobalVar.inventoryDataFile);
            }
        } catch (Exception e) {
        }

    }

    @SuppressWarnings("WeakerAccess")
    public void callOnClick(View view) {
        EditText itemDesignName = (EditText) findViewById(R.id.DesignName);
        EditText itemPrice = (EditText) findViewById(R.id.ItemPrice);
        EditText itemQty = (EditText) findViewById(R.id.Quantity);
        Spinner itemProductRating = (Spinner)findViewById(R.id.ProductRating);

        if (itemDesignName.getText().toString().isEmpty() ||
                itemPrice.getText().toString().isEmpty() ||
                itemQty.getText().toString().isEmpty() ||
                itemProductRating.getSelectedItemPosition() == 0) {
            if (itemDesignName.getText().toString().isEmpty()) {
                itemDesignName.requestFocus();
                Snackbar.make(view, "Enter Design Name", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (itemPrice.getText().toString().isEmpty()) {
                itemPrice.requestFocus();
                Snackbar.make(view, "Enter Item Price", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (itemQty.getText().toString().isEmpty()) {
                itemQty.requestFocus();
                Snackbar.make(view, "Enter Item Qty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else if (itemProductRating.getSelectedItemPosition() == 0) {
                itemProductRating.requestFocus();
                Snackbar.make(view, "Enter Product Rating", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        } else {


            String str = itemDesignName.getText().toString() + "?" +
                    itemPrice.getText().toString() + "?" +
                    itemQty.getText().toString() + "?" +
                    Integer.toString(itemProductRating.getSelectedItemPosition()) + "?" + "\n";
            if (buttonClikced == 0)
            {
                boolean itemFound = false;
                if(inventoryTbl.getChildCount() > 1)
                {
                    for(int i = 1; i< inventoryTbl.getChildCount(); i++)
                    {
                        TableRow tblRow = (TableRow)inventoryTbl.getChildAt(i);
                        TextView tmp = (TextView)tblRow.getChildAt(0);
                        if(itemDesignName.getText().toString().contentEquals(tmp.getText()))
                        {
                            itemFound = true;
                        }
                    }
                }
                if(itemFound == false) {
                    byte[] by_new = str.getBytes();
                    try {
                        designFileStream = new FileOutputStream(inventoryData, true);
                        try {

                            designFileStream.write(by_new);
                            Snackbar.make(view, str, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            designFileStream.close();
                            newDesignAdded = itemDesignName.getText().toString();
                            confirmItem(newDesignAdded);
                        } catch (IOException e) {
                            Snackbar.make(view, "File write failed", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (FileNotFoundException e) {
                        Snackbar.make(view, "File not found", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                }else
                {
                    Snackbar.make(view, "Inventory already exist", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else if (buttonClikced == 1) {
                UpdateOrDelItem(itemDesignName.getText().toString(), str, false);
            } else if (buttonClikced == 2) {
                UpdateOrDelItem(itemDesignName.getText().toString(), str, true);
            }
            setInitialInventoryTable();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        inventoryTbl = (TableLayout) findViewById(R.id.InventoryTableLayout);
        //TableRow tmp = (TableRow) findViewById(R.id.InventoryTableRow);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        listnerObj = new View.OnClickListener() {
            public void onClick(View arg) {
                TableRow tmp1 = (TableRow) arg;


                if (((TextView) tmp1.getChildAt(0)).getText().toString().contentEquals(selectedDesign)) {
                    EditText designName = (EditText) findViewById(R.id.DesignName);
                    EditText itemprice = (EditText) findViewById(R.id.ItemPrice);
                    EditText qty = (EditText) findViewById(R.id.Quantity);
                    Spinner itemProductRating = (Spinner)findViewById(R.id.ProductRating);

                    designName.setText(((TextView) tmp1.getChildAt(0)).getText());
                    designName.setEnabled(false);
                    itemprice.setText(((TextView) tmp1.getChildAt(1)).getText());
                    qty.setText(((TextView) tmp1.getChildAt(2)).getText());
                    itemProductRating.setSelection((int)tmp1.getTag());

                    Button InventoryAddUpdateButton = (Button) findViewById(R.id.InventoryAddUpdateButton);
                    InventoryAddUpdateButton.setText("Update");
                    newDesignAdded = designName.getText().toString();
                    showpicture();
                    newDesignAdded = "";
                    buttonClikced = 1;


                    selectedDesign = "";

                }
                selectedDesign = ((TextView) tmp1.getChildAt(0)).getText().toString();

            }

        };

        imgTouchListner = new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent e){


                //String filename = "";
                //ImageView imageView1 = (ImageView)v;
                //ImageView tmp = new ImageView(getApplicationContext());

                //filename = (String)imageView1.getTag();
                //tmp.setImageBitmap(BitmapFactory.decodeFile(filename));

                //tmp.setScaleType(ImageView.ScaleType.FIT_XY);
                //PopupWindow popUp = new PopupWindow(600, 600);
                //tmp.setTag(popUp);
                //popUp.setTouchable(true);
                //popUp.setFocusable(true);
                //popUp.setContentView(tmp);





                //popUp.showAsDropDown(findViewById(R.id.activity_inventory));
//                popUp.showAtLocation(findViewById(R.id.activity_inventory), Gravity.CENTER , 0, 0);
                //popUp.update(50, 50, 300, 80);
                return true;
            }
        };

        setInitialInventoryTable();

        final EditText designName = (EditText) findViewById(R.id.DesignName);

        designName.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = "";
                String price = "";
                String quantity = "";
                int selection = 0;

                EditText designNameItem = (EditText) findViewById(R.id.DesignName);
                {
                    inventoryTbl.removeViews(1, inventoryTbl.getChildCount() - 1);
                    String str = designNameItem.getText().toString();
                    str = str.toLowerCase();
                    try {
                        FileReader fd = new FileReader(inventoryData);

                        {
                            inReader = new BufferedReader(fd);
                            try {
                                String fileStr = inReader.readLine();
                                while (fileStr != null) {
                                    StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                                    name = tokens.nextToken();
                                    name = name.toLowerCase();
                                    price = tokens.nextToken();
                                    quantity = tokens.nextToken();
                                    selection = Integer.parseInt(tokens.nextToken());
                                    if (name.matches("(.*)" + str + "(.*)")) {
                                        fillTable(inventoryTbl, name, price, quantity, selection);
                                    } else {

                                    }
                                    fileStr = inReader.readLine();
                                }
                            } catch (IOException e) {
                            }
                            try {
                                inReader.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (FileNotFoundException e) {
                    }
                }
            }

        });

        final Button addUpdateButton = (Button) findViewById(R.id.InventoryAddUpdateButton);
        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOnClick(view);
                Button inventoryAddUpdateItem = (Button) findViewById(R.id.InventoryAddUpdateButton);
                addUpdateButton.setText("Add/Update");
                buttonClikced = 0;
            }

        });

        final Button cancelButton = (Button) findViewById(R.id.InventoryCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClikced = 3;
                callOnClick(view);
                Button inventoryAddUpdateItem = (Button) findViewById(R.id.InventoryAddUpdateButton);
                addUpdateButton.setText("Add/Update");
                buttonClikced = 0;
            }

        });

        final Button deleteButton = (Button) findViewById(R.id.InventoryDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClikced = 2;
                callOnClick(view);
                Button inventoryAddUpdateItem = (Button) findViewById(R.id.InventoryAddUpdateButton);
                addUpdateButton.setText("Add/Update");
                buttonClikced = 0;

            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @SuppressWarnings("WeakerAccess")
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Inventory Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
