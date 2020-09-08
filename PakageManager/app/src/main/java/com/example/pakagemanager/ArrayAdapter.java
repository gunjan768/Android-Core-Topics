package com.example.pakagemanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArrayAdapter extends android.widget.ArrayAdapter<ApplicationInfo>
{
    private List<ApplicationInfo> appList = null;
    private Context context;
    private PackageManager packageManager;

    public ArrayAdapter(@NonNull Context context, int resource, @NonNull List<ApplicationInfo> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.appList = objects;
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount()
    {
        return ((appList != null) ? appList.size() : 0);
    }

    @Nullable
    @Override
    public ApplicationInfo getItem(int position)
    {
        return ((appList != null) ? appList.get(position) : null);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view = convertView;

        if(null == view)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.list_item, null);
        }

        ApplicationInfo data = appList.get(position);

        if(data != null)
        {
            TextView appName = view.findViewById(R.id.app_name);
            TextView packageName = view.findViewById(R.id.app_package);
            ImageView iconView = view.findViewById(R.id.app_icon);

            appName.setText(data.loadLabel(packageManager));
            packageName.setText(data.packageName);
            iconView.setImageDrawable(data.loadIcon(packageManager));
        }

        return view;
    }
}
