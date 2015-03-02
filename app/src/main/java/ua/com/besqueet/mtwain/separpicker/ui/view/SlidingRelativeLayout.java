package ua.com.besqueet.mtwain.separpicker.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SlidingRelativeLayout extends RelativeLayout {

    public SlidingRelativeLayout(Context context) {
        super(context);
    }

    public SlidingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setYFraction(final float fraction) {
        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }

    public float getYFraction() {
        if (getHeight() == 0) {
            return 0;
        }
        return getTranslationY() / getHeight();
    }

    public float getXFraction() {
        return getX() / getWidth();
    }

    public void setXFraction(float xFraction) {
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }

}
