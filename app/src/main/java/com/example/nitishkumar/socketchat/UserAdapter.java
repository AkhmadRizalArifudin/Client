package com.example.nitishkumar.socketchat;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<User> listUser = new ArrayList<>();
    public ArrayList<User> targets = new ArrayList<>();

    public UserAdapter(ArrayList<User> list) {
        this.listUser = list;
    }
    public Activity mActivity;
//    public interface OnItemClickListener {
//        void onItemClick(User item);
//    }
//
//    private final OnItemClickListener listener;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user, parent, false);
        Activity activity = (Activity) parent.getContext();
        return new UserViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUser.get(position);

//        Glide.with(holder.itemView.getContext()).load(hero.getPhoto()).apply(new RequestOptions().override(55, 55)).into(holder.imgPhoto);

        holder.setvUsername(user.getUser());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view, mActivity, user);
            }
        });
    }

    private void showDialog(View view, Activity activity, User target){
        targets = new ArrayList<>();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

        // set title dialog
        alertDialogBuilder.setTitle("Mulai private chat?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Ya untuk mulai!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        targets.add(target);
                        Intent v=new Intent();
                        v.putExtra("targetID",targets.get(0).getID());
                        v.putExtra("targetName",targets.get(0).getUser());
                        activity.setResult(RESULT_OK,v);
                        activity.finish();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    @Override
    public int getItemCount() {return listUser.size();}

    public ArrayList<User> getTarget() {return targets;}

    public class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView vUsername;
        public View vView;


        public UserViewHolder(View itemView, Activity activity) {
            super(itemView);
            vUsername=(TextView)itemView.findViewById(R.id.contactname);
            vView=itemView;
            mActivity=activity;
        }

        public void setvUsername(String username){
            if(vUsername==null)
                return;
            vUsername.setText(username);
        }
    }
}
