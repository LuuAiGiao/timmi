package cf20.soft.shoes.timmi.ui.home;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    public SpacesItemDecoration(int space) {
        this.top = space;
        this.right = space;
        this.bottom = space;
        this.left = space;
    }

    public SpacesItemDecoration(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = right;
        outRect.bottom = bottom;
        outRect.top = top;
        outRect.left = left;

        int itemPosition = parent.getChildAdapterPosition(view);

        if (itemPosition % 2 == 0) {
            outRect.left = left * 2;
            outRect.right = right;
        } else {
            outRect.right = right * 2;
            outRect.left = left;
        }

        outRect.bottom = bottom;
        outRect.top = top;

    }
}
