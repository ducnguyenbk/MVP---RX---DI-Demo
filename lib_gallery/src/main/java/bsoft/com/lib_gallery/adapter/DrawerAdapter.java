package bsoft.com.lib_gallery.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import bsoft.com.lib_gallery.R;
import bsoft.com.lib_gallery.model.DrawerModel;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
    private DisplayImageOptions options;
    private List<DrawerModel> drawerMenuList;
    private OnNavigationItemSelectedListener listener;

    public DrawerAdapter(List<DrawerModel> drawerMenuList) {
        this.drawerMenuList = drawerMenuList;
       // options = MyApplication.optionsCache();
    }

    public void setOnItemSelectedListener(OnNavigationItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item_layout, parent, false);
        return new DrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, final int position) {
        holder.title.setText(drawerMenuList.get(position).getTitle());
        String iconPath = drawerMenuList.get(position).iconPath;
        if (iconPath != null) {
            ImageLoader.getInstance().displayImage(String.valueOf("file:///" + iconPath), holder.icon);
        } else {
            //   holder.icon.setImageResource(R.mipmap.ic_launcher);
        }
        holder.number.setText("(" + drawerMenuList.get(position).total + ")");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNavigationItemSelected(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawerMenuList.size();
    }

    public interface OnNavigationItemSelectedListener {
        void onNavigationItemSelected(int position);
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        TextView number;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon_tab);
            number = (TextView) itemView.findViewById(R.id.number);

        }
    }


    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            final int itemPosition = parent.getChildAdapterPosition(view);
            if (itemPosition == 0) {
                outRect.top = mVerticalSpaceHeight;
            }
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}