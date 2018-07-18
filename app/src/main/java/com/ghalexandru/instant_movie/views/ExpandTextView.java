package com.ghalexandru.instant_movie.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * A {@link TextView} that will expand or collapse when touched
 */
public class ExpandTextView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {

    private static final int MAX_MODE_LIST = 1;

    /**
     * Max lines from init of @textview
     */
    private final int maxLines;

    public ExpandTextView(Context context) {
        super(context);
        maxLines = this.getMaxLines();

        setOnClickListener(this);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        maxLines = this.getMaxLines();

        setOnClickListener(this);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.maxLines = this.getMaxLines();

        setOnClickListener(this);
    }

    /**
     * Get current max lines
     */
    @Override
    public int getMaxLines() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            return super.getMaxLines();

        try {
            final Field maxMode = TextView.class.getField("maxMode");
            maxMode.setAccessible(true);
            final Field maximum = TextView.class.getField("maximum");
            maximum.setAccessible(true);

            final int maxModeValue = (int) maxMode.get(this);
            final int maximumValue = (int) maximum.get(this);

            return maxModeValue == MAX_MODE_LIST ? maximumValue : -1;
        } catch (final Exception e) {
            return -1;
        }
    }

    /**
     * Expand or collapse
     */
    @Override
    public void onClick(View v) {

        clearAnimation();

        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        animation.setDuration(100);
        animation.setInterpolator(new AccelerateInterpolator(1.0f));

        startAnimation(animation);

        if (getMaxLines() != maxLines)
            setMaxLines(maxLines);
        else
            setMaxLines(Integer.MAX_VALUE);

    }
}
