package hashbird.com.newapp.Helper;

/**
 * Created by Chandan on 10/21/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hashbird.com.newapp.R;

/**
 * Created by csa on 3/1/2017.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.Myholder> {
    List<DataModel> dataModelArrayList;
Security security;
    public RecycleAdapter(List<DataModel> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView name,email,phone,dob;

        public Myholder(View itemView) {
            super(itemView);
            security = new Security();
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            phone = (TextView) itemView.findViewById(R.id.phone);
            dob = (TextView) itemView.findViewById(R.id.dob);
        }
    }


    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview,null);
        return new Myholder(view);

    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        DataModel dataModel=dataModelArrayList.get(position);
        try {
            holder.name.setText(dataModel.getname());
            holder.email.setText(dataModel.getemail());
            holder.phone.setText(dataModel.getphone());
            holder.dob.setText(dataModel.getdob());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
}