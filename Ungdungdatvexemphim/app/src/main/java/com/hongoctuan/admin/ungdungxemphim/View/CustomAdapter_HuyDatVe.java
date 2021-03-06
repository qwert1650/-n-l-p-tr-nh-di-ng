package com.hongoctuan.admin.ungdungxemphim.View;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongoctuan.admin.ungdungxemphim.DTO.AccountDTO;
import com.hongoctuan.admin.ungdungxemphim.DTO.VeDTO;
import com.hongoctuan.admin.ungdungxemphim.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by admin on 6/7/2016.
 */
public class CustomAdapter_HuyDatVe extends ArrayAdapter<VeDTO> {

    Activity context;
    ArrayList<VeDTO> objects;
    public CustomAdapter_HuyDatVe(Activity context, int resource, ArrayList<VeDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.customadapter_huydatve, null);
        }
        TextView txt_vetenphim = (TextView) view.findViewById(R.id.txt_vetenphim);
        TextView txt_vegiochieu = (TextView) view.findViewById(R.id.txt_vegiochieu);
        TextView txt_vekythuat = (TextView) view.findViewById(R.id.txt_vekythuat);
        TextView txt_vemaghe = (TextView) view.findViewById(R.id.txt_vemaghe);
        TextView txt_vephong = (TextView) view.findViewById(R.id.txt_vephong);
        TextView txt_verap = (TextView) view.findViewById(R.id.txt_verap);
        //gán danh các thông tin cho các control.
        txt_vegiochieu.setText(" - Giờ chiếu: "+objects.get(position).getThoigian().toString());
        txt_vekythuat.setText(" - Loại Phim: "+objects.get(position).getKythuat().toString()+"D");
        txt_vemaghe.setText("Ghế: "+objects.get(position).getMaghe().toString());
        txt_vephong.setText(" - Phòng: "+objects.get(position).getPhong().toString());
        txt_verap.setText("Rạp: "+objects.get(position).getTenrap().toString());
        txt_vetenphim.setText("Phim: "+objects.get(position).getPhim().toString());
        return view;
    }
}

