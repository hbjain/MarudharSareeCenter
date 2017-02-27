/**
 * Created by Hemant on 1/9/2017.
 */

package com.marudhar.marudharsareecenter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class GridViewImageAdapter extends BaseAdapter {
    private Context contxt;
    String [] itemName;
    String [] itemQty;
    String customerName;
    int itemCount;
    GridViewImageAdapter adapter = this;



    public GridViewImageAdapter(Context c, String [] inItemName, String [] inItemQty, String inCustomerName, int inCount) {
        contxt = c;
        itemName = inItemName;
        itemQty = inItemQty;
        customerName = inCustomerName;
        itemCount = inCount;
    }

    public int getCount() {
        return itemCount;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        ImageView imageView;
        TextView textview;

        ContextWrapper contextWrapper = new ContextWrapper(contxt);
        //File directory = contextWrapper.getDir(GlobalVar.InventoryPhoto, Context.MODE_PRIVATE);

        File directory = new File(GlobalVar.InventoryPhoto);

        String picturePath = directory.toString() + "/" + itemName[position] + "1";
        Log.d("GridView", Integer.toString(position) + " " + picturePath);

        if (view == null) {

            view = LayoutInflater.from(contxt).inflate(R.layout.order_scroll_item, null);
        }

        textview = (TextView)view.findViewById(R.id.title123);
        textview.setText(itemName[position]);
        imageView = (ImageView)view.findViewById(R.id.image123);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setPadding(8, 8, 8, 8);

        GlobalVar.width = 300;
        GlobalVar.height = 300;
        GlobalVar.loadBitmap(contxt, picturePath,imageView);

        Button remove = (Button)view.findViewById(R.id.button123);
        remove.setTag(position);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start = ((int) v.getTag());

                itemName[start] = itemName[itemCount - 1];
                itemQty[start] = itemQty[itemCount - 1];
                itemCount = itemCount - 1;
                if(itemCount == 0)
                {
                    NavUtils.navigateUpTo((Activity)contxt, new Intent(contxt, Store.class));
                    //navigateUpTo(new Intent(contxt, Store.class))
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public String [] getItemName() {
        return itemName;
    }

    public String [] getItemQty() {
        return itemQty;
    }

    public int getItemCount() {
        return itemCount;
    }

}
