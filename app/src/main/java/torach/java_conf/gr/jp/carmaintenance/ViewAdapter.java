package torach.java_conf.gr.jp.carmaintenance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListItem dataSet = data.get(position);

        holder.category.setText(dataSet.getCategory());
        holder.date.setText(dataSet.getDate());
        holder.price.setText(dataSet.getPrice());
        holder.notes.setText(dataSet.getNotes());

        //クリックイベント
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int position = holder.getAdapterPosition();
                long id = getItemId(position);

                String obCategory = dataSet.getCategory();

                //Spinner spinner = (Spinner) layout.findViewByID(R.id.category_spinner);
                //String spinnerStr = (String) spinner.getSelectedItem();
                String obDate = dataSet.getDate();
                String obPrice = dataSet.getPrice();
                String obNotes = dataSet.getNotes();

                Context context = v.getContext();

                Intent intent = new Intent(context, EditMaintenanceData.class);
                intent.putExtra("iPos", id);
                intent.putExtra("iCategory", obCategory);
                intent.putExtra("iDate", obDate);
                intent.putExtra("iPrice", obPrice);
                intent.putExtra("iNotes", obNotes);
                context.startActivity(intent);
            }
        });
    }

    //データ項目数を取得
    @Override
    public int getItemCount()
    {
        return data == null ? 0 : data.size();
    }

   //該当のidを返す
    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }




    /*
    //リストのアイテム削除
    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }*/
}
