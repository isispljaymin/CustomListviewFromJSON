package com.isispl.customlistviewfromjson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.isispl.customlistviewfromjson.model.Flower;
import java.util.List;

/**
 * Created by Jaymin581 on 24/06/15.
 */
public class FlowerAdapter extends ArrayAdapter<Flower> {

    private Context context;
    private List<Flower> flowerList;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row_item, parent, false);

        // Display Flower name
        Flower flower = flowerList.get(position);

        TextView tv_item = (TextView)view.findViewById(R.id.row_item_txt);

        tv_item.setText(flower.getName());

        //Display image
        ImageView img = (ImageView)view.findViewById(R.id.row_item_img);
        img.setImageBitmap(flower.getBitmap());

        return view;

    }



}
