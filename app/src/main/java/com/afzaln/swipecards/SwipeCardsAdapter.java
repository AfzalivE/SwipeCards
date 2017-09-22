package com.afzaln.swipecards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afzaln.swipecards.data.Business;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by afzal on 2017-09-20.
 */

public class SwipeCardsAdapter extends BaseAdapter {

    private List<Business> list;

    public SwipeCardsAdapter(List<Business> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Business getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Timber.d("Open for: " + position);
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.bind(list.get(position), position);

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.name)
        TextView tvName;

        @BindView(R.id.photo)
        ImageView ivPhoto;

        @BindView(R.id.rating)
        TextView ratingTv;

        @BindView(R.id.price)
        TextView priceTv;

        private View view;

        public ViewHolder(View view) {
            this.view = view;
            ButterKnife.bind(this, view);
        }

        public void bind(Business business, int position) {
            Picasso.with(view.getContext())
                    .load(business.photos.get(0))
                    .fit()
                    .centerCrop()
                    .into(ivPhoto);

            tvName.setText(business.name + position);
            ratingTv.setText(view.getResources().getQuantityString(R.plurals.stars, Double.valueOf(business.rating).intValue(), business.rating));
            priceTv.setText(business.price);
        }
    }
}
