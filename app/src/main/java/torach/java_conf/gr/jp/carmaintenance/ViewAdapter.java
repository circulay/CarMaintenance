package torach.java_conf.gr.jp.carmaintenance;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewHolder> {


    private ArrayList<ListItem> data;

    //コンストラクター
    public ViewAdapter(ArrayList<ListItem> data) {
        this.data = data;

    }


    //ビューホルだ生成
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_field, parent,false);
        return new ViewHolder(view);
    }

    //ビューにデータを割り当てる・リストの項目の生成
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ListItem dataSet = data.get(position);

        holder.category.setText(dataSet.getCategory());
        holder.date.setText(dataSet.getDate());
        holder.price.setText(dataSet.getPrice());
        holder.notes.setText(dataSet.getNotes());

    }

    //データ項目数を取得
    @Override
    public int getItemCount()
    {
        return this.data == null ? 0 : this.data.size();
    }



    public void updataList(ArrayList<ListItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

}
