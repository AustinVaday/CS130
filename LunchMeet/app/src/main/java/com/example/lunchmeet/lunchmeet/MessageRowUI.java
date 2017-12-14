package com.example.lunchmeet.lunchmeet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Brian on 12/13/2017.
 */

public class MessageRowUI extends TableRow {
    public TextView name;
    public TextView message;
    private void inflateLayout(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.message_row, this);

        this.name = view.findViewById(R.id.name);
        this.message = view.findViewById(R.id.message);
    }
    public MessageRowUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout(context);
    }

    public MessageRowUI(Context context) {
        super(context);
        inflateLayout(context);
    }
}
