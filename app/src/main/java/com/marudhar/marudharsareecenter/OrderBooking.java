package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;

public class OrderBooking extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String [] itemName = new String[200];
    private String [] itemPrice = new String[200];
    private String [] itemQty = new String[200];
    private static String [] CartItemName = new String[200];
    private static String [] CartItemQty = new String[200];
    private static String [] CartItemPrice = new String[200];
    private static String [] CartItemReview = new String[200];
    private static int count = 0;
    private String customerName;
    private int  customerRating;
    private int maxItem;
    private ViewGroup hiddenPanel;
    private ViewGroup.LayoutParams hiddenPanelParam;
    private ViewGroup.LayoutParams hiddenPanelParamOrig;
    private static Context context;
    private boolean isPanelShown;
    private boolean isPanelTouchTriggered;
    private int currentPosition = 0;

    private void readDesignFile()
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPath, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPath);
        File inventoryData = new File(directory, GlobalVar.inventoryDataFile);

        maxItem = 0;

        if (inventoryData.exists()) {
            try {
                FileReader fd = new FileReader(inventoryData);

                BufferedReader inReader = new BufferedReader(fd);
                try {
                    String fileStr = inReader.readLine();
                    while (fileStr != null) {
                        StringTokenizer tokens = new StringTokenizer(fileStr, "?");
                        itemName[maxItem] = tokens.nextToken();
                        itemPrice[maxItem] = tokens.nextToken();
                        itemQty[maxItem] = tokens.nextToken();
                        String designRating = tokens.nextToken();
                        int inDesignRating = Integer.parseInt(designRating);
                        if(inDesignRating >= customerRating && Integer.parseInt(itemQty[maxItem]) != 0)
                        {
                            maxItem++;
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

    private static int getIndexFromCart(String itemName)
    {
        for(int i = 0; i<count; i++ )
        {
            if(CartItemName[i].contentEquals(itemName))
            {
                return i;
            }
        }
        return 201;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_booking);
        context = getApplicationContext();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent myIntent = this.getIntent();
        customerName = myIntent.getStringExtra("Customer Name");
        customerRating = Integer.parseInt(myIntent.getStringExtra("Customer Rating"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        readDesignFile();
        if(maxItem == 0)
        {
            Toast.makeText(getApplicationContext(), "Item Out of Stock", Toast.LENGTH_LONG);
            navigateUpTo(new Intent(getApplicationContext(), Store.class));
        }

        hiddenPanel = (ViewGroup)findViewById(R.id.hidden_panel);
        hiddenPanelParam = hiddenPanel.getLayoutParams();
        hiddenPanelParamOrig = hiddenPanel.getLayoutParams();
        hiddenPanel.setVisibility(View.INVISIBLE);
        isPanelShown = false;
        isPanelTouchTriggered = false;




        count = 0;
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setTag(false);
        GlobalVar.orderBookingBackPressed = 0;
        GlobalVar.orderBookingBackPressedTime = -1;

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton cartButton = (ImageButton) findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(count == 0)
                {
                    Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent orderScroll = new Intent(context, OrderScroll.class);
                    orderScroll.putExtra("ItemName", CartItemName);
                    orderScroll.putExtra("ItemQty", CartItemQty);
                    orderScroll.putExtra("ItemPrice", CartItemPrice);
                    orderScroll.putExtra("ItemRemark", CartItemReview);
                    orderScroll.putExtra("count", count);
                    orderScroll.putExtra("Customer Name", customerName);
                    startActivity(orderScroll);
                }
            }

        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {




                boolean isImageVisible = (boolean) mViewPager.getTag();
                if (isImageVisible == true) {
                    Log.e("OrdBook PgSc", Integer.toString(mViewPager.getCurrentItem()) + " " + Integer.toString(position) + " " + Float.toString(positionOffset));
                    zoomOut(position);
                }


            }

            @Override
            public void onPageSelected(int position) {

                Log.e("OrdBook PgSele", Integer.toString(position) );

                GlobalVar.orderBookingBackPressed = 0;
                GlobalVar.orderBookingBackPressedTime = -1;



            }

            @Override
            public void onPageScrollStateChanged(int state) {

                Log.e("OrdBook PgScr", Integer.toString(state));

            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int viewHeight;
        String x = Float.toString(event.getX());
        String y = Float.toString(event.getY());
        Log.e("MSCEvent", Integer.toString(event.getAction()) + " " + x + " " + y);
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            isPanelTouchTriggered = true;
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            if(isPanelTouchTriggered) {
                hiddenPanel.setVisibility(View.VISIBLE);
                isPanelShown = true;
            }

            viewHeight = (int)event.getY();
            if(viewHeight > 50)
            {
                viewHeight = 50;
            }


            hiddenPanelParam.height = viewHeight;

            hiddenPanel.setLayoutParams(hiddenPanelParam);
            hiddenPanel.getBackground().setAlpha(80);

            Button hiddenPanelPrice = (Button)findViewById(R.id.hiddenPrice);
            hiddenPanelPrice.setText(itemPrice[mViewPager.getCurrentItem()]);
        }else if(event.getAction() == MotionEvent.ACTION_UP)
        {
            hiddenPanel.setLayoutParams(hiddenPanelParamOrig);
            hiddenPanel.setVisibility(View.INVISIBLE);
            isPanelShown = false;
            isPanelTouchTriggered = false;
        }
        return super.onTouchEvent(event);
    }

    public void zoomOut(int position)
    {

        ViewGroup.LayoutParams tmp = mViewPager.getLayoutParams();
        ((CoordinatorLayout.LayoutParams) tmp).setMargins(0, 50, 0, 0);

        for(int index = 0; index < mViewPager.getChildCount(); index++) {

            RelativeLayout v = (RelativeLayout) mViewPager.getChildAt(index);


            Log.e("zoom OUT", Integer.toString(index));

            if (v != null) {

                Log.e("zoom OUT 1", Integer.toString(position) + " " + v.toString());
                mViewPager.setTag(false);
                int count = v.getChildCount();

                boolean removeView = false;

                for (int i = 0; i < count; i++) {
                    View tmp1 = v.getChildAt(i);
                    if (!((tmp1.getId() == R.id.ItemPrice) || (tmp1.getId() == R.id.AddedToCartIcon))) {


                        if(tmp1.getVisibility() == View.INVISIBLE)
                        {
                            Log.e("zoom OUT 2", Integer.toString(position) + " " + Integer.toString(index) + " " + tmp1.toString());
                            tmp1.setVisibility(View.VISIBLE);
                            removeView = true;
                        }

                    }
                }
                if(removeView == true) {
                    v.removeViewAt(count - 1);
                }
            }
        }
    }

    public void onBackPressed() {

        boolean isImageVisible = (boolean)mViewPager.getTag();


        Calendar c = Calendar.getInstance();



        if(isImageVisible == true) {

            zoomOut(mViewPager.getCurrentItem());
            GlobalVar.orderBookingBackPressed = 0;
        }
        else
        {


            if(GlobalVar.orderBookingBackPressed == 0)
            {
                Toast display = Toast.makeText(getApplicationContext(), "Press Back Key again to exist", Toast.LENGTH_LONG);
                display.setGravity(Gravity.CENTER, 0, 0);
                display.show();
                GlobalVar.orderBookingBackPressed++;
                GlobalVar.orderBookingBackPressedTime = c.getTimeInMillis();

            }
            else
            {
                long currentdiff = c.getTimeInMillis() - GlobalVar.orderBookingBackPressedTime;

                if(currentdiff < 4000) {
                    super.onBackPressed();
                }
                else
                {
                    Toast display = Toast.makeText(getApplicationContext(), "Press Back Key again to exist", Toast.LENGTH_SHORT);
                    display.setGravity(Gravity.CENTER, 0, 0);
                    display.show();
                    GlobalVar.orderBookingBackPressed++;
                    GlobalVar.orderBookingBackPressedTime = SystemClock.currentThreadTimeMillis();
                }
            }
        }

        //if (mViewPager.getCurrentItem() == 0) {
        // If the user is currently looking at the first step, allow the system to handle the
        // Back button. This calls finish() on this activity and pops the back stack.
        //   super.onBackPressed();
        //} else {
           // Otherwise, select the previous step.
        //    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        //}
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_booking, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_ITEMName = "itemName";
        private static final String ARG_ITEMPrice = "itemPrice";
        private static final String ARG_ITEMQty = "itemQty";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String itemName, String itemPrice, String itemQty) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_ITEMName, itemName);
            args.putString(ARG_ITEMPrice, itemPrice);
            args.putString(ARG_ITEMQty, itemQty);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView;

            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                rootView = inflater.inflate(R.layout.fragment_order_booking_landscape, container, false);
            }
            else {

                rootView = inflater.inflate(R.layout.fragment_order_booking, container, false);
            }



            ImageButton takeNotesButton = (ImageButton)rootView.findViewById(R.id.takeNoteButton);

            if(takeNotesButton.getTag(R.id.KEY_1) == null) {

                takeNotesButton.setTag(R.id.KEY_1, rootView);
                //takeNotesButton.setTag(R.id.KEY_2, "");
            }

            takeNotesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View rootView = (View)v.getTag(R.id.KEY_1);
                    String remarks = (String)v.getTag(R.id.KEY_2);

                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(rootView.getContext());

                    dialogBuilder.setTitle("Remarks");

                    LinearLayout remarkLayout = new LinearLayout(rootView.getContext());
                    remarkLayout.setLayoutParams(new LinearLayout.LayoutParams(100,100));
                    remarkLayout.setOrientation(LinearLayout.VERTICAL);



                    final EditText input = new EditText(rootView.getContext());
                    input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    input.setBackgroundResource(R.drawable.cell_shape);
                    input.requestFocus();
                    input.setTag(v);

                    input.setText(remarks);

/*
                    input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            View rootView = (View)input.getTag();
                            rootView.setTag(s.toString());
                        }
                    });
*/

                    remarkLayout.addView(input);
                    dialogBuilder.setView(remarkLayout);

                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            View v = (View)input.getTag();
                            v.setTag(R.id.KEY_2, "");
                            dialog.dismiss();

                        }
                    });

                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            View v = (View)input.getTag();
                            v.setTag(R.id.KEY_2, input.getText().toString());

                            dialog.dismiss();

                        }
                    });

                    //Create alert dialog object via builder
                    AlertDialog alertDialogObject = dialogBuilder.create();


                    //Show the dialog
                    alertDialogObject.show();

                }
            });

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            textView.setText(getArguments().getString(ARG_ITEMName));
            textView.setTextSize(30);

            TextView itemPrice = (TextView) rootView.findViewById(R.id.ItemPrice);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            itemPrice.setText(getArguments().getString(ARG_ITEMPrice));
            itemPrice.setVisibility(View.INVISIBLE);


            TextView itemQty = (TextView)rootView.findViewById(R.id.itemqty);
            itemQty.setText("0");
            //itemQty.setShowSoftInputOnFocus(false);

            rootView.setTag(R.id.KEY_1, getArguments().getString(ARG_ITEMQty));
            Button decreaseButton = (Button) rootView.findViewById(R.id.decreaseButton);
            decreaseButton.setTag(rootView);
            decreaseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    View tmp = (View)(v.getTag());
                    TextView itemQty = (TextView)tmp.findViewById(R.id.itemqty);
                    int num1 =  Integer.parseInt(itemQty.getText().toString());
                    if(num1 > 0)
                    {
                        num1 = num1 - 1;
                        itemQty.setText(Integer.toString(num1));

                    }
                }
            });

            Button increaseButton = (Button) rootView.findViewById(R.id.increaseButton);
            increaseButton.setTag(rootView);
            increaseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    View tmp = (View)(v.getTag());
                    String itemQtyStr = (String)tmp.getTag(R.id.KEY_1);
                    int itemMaxQty = Integer.parseInt(itemQtyStr);
                    TextView itemQty = (TextView)tmp.findViewById(R.id.itemqty);
                    int num1 =  Integer.parseInt(itemQty.getText().toString());
                    num1 = num1 + 1;
                    if(num1 <= itemMaxQty) {
                        itemQty.setText(Integer.toString(num1));
                    }


                }
            });

            Button addToCart = (Button) rootView.findViewById(R.id.addToCart);
            addToCart.setTag(rootView);
            addToCart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    View tmp = (View)(v.getTag());
                    TextView itemQty = (TextView)tmp.findViewById(R.id.itemqty);
                    TextView itemName = (TextView)tmp.findViewById(R.id.section_label);
                    TextView itemPrice = (TextView)tmp.findViewById(R.id.ItemPrice);
                    ImageView addedToCartImage = (ImageView)tmp.findViewById(R.id.AddedToCartIcon);
                    ImageButton takeNotesButton = (ImageButton)tmp.findViewById(R.id.takeNoteButton);
                    int num1 =  Integer.parseInt(itemQty.getText().toString());
                    if(num1 == 0)
                    {
                        Toast.makeText(context, "Nothing to Add", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        int index = getIndexFromCart(itemName.getText().toString());
                        if(index == 201)
                        {
                            index = count;
                            count++;
                        }
                        CartItemName[index] = itemName.getText().toString();
                        CartItemQty[index] = itemQty.getText().toString();
                        CartItemPrice[index] = itemPrice.getText().toString();
                        CartItemReview[index] = (String)takeNotesButton.getTag(R.id.KEY_2);

                        View abc = tmp.getRootView();
                        EditText cartQty = (EditText)abc.findViewById(R.id.cartQty);
                        cartQty.setText(Integer.toString(count));
                        addedToCartImage.setVisibility(View.VISIBLE);

                        ViewPager mViewPager= (ViewPager)abc.findViewById(R.id.container);
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);


                    }
                }
            });
            //ImageView imageView1 = (ImageView) rootView.findViewById(R.id.orderBookImage1);

            ContextWrapper contextWrapper = new ContextWrapper(context);
            //File directory = contextWrapper.getDir(GlobalVar.InventoryPhoto, Context.MODE_PRIVATE);

            File directory = new File(GlobalVar.InventoryPhoto);

            String picturePath = directory.toString() + "/" + getArguments().getString(ARG_ITEMName);

            ImageView orderBookingImage1 = (ImageView)rootView.findViewById(R.id.OrderBookImageView1);
            ImageView orderBookingImage2 = (ImageView)rootView.findViewById(R.id.OrderBookImageView2);
            ImageView orderBookingImage3 = (ImageView)rootView.findViewById(R.id.OrderBookImageView3);
            ImageView orderBookingImage4 = (ImageView)rootView.findViewById(R.id.OrderBookImageView4);



            GlobalVar.width = 1000;
            GlobalVar.height = 1000;
            GlobalVar.loadBitmap(context, picturePath + "1",orderBookingImage1);

            //GlobalVar.width = 200;
            //GlobalVar.height = 200;
            GlobalVar.loadBitmap(context, picturePath + "2",orderBookingImage2);
            GlobalVar.loadBitmap(context, picturePath + "3",orderBookingImage3);
            GlobalVar.loadBitmap(context, picturePath + "4",orderBookingImage4);

            //stackView.setAdapter(new ImageAdapter(context, getArguments().getString(ARG_ITEMName), 4));

            //stackView.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent e){

