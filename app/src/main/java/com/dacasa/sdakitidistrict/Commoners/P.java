package com.dacasa.sdakitidistrict.Commoners;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.dacasa.sdakitidistrict.POJOS.Chapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class P {

    public static final String bible_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SDA KITI DISTRICT/Bible/.bible";
    public static final String songs_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/PCEA/songs";
    public static final int VERSE_ONLY = 0,VERSE_IMAGE_MESSAGE = 1,MESSAGE = 2,MESSAGE_IMAGE = 3,VERSE_MESSAGE = 4,ANNOUNCEMENT_IMAGE = 5,ANNOUNCEMENT = 6,BULLETIN = 7,IMAGE_ONLY = 8;


    public static boolean bibleAvailable(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SDA KITI DISTRICT/Bible/bible.db");
        if (!file.exists()){
            return false;
        }
        if (file.length() > 5700000){
            return true;
        }
        return false;
    }

    public static Chapter chapterFromBundle(Bundle b){
        return new Chapter(b.getInt("b"),b.getInt("c"));
    }

    public static Bundle chapterToBundle(Chapter c){
        Bundle b = new Bundle();
        b.putInt("b",c.getBook());
        b.putInt("c",c.getChapter());
        return b;
    }

    public static void enterReveal(final View layout){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int cx = (layout.getRight()+layout.getLeft())/2;
            int cy = (layout.getTop()+layout.getBottom())/2;

            int finalRadius = Math.max(layout.getWidth(), layout.getHeight());

            Animator animator = ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, finalRadius);
            layout.setVisibility(View.VISIBLE);
            animator.setDuration(800);
            animator.start();
        }else{
            layout.setVisibility(View.VISIBLE);
        }
    }

    public static void exitReveal(final View layout,String  to){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int cx = 0;
            int cy = 0;
            if (to.equals("center")){
                cx = (layout.getRight()+layout.getLeft())/2;
                cy = (layout.getTop()+layout.getBottom())/2;
            }else if (to.equals("top")){
                cx = (layout.getRight()+layout.getLeft())/2;
                cy = (layout.getTop());
                layout.animate().alpha(0f).setDuration(500).setStartDelay(200);
            }else {
                cx = (layout.getRight()+layout.getLeft())/2;
                cy = (layout.getTop()+layout.getBottom())/2;
            }

            int finalRadius = Math.max(layout.getWidth(), layout.getHeight());

            Animator animator = ViewAnimationUtils.createCircularReveal(layout, cx, cy, finalRadius, 0);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layout.setVisibility(View.GONE);
                    layout.setAlpha(1f);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.setDuration(1000);
            animator.start();
        }else{
            layout.animate().alpha(0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layout.setVisibility(View.GONE);
                    layout.setAlpha(1f);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }




    public static String bytesToMB(long bytes){
        DecimalFormat df = new DecimalFormat("####0.0");
        return df.format((1.0*bytes)/(1024*1024));
    }

    public static double bytesToDouble(long bytes){
        return ((1.0*bytes)/(1024*1024));
    }

    public static boolean songExists(String name){
        File to_file = new File(P.songs_path+"/"+name+".mp3");
        return to_file.exists();
    }


    public static Bitmap bitmapR(Resources res, int resId, int reqWidth){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //checks the dimensions
        try {
            BitmapFactory.decodeResource(res, resId, options); //decodes without loading in memory
        }catch (Exception e){}
        options.inSampleSize = calculateSampleSize(options,reqWidth,reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodedBitmap(String filePath, int reqWidth, int reqHeight,int quality){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //checks the dimensions
        try {
            BitmapFactory.decodeFile(filePath, options); //decodes without loading in memory
        }catch (Exception e){}
        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap =  BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        if (bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bytearrayoutputstream);
        }else {
            return null;
        }

        byte[] BYTE = bytearrayoutputstream.toByteArray();

        return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
    }

    public static Bitmap bitmapFromView(ImageView imageView){
        try{
            return ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        }catch (Exception e){
            return null;
        }
    }

    public static int calculateSampleSize(BitmapFactory.Options options, int rw, int rh){
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > rw || height > rh){
            int halfH = height/2;
            int halfW = width/2;
            while ((halfH/inSampleSize)>rh && (halfW/inSampleSize)>rw){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static byte[] bytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
        return stream.toByteArray();
    }

    public static byte[] smallBitmap(byte[] bytes, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //checks the dimensions
        BitmapFactory.decodeByteArray(bytes,0,bytes.length,options); //decodes without loading in memory
        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options).compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap squareBitmap(Bitmap bitmap){
        int dim = Math.max(bitmap.getWidth(),bitmap.getHeight());
        Bitmap dest = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dest);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitmap, (dim - bitmap.getWidth()) / 2, (dim - bitmap.getHeight()) / 2, null);
        return dest;
    }

    public static Bitmap imageFromBytes(byte[] bytes,int rw){
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; //checks the dimensions
            BitmapFactory.decodeByteArray(bytes,0,bytes.length,options); //decodes without loading in memory
            options.inSampleSize = calculateSampleSize(options, rw, rw);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        }catch (Exception e){}
        return bitmap;
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }




}
