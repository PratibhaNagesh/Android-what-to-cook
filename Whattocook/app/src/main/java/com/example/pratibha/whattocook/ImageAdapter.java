package com.example.pratibha.whattocook;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Pratibha on 12/1/2015.
 */
class ImageAdapter extends ArrayAdapter
{
    private Activity context;
    private String[] items;
    private boolean[] arrows;
    private int layoutId;
    private int textId;
    private int imageId;

    ImageAdapter(Activity context, int layoutId, int textId, int imageId, String[] items, boolean[] arrows)
    {
        super(context, layoutId, items);

        this.context = context;
        this.items = items;
        this.arrows = arrows;
        this.layoutId = layoutId;
        this.textId = textId;
        this.imageId = imageId;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View row=inflater.inflate(layoutId, null);
        TextView label=(TextView)row.findViewById(textId);

        label.setText(items[pos]);

        if (arrows[pos])
        {
            ImageView icon=(ImageView)row.findViewById(imageId);
            icon.setImageResource(R.drawable.arrow);
        }

        return(row);
    }

}
