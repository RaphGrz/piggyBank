package com.piggybank.outils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.piggybank.R;

public class AdapterTirelire extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater; //instancie layout xml à un objet/view
    private String[] tirelire;

    public AdapterTirelire(Context context, String[] tirelire)
    {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tirelire = tirelire;
    }

    /**
     *
     * @return le nombre de tirelire
     */
    @Override
    public int getCount() {
        return tirelire.length;
    }

    /**
     *
     * @param position de la tirelire
     * @return le nom de la tirelire
     */
    @Override
    public Object getItem(int position) {
        return tirelire[position];
    }

    /**
     *
     * @param i
     * @return i
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * attribue au textview le nom de la tirelire
     * @param position de la tirelire
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.line_tirelire, null);

        TextView line_textview_tirelire = convertView.findViewById(R.id.line_textview_tirelire);
        line_textview_tirelire.setText(tirelire[position]);
        return convertView;
    }
}
