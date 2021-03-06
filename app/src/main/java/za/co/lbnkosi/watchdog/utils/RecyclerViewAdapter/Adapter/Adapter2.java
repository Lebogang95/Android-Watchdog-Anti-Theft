package za.co.lbnkosi.watchdog.utils.RecyclerViewAdapter.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.utils.RecyclerViewAdapter.Model.Model2;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {

    private final List<Model2> itemList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        //each data item is just a string in this case
        final TextView MainHeader;
        final TextView SecondaryHeader;
        final ImageView BannerImage;

        ViewHolder(View v) {
            super(v);
            MainHeader = v.findViewById(R.id.title);
            SecondaryHeader = v.findViewById(R.id.description);
            BannerImage = v.findViewById(R.id.icon);
        }
    }

    public Adapter2(List<Model2> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item3,parent,false);

        return new ViewHolder(v);
    }

    //Replace the contents of a view (invoked by the layout manager
    @Override
    public void onBindViewHolder(@NonNull Adapter2.ViewHolder holder, int position) {

        // - get element from arraylist at this position
        // - replace the contents of the view with that element

        Model2 item = itemList.get(position);
        holder.MainHeader.setText(item.getMain_header());
        holder.SecondaryHeader.setText(item.getSecondary_header());
        holder.BannerImage.setImageResource(item.getBanner_image());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

