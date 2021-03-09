package com.example.fyp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.Activities.ViewMarketingComment;
import com.example.fyp.R;
import com.example.fyp.data.model.ModelMarketing;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MarketingAdapter extends RecyclerView.Adapter<MarketingAdapter.MyViewHolder> {

    //reference https://github.com/bikashthapa01/firebase-authentication-android
    //refernce https://www.youtube.com/watch?v=pAhYEy6s9wQ
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView fullName;
    String newvariablewithcurrentuserid2;
    String fullname;


    //reference https://drive.google.com/file/d/1LTxVNNs-fnD4tIADfqVllZztUFZe3KNl/view
    //reference https://www.youtube.com/watch?v=yPJ_5ybLkFo&list=PLhhNsarqV6MQ-eMvAOwjuBUDm7hfsTUta&index=10
    private ViewMarketingComment activity;
    private List<ModelMarketing> mList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public MarketingAdapter(ViewMarketingComment activity , List<ModelMarketing> mList){
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MarketingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item , parent , false);
        return new MarketingAdapter.MyViewHolder(v);



    }

    //reference
    //https://www.youtube.com/watch?v=eSwRpMugwgA&list=PLhhNsarqV6MQ-eMvAOwjuBUDm7hfsTUta&index=12
    public void deleteDataMarketing(int position) {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        newvariablewithcurrentuserid2 = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("users").document(newvariablewithcurrentuserid2);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //fullName.setText(document.getString("fName"));

                        newvariablewithcurrentuserid2 = document.getString("fName");

                        if (mList.get(position).getId().equals(newvariablewithcurrentuserid2)) {

                            ModelMarketing item = mList.get(position);
                            db.collection("Marketing Comments").document(item.getId()).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                notifyRemoved(position);
                                                Toast.makeText(activity, "Data Deleted !!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(activity, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }

                    }
                }
            }
        });

    }


        //reference
        //https://www.youtube.com/watch?v=eSwRpMugwgA&list=PLhhNsarqV6MQ-eMvAOwjuBUDm7hfsTUta&index=12
        private void notifyRemoved(int position){

            mList.remove(position);
            notifyItemRemoved(position);
            activity.showDataMarketing();
        }



        @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.desc.setText(mList.get(position).getDesc());
        holder.id.setText(mList.get(position).getId());
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title , desc, id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            desc = itemView.findViewById(R.id.desc_text);
            id = itemView.findViewById(R.id.id_text);
        }
    }

}