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
 * Java code for an individual message in the messaging part of the app.
 *
 * @author Brian Kwak
 */
public class MessageRowUI extends LinearLayout {
    public ImageView imageView;
    public TextView name;
    public TextView message;
    public LinearLayout messageBg;
    public LinearLayout content;

    /**
     * Inflates the layout from the corresponding XML to the context.
     * @param context The context.
     */
    private void inflateLayout(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.message_row, this);

        this.imageView = view.findViewById(R.id.imageView);
        this.name = view.findViewById(R.id.name);
        this.message = view.findViewById(R.id.message);
        this.messageBg = view.findViewById(R.id.messageBackground);
        this.content = view.findViewById(R.id.content);
    }

    /**
     * Constructor with attributes.
     * @param context The context.
     * @param attrs The attributes.
     */
    public MessageRowUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout(context);
    }

    /**
     * Constructor.
     * @param context The context.
     */
    public MessageRowUI(Context context) {
        super(context);
        inflateLayout(context);
    }


    /**
     * Constructor that allows the values to be from self.
     * @param context The context.
     * @param self Boolean to denote whether the message is sent by themselves.
     */
    public MessageRowUI(Context context, boolean self) {
        super(context);
        inflateLayout(context);
        if(self){
            setFromSelf();
        }
    }

    /**
     * Sets the graphical attributes to be from self, i.e. on the right side and in a different
     * color. Automatically called by a constructor with the given parameters.
     */
    public void setFromSelf(){
        messageBg.setBackgroundResource(R.drawable.message_self);
        content.setHorizontalGravity(Gravity.RIGHT);
        message.setTextColor(getResources().getColor(R.color.white));
        this.setHorizontalGravity(Gravity.RIGHT);

    }
}
