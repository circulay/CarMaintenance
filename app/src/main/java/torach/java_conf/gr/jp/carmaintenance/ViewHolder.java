package torach.java_conf.gr.jp.carmaintenance;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    //long id;
    TextView category;
    TextView date;
    TextView price;
    TextView notes;

    ViewHolder(View itemView) {

        super(itemView);
        //this.id = itemView.setTag();
        this.category = itemView.findViewById(R.id.content_Data);
        this.date = itemView.findViewById(R.id.date_Data);
        this.price = itemView.findViewById(R.id.price_Data);
        this.notes = itemView.findViewById(R.id.notes_Data);

    }

}
