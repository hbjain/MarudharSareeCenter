package com.marudhar.marudharsareecenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by Hemant on 1/31/2017.
 */

class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private String data = "";
    private int height;
    private int width;
    private Context ctxt;

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }

        public static boolean cancelPotentialWork(String data, ImageView imageView) {
            final BitmapWorkerTask bitmapWorkerTask = BitmapWorkerTask.getBitmapWorkerTask(imageView);

            if (bitmapWorkerTask != null) {
                final String bitmapData = bitmapWorkerTask.data;
                // If bitmapData is not yet set or it differs from the new data
                if (bitmapData == "" || (bitmapData.equals(data) == false)) {
                    // Cancel previous task
                    bitmapWorkerTask.cancel(true);
                }
                else {
                    // The same work is already in progress
                    return false;
                }
            }
            // No task associated with the ImageView, or an existing task was cancelled
            return true;
        }
    }

    public BitmapWorkerTask(ImageView imageView, Context context) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        ctxt = context;
        ViewGroup.LayoutParams param = imageView.getLayoutParams();
        if(param.width == -1 || param.height == -1) {
            width = 800;
            height = 800;
        }
        else
        {
            width = param.width;
            height = param.height;

        }
    }

     // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];

        int reqWidth =  width;
        int reqHeight = height;
        return GlobalVar.decodeSampledBitmapFromResource(data, reqWidth, reqHeight);
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null && bitmap != null){
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setPadding(8, 8, 8, 8);
                imageView.setImageBitmap(bitmap);
                imageView.setBackgroundResource(R.drawable.imageborder);

                View parentView = (View) imageView.getParent();
                parentView.setTag(R.id.OrderBookImageVisible, 0);

                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        ImageView baseImage = (ImageView) v;


                        View a = (View) baseImage.getParent();

                        int vid = a.getId();

                        if (vid == R.id.orderBookingRelLandScape)
                        {

                            RelativeLayout parentView = (RelativeLayout) baseImage.getParent();
                            ViewPager mViewPager = (ViewPager) parentView.getParent();
                            mViewPager.setTag(true);

                            ViewGroup.LayoutParams tmp = mViewPager.getLayoutParams();
                            ((CoordinatorLayout.LayoutParams) tmp).setMargins(0, 1, 0, 0);

                            int count = parentView.getChildCount();

                            parentView.setTag(R.id.OrderBookImageVisible, 1);
                            for (int i = 0; i < count; i++) {
                                parentView.getChildAt(i).setVisibility(View.INVISIBLE);
                            }



                            //RelativeLayout.LayoutParams parentParams = (RelativeLayout.LayoutParams)parentView.getLayoutParams();


                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                            ZoomImageView img = new ZoomImageView(ctxt);

                            //img.setLayoutParams(params);
                            img.setLayoutParams(params);
                            img.setBackgroundResource(R.drawable.imageborder);
                            img.setTag(R.id.KEY_1, parentView);
                            img.setTag(R.id.KEY_2, mViewPager);
                            img.setImageBitmap(BitmapFactory.decodeFile((String) baseImage.getTag()));
                            img.setPadding(8, 8, 8, 8);
                            img.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v)
                                {

                                    ImageView img = (ImageView) v;


                                    RelativeLayout parentView = (RelativeLayout) img.getTag(R.id.KEY_1);
                                    ViewPager mViewPager = (ViewPager) img.getTag(R.id.KEY_2);
                                    mViewPager.setTag(false);
                                    parentView.setTag(R.id.OrderBookImageVisible, 0);
                                    ViewGroup.LayoutParams tmp = mViewPager.getLayoutParams();
                                    ((CoordinatorLayout.LayoutParams) tmp).setMargins(0, 50, 0, 0);

                                    int count = parentView.getChildCount();
                                    for (int i = 0; i < count; i++)
                                    {
                                        View tmp1 = parentView.getChildAt(i);
                                        if (!((tmp1.getId() == R.id.ItemPrice) || (tmp1.getId() == R.id.AddedToCartIcon)))
                                        {
                                            tmp1.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    parentView.removeView(img);

                                    return true;
                                }
                            });

                            parentView.addView(img);
                        }

                        return true;
                    }

                });
            }    //imageView.setRotation(90);
        }


   //     if (imageViewReference != null && bitmap != null) {
   //         final ImageView imageView = imageViewReference.get();
   //         if (imageView != null) {
   //             imageView.setImageBitmap(bitmap);
   //             imageView.setRotation(90);
   //         }
   //     }
    }
}
