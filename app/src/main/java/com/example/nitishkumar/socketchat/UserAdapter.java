package com.example.nitishkumar.socketchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<User> listUser = new ArrayList<>();

    public UserAdapter(ArrayList<User> list) {
        this.listUser = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUser.get(position);

//        Glide.with(holder.itemView.getContext()).load(hero.getPhoto()).apply(new RequestOptions().override(55, 55)).into(holder.imgPhoto);

        holder.setvUsername(user.getUser());
    }

    @Override
    public int getItemCount() {return listUser.size();}

    public class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView vUsername;
        public View vView;

        public UserViewHolder(View itemView) {
            super(itemView);
            vUsername=(TextView)itemView.findViewById(R.id.contactname);
            vView=itemView;
        }

        public void setvUsername(String username){
            if(vUsername==null)
                return;
            vUsername.setText(username);
        }
    }
}
