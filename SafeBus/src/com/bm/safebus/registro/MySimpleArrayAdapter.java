package com.bm.safebus.registro;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bm.savebus.R;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  List<String> values;

  public MySimpleArrayAdapter(Context context, List<String> values) {
    super(context, R.layout.row_lista, values);
    this.context = context;
    this.values=values;
  }
  


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row_lista, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.row_lista_tv_info);
    textView.setText(values.get(position));
    textView.setTextColor(context.getResources().getColor(R.color.color_vivos));

    return rowView;
  }
} 