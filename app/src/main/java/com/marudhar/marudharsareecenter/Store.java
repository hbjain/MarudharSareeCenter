package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Store extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private List<String> gCustomerDetails;
    private List<String> gFilteredCustomerDetails;

    private List<String> gCustomerTypeDetails;
    private List<String> gFilteredCustomerTypeDetails;

    private void getCustomerDetails()
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
                if(fileStr != null)
                {
                    gCustomerDetails = new ArrayList<String>();
                    gFilteredCustomerDetails = new ArrayList<String>();
                    gCustomerTypeDetails = new ArrayList<String>();
                    gFilteredCustomerTypeDetails = new ArrayList<String>();
                }
                while(fileStr != null)
                {
                    StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                    int index = 0;
                    while (tokens.hasMoreTokens()) {
                        fields[index] = tokens.nextToken();
                        index++;
                    }
                    //String str = tokens.nextToken();
                    gCustomerDetails.add(fields[0]);
                    gFilteredCustomerDetails.add(fields[0]);

                    gCustomerTypeDetails.add(fields[0] + "?" + fields[7]);
                    gFilteredCustomerTypeDetails.add(fields[0] + "?" + fields[7]);


                    fileStr = inReader.readLine();
                }
            }catch(Exception e){}
            Collections.sort(gCustomerDetails, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            Collections.sort(gFilteredCustomerDetails, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            Collections.sort(gCustomerTypeDetails, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            Collections.sort(gFilteredCustomerTypeDetails, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
        }
    }

    private void DisplayCustomerDetails()
    {
        //Create sequence of items
        final CharSequence[] customerDetail = gCustomerDetails.toArray(new String[gCustomerDetails.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Select Customer");
        LinearLayout custSelectLayout = new LinearLayout(this);

        custSelectLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        custSelectLayout.setOrientation(LinearLayout.VERTICAL);

        ListView customerList = new ListView(this);
        customerList.setTag(this);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gFilteredCustomerDetails);
        customerList.setAdapter(itemsAdapter);
        customerList.setPadding(0,20,0,0);

        customerList.setItemsCanFocus(true);

        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String[] orderBookingType = new String[2];
                orderBookingType[0] = "Text View";
                orderBookingType[1] = "Image View";

                ListView tmpList = (ListView)parent;
                ArrayAdapter<String> tmpItemsAdapter = (ArrayAdapter<String>)tmpList.getAdapter();

                String selectedText = "";
                if(tmpItemsAdapter.getItem(position) != null) //Selected item in listview
                {
                    selectedText = tmpItemsAdapter.getItem(position).toString();
                }

                StringTokenizer tmp = new StringTokenizer(gFilteredCustomerTypeDetails.get(position), "?");
                String custRating = tmp.nextToken();
                custRating = tmp.nextToken();

                Context ctx = (Context)tmpList.getTag();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);

                LinearLayout orderBookSelectLayout = new LinearLayout(ctx);

                orderBookSelectLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                orderBookSelectLayout.setOrientation(LinearLayout.VERTICAL);

                ListView orderBookTypeList = new ListView(ctx);


                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, orderBookingType);
                orderBookTypeList.setAdapter(itemsAdapter);
                orderBookTypeList.setPadding(0,20,0,0);

                orderBookTypeList.setTag(R.id.KEY_1, selectedText);
                orderBookTypeList.setTag(R.id.KEY_2, custRating);


                dialogBuilder.setTitle("Select Order Booking type");
                orderBookTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent;

                        String selectedText = (String)parent.getTag(R.id.KEY_1);
                        String custRating = (String)parent.getTag(R.id.KEY_2);
                        if(position == 0)
                        {
                            intent = new Intent(Store.this, OrderBookingSimple.class);
                        }
                        else
                        {
                            intent = new Intent(Store.this, OrderBooking.class);
                        }
                        intent.putExtra("Customer Name", selectedText);
                        intent.putExtra("Customer Rating", custRating);
                        startActivity(intent);

                    }
                });
                orderBookSelectLayout.addView(orderBookTypeList);
                dialogBuilder.setView(orderBookSelectLayout);
                AlertDialog alertDialogObject = dialogBuilder.create();
                alertDialogObject.show();


            }
        });

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        input.setPadding(0,10,0,10);
        input.setBackgroundResource(R.drawable.cell_shape);
        input.requestFocus();

        input.setTag(R.id.KEY_1, custSelectLayout);
        input.setTag(R.id.KEY_2, customerList);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ListView tmpCustomerList = (ListView)input.getTag(R.id.KEY_2);


                gFilteredCustomerDetails.clear();
                gFilteredCustomerTypeDetails.clear();
                for(int i = 0; i < gCustomerDetails.size(); i++)
                {
                    if(gCustomerDetails.get(i).toLowerCase().startsWith(s.toString().toLowerCase()))
                    {
                        gFilteredCustomerDetails.add(gCustomerDetails.get(i));
                        gFilteredCustomerTypeDetails.add(gCustomerTypeDetails.get(i));
                    }
                }


                ArrayAdapter<String> itemsAdapterNew =
                        new ArrayAdapter<String>(Store.this, android.R.layout.simple_list_item_1, gFilteredCustomerDetails);

                tmpCustomerList.setAdapter(null);
                tmpCustomerList.setAdapter(itemsAdapterNew);


                Log.e("TT", s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });



        custSelectLayout.addView(input);
        custSelectLayout.addView(customerList);
        dialogBuilder.setView(custSelectLayout);
        /*
        dialogBuilder.setItems(customerDetail, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = customerDetail[item].toString();  //Selected item in listview
                Intent intent = new Intent(Store.this, OrderBooking.class);
                intent.putExtra("Customer Name", selectedText);
                startActivity(intent);
            }
        });*/

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();*/

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        File dir = getExternalFilesDir("");

        String baseDir = dir.getAbsolutePath();

        GlobalVar.InventoryPath = baseDir + "/Inventory";
        dir = new File(GlobalVar.InventoryPath);
        if(!dir.exists())
        {
            dir.mkdir();
        }

        GlobalVar.InventoryPhoto = baseDir + "/InventoryPhoto";
        dir = new File(GlobalVar.InventoryPhoto);
        if(!dir.exists())
        {
            dir.mkdir();
        }

        GlobalVar.CustomerInfo = baseDir + "/Customer";
        dir = new File(GlobalVar.CustomerInfo);
        if(!dir.exists())
        {
            dir.mkdir();
        }

        GlobalVar.CustomerOrderDetails = baseDir + "/CustomerOrder";
        dir = new File(GlobalVar.CustomerOrderDetails);
        if(!dir.exists())
        {
            dir.mkdir();
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i("MSC", "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, 3);
        } catch (IntentSender.SendIntentException e) {
            Log.e("MSC", "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("MSC", "API client connected.");

    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("msc", "GoogleApiClient connection suspended");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            Intent intent = new Intent(this, Inventory.class);
            startActivity(intent);

        } else if (id == R.id.nav_add_customer) {
            Intent intent = new Intent(this, NewCustomer.class);
            startActivity(intent);

        }else if (id == R.id.nav_search_customer) {
            Intent intent = new Intent(this, CustomerInfo.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_new_order) {


            getCustomerDetails();
            if(gCustomerDetails != null && gCustomerDetails.size() > 0)
            {
                DisplayCustomerDetails();
            }
            //Intent intent = new Intent(this, OrderBooking.class);
            //startActivity(intent);


        } else if (id == R.id.nav_view_order) {
            Intent intent = new Intent(this, ViewOrder.class);
            startActivity(intent);

        }else if (id == R.id.nav_generate_report) {
            Intent intent = new Intent(this, GenerateReport.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
