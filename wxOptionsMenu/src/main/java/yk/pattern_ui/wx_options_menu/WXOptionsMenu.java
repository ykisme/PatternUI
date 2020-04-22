package yk.pattern_ui.wx_options_menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.PopupWindowCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WXOptionsMenu implements AdapterView.OnItemClickListener {
    private final WXOptionsMenuDrawable wxOptionsMenuDrawable;
    private List<MenuItem> menus;
    private PopupWindow popupWindow;
    private View anchorView;
    private int width;
    private MenuAdapter menuAdapter;
    private Context context;
    //这个值根据微信截图计算得出，单位dp
    private final float DEFAULT_WINDOW_WIDTH_DP = 166.9f;
    private int anchorMenuItemId;
    private MenuClickListener menuClickListener;

    public void setMenuClickListener(MenuClickListener listener) {
        this.menuClickListener = listener;
    }

    public void setAnchorView(View view) {
        Objects.requireNonNull(view);
        anchorView = view;
        if (wxOptionsMenuDrawable != null) {
            int width = view.getWidth();
            wxOptionsMenuDrawable.setTriangleLeft(popupWindow.getWidth() - width / 2.f);
        }
    }

    public void setMenus(List<MenuItem> menus) {
        this.menus = menus;
        menuAdapter.notifyDataSetChanged();
    }

    public void setMenus(@NonNull int[] icons, @NonNull String[] titles) {
        List<MenuItem> menus = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            menus.add(new MenuItem(icons[i], titles[i]));
        }
        setMenus(menus);
    }

    public List<MenuItem> getMenus() {
        return menus;
    }

    private void setWindowWidth(int width) {
        this.width = width;
        popupWindow.setWidth(width);
    }

    public WXOptionsMenu(Context context) {
        this.context = context;
        popupWindow = new PopupWindow(context, null, R.style.WXMenuPopupWindow);
        View rootView = LayoutInflater.from(context).inflate(R.layout.wxmenu_popup_wx_menu, null);
        wxOptionsMenuDrawable = new WXOptionsMenuDrawable();
        wxOptionsMenuDrawable.setTriangleHeight(context.getResources().getDimension(R.dimen.wx_popup_menu_bg_triangle_height));
        wxOptionsMenuDrawable.setRoundRectRadius(context.getResources().getDimension(R.dimen.wx_popup_menu_bg_round_rect_radius));
        wxOptionsMenuDrawable.setColor(ContextCompat.getColor(context, R.color.wx_options_menu_bg_popup_window));
        rootView.setBackground(wxOptionsMenuDrawable);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        menuAdapter = new MenuAdapter();
        listView.setAdapter(menuAdapter);
        listView.setDivider(null);
        if (width == 0) {
            setWindowWidth(dp2px(context, DEFAULT_WINDOW_WIDTH_DP));
        }
        popupWindow.setContentView(rootView);
        popupWindow.setOutsideTouchable(true);
    }

    public void show() {
        if (!popupWindow.isShowing()) {
            if (anchorView != null) {
                PopupWindowCompat.showAsDropDown(popupWindow, anchorView, -context.getResources().getDimensionPixelSize(R.dimen.wx_popup_menu_margin_right),
                        context.getResources().getDimensionPixelSize(R.dimen.wx_popup_menu_margin_top), Gravity.BOTTOM | Gravity.RIGHT);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (menuClickListener != null) {
            menuClickListener.onItemClick(id);
        }
    }

    //这个方法应该在Activity的onOptionsItemSelected中被调用
    public void onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == this.anchorMenuItemId) {
            setAnchorView(((Activity) context).findViewById(anchorMenuItemId));
            show();
        }
    }

    public void setAnchorMenuItemId(int itemId) {
        anchorMenuItemId = itemId;
    }

    public void dismiss() {
        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    public static class MenuItem {
        int id = -1;
        int iconRes;
        int titleRes = -1;
        CharSequence title;

        public MenuItem(int iconRes, int titleRes) {
            this.iconRes = iconRes;
            this.titleRes = titleRes;
        }

        public MenuItem(int iconRes, CharSequence title) {
            this.iconRes = iconRes;
            this.title = title;
        }

        public MenuItem(int id, int iconRes, CharSequence title) {
            this(iconRes, title);
            this.id = id;
        }

        public MenuItem(int id, int iconRes, int titleRes) {
            this(iconRes, titleRes);
            this.id = id;
        }
    }

    int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }

    class MenuAdapter extends BaseAdapter {
        MenuAdapter() {
        }

        @Override
        public int getCount() {
            return menus == null ? 0 : menus.size();
        }

        @Override
        public Object getItem(int position) {
            return menus == null ? null : menus.get(position);
        }

        @Override
        public long getItemId(int position) {
            int id = menus.get(position).id;
            return id >= 0 ? id : position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.wxmenu_item_menu, parent, false);
            }
            MenuItem menuItem = (MenuItem) getItem(position);
            ImageView icon = convertView.findViewById(R.id.icon);
            icon.setImageDrawable(context.getDrawable(menuItem.iconRes));
            TextView title = convertView.findViewById(R.id.title);
            title.setTextColor(Color.WHITE);
            title.setText(TextUtils.isEmpty(menuItem.title) ? context.getString(menuItem.titleRes) : menuItem.title);
            if (position == getCount() - 1) {
                convertView.findViewById(R.id.divider).setVisibility(View.GONE);
            } else {
                convertView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    public interface MenuClickListener {
        void onItemClick(long id);
    }
}
