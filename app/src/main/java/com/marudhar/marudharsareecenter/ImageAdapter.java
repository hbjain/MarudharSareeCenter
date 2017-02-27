/**
 * Created by Hemant on 1/9/2017.
 */

package com.marudhar.marudharsareecenter;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;

public class ImageAdapter extends BaseAdapter {
    private Context contxt;
    String itemName;
    int count;

    public ImageAdapter(Context c, String name, int incount) {
        contxt = c;
        itemName = name;
        count = 1;
    }

    public int getCount() {
        return count;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup
            parent) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater)
                    contxt.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.stack_image, null, false);
            ContextWrapper contextWrapper = new ContextWrapper(contxt);
            //File directory = contextWrapper.getDir(GlobalVar.InventoryPhoto, Context.MODE_PRIVATE);

            File directory = new File(GlobalVar.InventoryPhoto);
            String picturePath = directory.toString() + "/" + itemName + String.valueOf((position+1));

            ImageView imageView = (ImageView) view.findViewById(R.id.stackImageView);

            GlobalVar.width = 600;
            GlobalVar.height = 600;
            GlobalVar.loadBitmap(contxt, picturePath,imageView);
            //BitmapWorkerTask task = new BitmapWorkerTask(imageView);

            //task.execute(picturePath);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            //imageView.setScaleType(ImageView.ScaleType.CENTER);
        }


        //imageView.setImageResource(images[position]);
        return view;
    }

}
