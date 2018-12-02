package sk.finishersapps.finisher.lovemydogo.model.view_objects;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sk.finishersapps.finisher.lovemydogo.R;
import sk.finishersapps.finisher.lovemydogo.model.data_objects.GenericMapObject;

/**
 * Created by Filip on 26.11.2017.
 * My custom view to show Map object into layout.
 */

public class ComponentView extends RelativeLayout {

    private Context context;
    private TextView mNameText = null;
    private TextView mDistanceText = null;
    private Button mNavigateButton = null;

    private LinearLayout mTextsLayout = null;

    public ComponentView(Context context) {
        super(context);
        RelativeLayout thisLayout = (RelativeLayout) inflate(context, R.layout.component_view, this);
    }

    public ComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RelativeLayout thisLayout = (RelativeLayout) inflate(context, R.layout.component_view, this);
    }

    public ComponentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RelativeLayout thisLayout = (RelativeLayout) inflate(context, R.layout.component_view, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ComponentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        RelativeLayout thisLayout = (RelativeLayout) inflate(context, R.layout.component_view, this);
    }

    public ComponentView load() {

        mTextsLayout = (LinearLayout) findViewById(R.id.cvTextLayout);

        mNameText = (TextView) findViewById(R.id.cvNameText);

        mDistanceText = (TextView) findViewById(R.id.cvDistanceText);

        mNavigateButton = (Button) findViewById(R.id.cvNavigateButton);


        return this;
    }

    public ComponentView setPositions(DisplayMetrics displayMetrics) {
//        setViewsPosition(mNameText, 0f, 0f, 0.7f, 0.1f, displayMetrics);
        mTextsLayout.getLayoutParams().width = (int) (displayMetrics.widthPixels * 0.7f);

        mNameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMetrics.widthPixels * 0.05f);

        mDistanceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMetrics.widthPixels * 0.04f);

        mNavigateButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMetrics.heightPixels * 0.025f);
        setViewsPosition(mNavigateButton, 0.7f, 0f, 0.2f, 0.06f, displayMetrics);
        return this;
    }

    private void setViewsPosition(View view, float x, float y, float width, float height, DisplayMetrics displayResolution) {
        view.setX(displayResolution.widthPixels * x);
        view.setY(displayResolution.heightPixels * y);
        view.getLayoutParams().width = Math.round(displayResolution.widthPixels * width);
        view.getLayoutParams().height = Math.round(displayResolution.heightPixels * height);
    }

    public ComponentView setContent(GenericMapObject data) {
        double distance = data.calculateDistance(0, 0, 0);
        mNameText.setText(data.getName());
        mDistanceText.setText(distance + " meters");
        distance = data.calculateDistance(1, 1, 0);
        mDistanceText.setText(((int) data.getDistance()) + " meters");
        return this;
    }


    public void setNavigationListener(OnClickListener navigationListener) {
        mNavigateButton.setOnClickListener(navigationListener);
        this.setOnClickListener(navigationListener);
    }
}
