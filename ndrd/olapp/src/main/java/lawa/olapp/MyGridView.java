package lawa.olapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
en gridview som fungerar innanför en scrollview förutsatt att alla bilder är fyrkantiga och har samma dimension.
 */
public class MyGridView extends GridView {

  public MyGridView(Context context) {
    super(context);
  }

  public MyGridView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MyGridView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  
//http://stackoverflow.com/questions/29119869/my-gridview-shows-only-one-row
//http://stackoverflow.com/questions/7545915/gridview-rows-overlapping-how-to-make-row-height-fit-the-tallest-item
@Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	  ListAdapter adapter = (ListAdapter) getAdapter();
	  int numRows = adapter.getCount() / getNumColumns() + (adapter.getCount() % getNumColumns() > 0 ? 1 : 0);
    int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getColumnWidth() * numRows, MeasureSpec.EXACTLY);
    super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
  }
}
/*	  
    Drawable mDrawable = getDrawable();
    if (mDrawable != null) {
      int mDrawableWidth = mDrawable.getIntrinsicWidth();
      int mDrawableHeight = mDrawable.getIntrinsicHeight();
      float actualAspect = (float) mDrawableWidth / (float) mDrawableHeight;

      // Assuming the width is ok, so we calculate the height.
      final int actualWidth = MeasureSpec.getSize(widthMeasureSpec);
      final int height = (int) (actualWidth / actualAspect);
      heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
*/