//                    if(e.getAction() == MotionEvent.ACTION_DOWN ) {

//                        StackView tmp = (StackView) v;
//                        tmp.showNext();
//                    }
  //                  return true;
  //              }

   //         });


            //Log.e("MSC onCreateView", textView.getText().toString() + " " + Integer.parseInt(itemQty.getText().toString()));


            String tmp = (String)takeNotesButton.getTag(R.id.KEY_2);

            Log.e("MSC onCreateView", textView.getText().toString() + " " + Integer.parseInt(itemQty.getText().toString()) + " " + tmp);

            return rootView;
        }
/*
        @Override
        public void onPause() {


            RelativeLayout rootView = (RelativeLayout)this.getView();

            ViewPager mViewPager = (ViewPager)rootView.getParent();

            mViewPager.setTag(false);

            ViewGroup.LayoutParams tmp = mViewPager.getLayoutParams();
            ((CoordinatorLayout.LayoutParams) tmp).setMargins(0, 50, 0, 0);

            int visible = (int)rootView.getTag(R.id.OrderBookImageVisible);

            if(rootView != null) {
                int count = rootView.getChildCount();




                for (int i = 0; i < count; i++) {
                    View tmp1 = rootView.getChildAt(i);
                    if (!((tmp1.getId() == R.id.ItemPrice) || (tmp1.getId() == R.id.AddedToCartIcon))) {
                        tmp1.setVisibility(View.VISIBLE);
                    }
                }
                rootView.removeViewAt(count - 1);
            }



            super.onPause();

        }

        /*
        @Override
        public void onResume() {

            View rootView = this.getView();

            TextView itemQty = (TextView)rootView.findViewById(R.id.itemqty);
            TextView itemName = (TextView)rootView.findViewById(R.id.section_label);
            ImageView addedToCartImage = (ImageView)rootView.findViewById(R.id.AddedToCartIcon);
            ImageButton takeNote = (ImageButton)rootView.findViewById(R.id.takeNoteButton);

            String tmp = (String)takeNote.getTag(R.id.KEY_2);

            Log.e("MSC onResume", itemName.getText().toString() + " " + Integer.parseInt(itemQty.getText().toString()) + " " + tmp);



            if(!itemQty.getText().toString().equals("0"))
            {
                addedToCartImage.setVisibility(View.VISIBLE);
                takeNote.setEnabled(true);
            }

            super.onResume();
        }
*/
        @Override
        public void onSaveInstanceState(Bundle outState)
        {
            View rootView = this.getView();
            if(rootView != null) {
                ImageView addedToCartImage = (ImageView) rootView.findViewById(R.id.AddedToCartIcon);
                ImageButton takeNote = (ImageButton) rootView.findViewById(R.id.takeNoteButton);

                String remark = "";
                if (takeNote != null) {
                    remark = (String) takeNote.getTag(R.id.KEY_2);
                }
                int visibility = addedToCartImage.getVisibility();


                outState.putString("Remark", remark);
                outState.putInt("Visibility", visibility);
            }

            super.onSaveInstanceState(outState);


        }

        public void onViewStateRestored(Bundle inState)
        {
            super.onViewStateRestored(inState);
            View rootView = this.getView();
            ImageView addedToCartImage = (ImageView)rootView.findViewById(R.id.AddedToCartIcon);
            ImageButton takeNote = (ImageButton)rootView.findViewById(R.id.takeNoteButton);

            if(inState != null)
            {
                String remark = inState.getString("Remark");
                int visibility = inState.getInt("Visibility");

                takeNote.setTag(R.id.KEY_2, remark);
                addedToCartImage.setVisibility(visibility);
            }

        }




    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            GlobalVar.itemPrice = itemPrice[position];
            return PlaceholderFragment.newInstance(position + 1, itemName[position], itemPrice[position], itemQty[position]);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return maxItem;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }
}
