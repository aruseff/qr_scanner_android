package training.ruseff.com.karateqrscanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import training.ruseff.com.karateqrscanner.R;
import training.ruseff.com.karateqrscanner.models.User;

public class UsersListAdapter extends ArrayAdapter<User> implements Filterable {

    private int lastPosition = -1;

    private ArrayList<User> originalData;
    private ArrayList<User> filteredData;
    private Context mContext;
    private ItemFilter mFilter = new ItemFilter();

    private static class ViewHolder {
        TextView nameTextView;
        TextView externalIdTextView;
    }

    public UsersListAdapter(ArrayList<User> data, Context context) {
        super(context, android.R.layout.simple_list_item_1, data);
        this.originalData = data;
        this.filteredData = data;
        this.mContext = context;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public User getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void remove(User item) {
        filteredData.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User dataModel = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_list_item, parent, false);
            viewHolder.nameTextView = convertView.findViewById(R.id.name);
            viewHolder.externalIdTextView = convertView.findViewById(R.id.external_id);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.nameTextView.setText(dataModel.getName());
        viewHolder.externalIdTextView.setText(String.valueOf(dataModel.getExternalId()));
        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<User> list = originalData;
            int count = list.size();
            final ArrayList<User> nlist = new ArrayList<>(count);
            User filterableUser;
            for (int i = 0; i < count; i++) {
                filterableUser = list.get(i);
                if (filterableUser.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableUser);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("payment_unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }

    }
}
