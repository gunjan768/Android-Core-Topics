package com.example.pakagemanager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    ArrayList<ApplicationInfo> appList;
    Context context;
    private PackageManager packageManager;

    public interface UninstallApplicationListener
    {
        void uninstallApp(ApplicationInfo data, int index);
    }

    UninstallApplicationListener uninstallApplicationListener;

    public RecyclerViewAdapter(Context context, ArrayList<ApplicationInfo> appList)
    {
        this.context = context;
        this.appList = appList;
        packageManager = context.getPackageManager();

        uninstallApplicationListener = (UninstallApplicationListener) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.my_row, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        final ApplicationInfo data = appList.get(position);

        if(data != null)
        {
            holder.appName.setText(data.loadLabel(packageManager));
            holder.appPackage.setText(data.packageName);
            holder.appIcon.setImageDrawable(data.loadIcon(packageManager));

            holder.appName.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = packageManager.getLaunchIntentForPackage(data.packageName);

                    if(intent != null)
                    {
                        context.startActivity(intent);
                    }
                }
            });

            holder.uninstall.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    uninstallApplicationListener.uninstallApp(data, position);
                }
            });
        }
    }

    @Override
    public long getItemId(int position)
    {

        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount()
    {
        return appList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView appName, appPackage;
        ImageView appIcon;
        Button uninstall;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            appName = itemView.findViewById(R.id.app_name);
            appPackage = itemView.findViewById(R.id.app_package);
            appIcon = itemView.findViewById(R.id.app_icon);
            uninstall = itemView.findViewById(R.id.uninstall_button);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}