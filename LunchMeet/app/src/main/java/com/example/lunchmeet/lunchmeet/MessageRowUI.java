package com.example.lunchmeet.lunchmeet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Brian on 12/13/2017.
 */

public class MessageRowUI extends LinearLayout {
    public ImageView imageView;
    public TextView name;
    public TextView message;
    public LinearLayout messageBg;
    public LinearLayout content;
    private void inflateLayout(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.message_row, this);

        this.imageView = view.findViewById(R.id.imageView);
        this.name = view.findViewById(R.id.name);
        this.message = view.findViewById(R.id.message);
        this.messageBg = view.findViewById(R.id.messageBackground);
        this.content = view.findViewById(R.id.content);
    }
    public MessageRowUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout(context);
    }

    public MessageRowUI(Context context) {
        super(context);
        inflateLayout(context);
    }

    public void setFromSelf(){
        messageBg.setBackgroundResource(R.drawable.message_self);
        content.setHorizontalGravity(Gravity.RIGHT);
        message.setTextColor(getResources().getColor(R.color.white));
        this.setHorizontalGravity(Gravity.RIGHT);

    }
}
