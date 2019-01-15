package categorytab;

import android.content.Context;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import ooyo.mn.showme.R;

/**
 * Created by appleuser on 10/22/16.
 */

public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private final int[] Imageid;

    // Constructor
    public CategoryAdapter(Context c, int[] Imageid ){
        mContext = c;
        this.Imageid = Imageid;
    }

    @Override
    public int getCount() {
        return Imageid.length;
    }

    @Override
    public Object getItem(int position) {
        return Imageid[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.category_item, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.categoryItemImage);
            imageView.setImageResource(Imageid[position]);
            DisplayMetrics lDisplayMetrics = mContext.getResources().getDisplayMetrics();
            int height = lDisplayMetrics.heightPixels;
            int width = lDisplayMetrics.widthPixels;
            imageView.getLayoutParams().width = width / 3;
            imageView.getLayoutParams().height = width / 3;

            imageView.requestLayout();
        } else {
            grid = (View) convertView;
        }
        return grid;
    }

}