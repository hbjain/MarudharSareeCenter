package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.StringTokenizer;

public class ViewOrder extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        mViewPager = (ViewPager) findViewById(R.id.container);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, mViewPager);


        // Set up the ViewPager with the sections adapter
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_order_new, menu);
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

        private View.OnClickListener l;
        private String[] orderFileName;
        private TableLayout custOrderTable;
        private static Context appContext;
        private static ViewPager mViewPager;


        private void getOrderFile() {

            ContextWrapper contextWrapper = new ContextWrapper(appContext);
            //File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

            File directory = new File(GlobalVar.CustomerOrderDetails);

            if (directory.isDirectory()) {
                orderFileName = directory.list();
            }

        }

        private void DisplayOrderInfo(TableLayout orderTable, int index, String fileName) {
            ContextWrapper contextWrapper = new ContextWrapper(appContext);
            //File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

            File directory = new File(GlobalVar.CustomerOrderDetails);


            StringTokenizer fields = new StringTokenizer(fileName, "_");
            if (fields.countTokens() > 1) {
                String tmp = fields.nextToken();
                String custName = fields.nextToken();
                String orderDate = fields.nextToken();
                tmp = fields.nextToken();
                StringTokenizer tmpFields = new StringTokenizer(tmp, ".");
                String orderTime = tmpFields.nextToken();
                TableRow row = new TableRow(appContext);
                TextView slno = new TextView(appContext);
                TextView name = new TextView(appContext);
                TextView date = new TextView(appContext);
                TextView time = new TextView(appContext);

                slno.setText(Integer.toString(index));
                slno.setBackground(ContextCompat.getDrawable(appContext, R.drawable.cell_shape));
                slno.setPadding(3, 3, 3, 3);
                slno.setTextSize(18);

                name.setText(custName);
                name.setBackground(ContextCompat.getDrawable(appContext, R.drawable.cell_shape));
                name.setPadding(3, 3, 3, 3);
                name.setTextSize(18);

                date.setText(orderDate);
                date.setBackground(ContextCompat.getDrawable(appContext, R.drawable.cell_shape));
                date.setPadding(3, 3, 3, 3);
                date.setTextSize(18);

                time.setText(orderTime);
                time.setBackground(ContextCompat.getDrawable(appContext, R.drawable.cell_shape));
                time.setPadding(3, 3, 3, 3);
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

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(Context context, int sectionNumber, ViewPager inViewPager) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            appContext = context;
            mViewPager = inViewPager;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_view_order, container, false);


            TableLayout custOrderTable = (TableLayout) rootView.findViewById(R.id.viewOrderTblNew);

            getOrderFile();

            //if (section == 1) {

                l = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ContextWrapper contextWrapper = new ContextWrapper(appContext);
                        //File directory = contextWrapper.getDir(GlobalVar.CustomerOrderDetails, Context.MODE_PRIVATE);

                        File directory = new File(GlobalVar.CustomerOrderDetails);

                        File orderFile = new File(directory, (String) (v.getTag()));
                        if (orderFile.exists()) {
                            Intent i = new Intent(appContext, ViewOrderForm.class);
                            i.putExtra("FileName", (String) (v.getTag()));
                            //i.putExtra("Section", (section == 1));
                            startActivityForResult(i, 1);
                        }
                    }
                };
            //}

            int pendingIndex = 0;
            int completedIndex = 0;
            for (int index = 0; index < orderFileName.length; index++) {
                if (section == 1) {
                    if (!orderFileName[index].startsWith("completed")) {
                        DisplayOrderInfo(custOrderTable, pendingIndex, orderFileName[index]);
                        pendingIndex++;
                    }
                } else {
                    if (orderFileName[index].startsWith("completed")) {
                        DisplayOrderInfo(custOrderTable, completedIndex, orderFileName[index]);
                        completedIndex++;
                    }
                }
            }
            return rootView;
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            PagerAdapter tmp = mViewPager.getAdapter();
            mViewPager.setAdapter(null);
            mViewPager.setAdapter(tmp);

        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Context appContex;
        private ViewPager mViewPager;
        public SectionsPagerAdapter(FragmentManager fm, Context context, ViewPager inViewPager) {
            super(fm);
            appContex = context;
            mViewPager = inViewPager;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(appContex, position + 1, mViewPager);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PENDING ORDER";
                case 1:
                    return "COMPLETED ORDER";
            }
            return null;
        }
    }
}
