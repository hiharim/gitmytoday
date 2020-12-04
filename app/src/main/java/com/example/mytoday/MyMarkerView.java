package com.example.mytoday;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class MyMarkerView extends MarkerView {

    private TextView tvContent;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent=(TextView)findViewById(R.id.tvContent);
    }

    @SuppressLint("SetTextI18n")
    public void refreshContent(Entry e, Highlight highlight) {
        if(e instanceof CandleEntry){
            CandleEntry ce=(CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(),0,true));
        }else {
            tvContent.setText("" + Utils.formatNumber(e.getY(),0,true));
        }
        super.refreshContent(e,highlight);
    }

    public MPPointF getOffset(){
        return new MPPointF(-(getWidth()/2),-getHeight());
    }
}