package sk.finishersapps.finisher.lovemydogo.acitivities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sk.finishersapps.finisher.lovemydogo.R;
import sk.finishersapps.finisher.lovemydogo.model.Data;
import sk.finishersapps.finisher.lovemydogo.model.communication.AsyncResponse;
import sk.finishersapps.finisher.lovemydogo.model.communication.ServerAsyncTask;
import sk.finishersapps.finisher.lovemydogo.model.data_objects.GenericObject;
import sk.finishersapps.finisher.lovemydogo.model.view_objects.ComponentView;

/**
 * Handles all logic that is user doing in application.
 */
public class MainMenuActivity extends Activity {

    private static final String TAG = MainMenuActivity.class.getSimpleName();

    private DisplayMetrics displayResolution = null;
    private SeekBar mDistanceSeekBar = null;
    private TextView mDistanceText = null;
    private int mDistanceValue;
    private RelativeLayout mDistancePickerView = null;
    private RelativeLayout mResultsOuternLayout = null;
    private ProgressBar mResultsProgress = null;
    private Button mCheckResultsOnMapButton = null;
    private LinearLayout mResultsList = null;
    private ArrayList<GenericObject> mLastResultsList = null;
    private RelativeLayout mWayOfTransportPickerLayout = null;
    private GenericObject mLastPickedPoint = null;

    private RelativeLayout mBusOuternLayout = null;
    private TextView mStartingBusStop = null;
    private TextView mEndingBusStop = null;
    private ProgressBar mBusProgress = null;
    private RelativeLayout mBusDataLayout = null;
    private Button mBusNavigateButton = null;

    private FusedLocationProviderClient mFusedLocationClient;
    private int mLastType = 0;

    double mMyLatitude;
    double mMyLongitude;
    private GenericObject mStartBusStop = null;

    /**
     * Inits view and inits location.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initView();
        initLocation();
    }

    /**
     * Asks permission just because of warnings (user won't get here if he denies permission.
     * Inits location of user so I can use his location to find all things that require
     * users location (closest bus stop, points of interest in range)
     */
    private void initLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mMyLatitude = location.getLatitude();
                            mMyLongitude = location.getLongitude();
                            Log.d("location", location.getLatitude() + " " + location.getLongitude());
                            // Logic to handle location object
                        } else {
                            Log.d("location", "is null");
                        }
                    }
                });
    }

    /**
     * Inits all user interface components and sets their position on screen. It shouldn't be done
     * like this buuut this is fastest solution for responsibility of application (not best - on
     * every screen size app looks same)
     */
    private void initView() {
        displayResolution = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayResolution);

        Button vets = (Button) findViewById(R.id.mmaVets);
        setViewsPosition(vets, 0.25f, 0.45f, 0.5f, 0.06f);
        Button parks = (Button) findViewById(R.id.mmaParks);
        setViewsPosition(parks, 0.25f, 0.55f, 0.5f, 0.06f);
        Button stores = (Button) findViewById(R.id.mmaStores);
        setViewsPosition(stores, 0.25f, 0.65f, 0.5f, 0.06f);

        initDistanceLayout();

        initResultsLayout();

        initWayOfTransportPicker();

        initBusStopsLayout();
    }

    private void initBusStopsLayout() {
        mBusOuternLayout = (RelativeLayout) findViewById(R.id.mmaBusDataOuternLayout);

        mBusProgress = (ProgressBar) findViewById(R.id.mmaBusProgress);

        mBusDataLayout = (RelativeLayout) findViewById(R.id.mmaBusDataLayout);

        mStartingBusStop = (TextView) findViewById(R.id.mmaClosestLocalBusStopName);
        setViewsPosition(mStartingBusStop, 0.2f, 0.2f, 0.6f, 0.15f);
        mStartingBusStop.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayResolution.heightPixels * 0.025f);
        mEndingBusStop = (TextView) findViewById(R.id.mmaClosestDestinationBusStopName);
        setViewsPosition(mEndingBusStop, 0.2f, 0.35f, 0.6f, 0.15f);
        mEndingBusStop.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayResolution.heightPixels * 0.025f);

        mBusNavigateButton = (Button) findViewById(R.id.mmaBusNavigateButton);
        setViewsPosition(mBusNavigateButton, 0.2f, 0.55f, 0.6f, 0.1f);
//
        Button busCheckConnections = (Button) findViewById(R.id.mmaBusCheckConnectionsButton);
        setViewsPosition(busCheckConnections, 0.2f, 0.67f, 0.6f, 0.1f);

    }

    private void initWayOfTransportPicker() {
        mWayOfTransportPickerLayout = (RelativeLayout) findViewById(R.id.mmaWayOfTransportPickerOuternLayout);


        RelativeLayout wayOfTransportBackground = (RelativeLayout) findViewById(R.id.mmaWayOfTransportBackground);
        setViewsPosition(wayOfTransportBackground, 0.2f, 0.35f, 0.6f, 0.3f);

        TextView wayOfTransportHeader = (TextView) findViewById(R.id.mmaChooseWayOfTransportHeader);
        setViewsPosition(wayOfTransportHeader, 0.2f, 0.37f, 0.6f, 0.08f);
        wayOfTransportHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayResolution.heightPixels * 0.025f);


        Button byBusButton = (Button) findViewById(R.id.mmaBusButton);
        setViewsPosition(byBusButton, 0.27f, 0.45f, 0.22f, 0.06f);
        byBusButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayResolution.heightPixels * 0.02f);
        Button byCarButton = (Button) findViewById(R.id.mmaCarButton);
        byCarButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayResolution.heightPixels * 0.02f);
        setViewsPosition(byCarButton, 0.51f, 0.45f, 0.22f, 0.06f);

        Button walkBycicleButton = (Button) findViewById(R.id.mmaBicycleWalkButton);
        setViewsPosition(walkBycicleButton, 0.27f, 0.53f, 0.44f, 0.06f);
        walkBycicleButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayResolution.heightPixels * 0.02f);

    }

    private void initResultsLayout() {
        mResultsOuternLayout = (RelativeLayout) findViewById(R.id.mmaSearchResultsLayout);

        mResultsProgress = (ProgressBar) findViewById(R.id.mmaResultsProgressBar);

        ScrollView resultScroll = (ScrollView) findViewById(R.id.mmaResultsScroll);
        setViewsPosition(resultScroll, 0.05f, 0.1f, 0.9f, 0.75f);

        mResultsList = (LinearLayout) findViewById(R.id.mmaResultsListLayout);

        mCheckResultsOnMapButton = (Button) findViewById(R.id.mmaCheckResultsOnMapButton);
        setViewsPosition(mCheckResultsOnMapButton, 0.3f, 0.85f, 0.4f, 0.06f);

    }

    private void initDistanceLayout() {
        mDistancePickerView = (RelativeLayout) findViewById(R.id.mmaDistancePickerLayout);

        mDistanceSeekBar = (SeekBar) findViewById(R.id.mmaDistanceSeekBar);
        setViewsPosition(mDistanceSeekBar, 0.0f, 0.50f, 1f, 0.05f);

        mDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int distance = (10000 * progress) / 100;
                mDistanceValue = distance;
                String textIntoDistanceTextView = null;
                if (distance == 10000) {
                    textIntoDistanceTextView = "Unlimited distance";
                } else {
                    textIntoDistanceTextView = "Max distance " + distance + "m";
                }
                mDistanceText.setText(textIntoDistanceTextView);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mDistanceText = (TextView) findViewById(R.id.mmaDistanceTextView);
        setViewsPosition(mDistanceText, 0f, 0.25f, 1f, 0.1f);
        mDistanceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, displayResolution.heightPixels * 0.05f);

        Button confirmDistance = (Button) findViewById(R.id.mmaConfirmDistanceButton);
        setViewsPosition(confirmDistance, 0.3f, 0.75f, 0.4f, 0.06f);
    }


    private void setViewsPosition(View view, float x, float y, float width, float height) {
        view.setX(displayResolution.widthPixels * x);
        view.setY(displayResolution.heightPixels * y);
        view.getLayoutParams().width = Math.round(displayResolution.widthPixels * width);
        view.getLayoutParams().height = Math.round(displayResolution.heightPixels * height);
    }


    public void findVetsClicked(View view) {
        showDistancePicker(0);
    }

    private void showDistancePicker(int type) {
        mLastType = type;
        mDistancePickerView.setVisibility(View.VISIBLE);
        mDistanceText.setText("Max distance " + 5000);
        mDistanceSeekBar.setProgress(50);
        mDistanceValue = 5000;
    }

    public void findParksClicked(View view) {
//        showDistancePicker(1);
        mLastType = 1;
        searchForResults();
    }

    public void findStoresClicked(View view) {
        showDistancePicker(2);
    }

    public void emptyClicker(View view) {

    }

    @Override
    public void onBackPressed() {
        if (mBusOuternLayout.getVisibility() == View.VISIBLE) {
            mBusOuternLayout.setVisibility(View.INVISIBLE);
        } else if (mWayOfTransportPickerLayout.getVisibility() == View.VISIBLE) {
            mWayOfTransportPickerLayout.setVisibility(View.INVISIBLE);
        } else if (mResultsOuternLayout.getVisibility() == View.VISIBLE) {
            mResultsOuternLayout.setVisibility(View.INVISIBLE);
        } else if (mDistancePickerView.getVisibility() == View.VISIBLE) {
            mDistancePickerView.setVisibility(View.INVISIBLE);
        } else {
            finish();
        }
    }

    public void searchClicked(View view) {
        searchForResults();
    }

    private void searchForResults() {
        mResultsProgress.setVisibility(View.VISIBLE);
        mCheckResultsOnMapButton.setVisibility(View.INVISIBLE);
        mResultsList.removeAllViews();
        mResultsOuternLayout.setVisibility(View.VISIBLE);
        mDistancePickerView.setVisibility(View.INVISIBLE);
        search();
    }

    private void search() {
        ServerAsyncTask at = new ServerAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(String result) {
                mResultsProgress.setVisibility(View.INVISIBLE);
                mCheckResultsOnMapButton.setVisibility(View.VISIBLE);
                if (result != null) {
                    processSearchResult(result);
                } else {
                    String fakeResults = "[{\"xCoordinate\":17.1046652,\"yCoordinate\":48.146617,\"name\":\"VET Line\",\"distance\":318.03138252},{\"xCoordinate\":17.1200912,\"yCoordinate\":48.1522315,\"name\":\"Bullypet\",\"distance\":1003.37201996},{\"xCoordinate\":17.0948137,\"yCoordinate\":48.1440285,\"name\":\"Veterinárna poliklinika Sibra\",\"distance\":1088.374011},{\"xCoordinate\":17.114711209567968,\"yCoordinate\":48.157711445193506,\"name\":\"VetLINE\",\"distance\":1138.11504816},{\"xCoordinate\":17.1333238,\"yCoordinate\":48.1490953,\"name\":\"Veterinka\",\"distance\":1903.90962079},{\"xCoordinate\":17.1307225,\"yCoordinate\":48.1594336,\"name\":\"Venusta's\",\"distance\":2091.31620454},{\"xCoordinate\":17.0996858,\"yCoordinate\":48.127096,\"name\":\"Vet\",\"distance\":2465.01860401},{\"xCoordinate\":17.1382133,\"yCoordinate\":48.1594174,\"name\":\"Veterinárna poliklinika\",\"distance\":2566.18134932},{\"xCoordinate\":17.1108018,\"yCoordinate\":48.1214658,\"name\":\"VetPoint\",\"distance\":3025.45949012},{\"xCoordinate\":17.137208313871692,\"yCoordinate\":48.168644365300594,\"name\":\"Veterinárna ambulancia MVDr. Ladislav Šranko\",\"distance\":3126.06205531},{\"xCoordinate\":17.1316745,\"yCoordinate\":48.1205144,\"name\":\"Animavet\",\"distance\":3594.82410858},{\"xCoordinate\":17.1337447,\"yCoordinate\":48.1208905,\"name\":\"VetAnet\",\"distance\":3638.11289238},{\"xCoordinate\":17.1556614,\"yCoordinate\":48.1598116,\"name\":\"Veterinárna ambulancia\",\"distance\":3776.65383758},{\"xCoordinate\":17.057207876867835,\"yCoordinate\":48.152070840121034,\"name\":\"Veterinary clinic\",\"distance\":3780.37806604},{\"xCoordinate\":17.1262517,\"yCoordinate\":48.1111545,\"name\":\"Pet Vet\",\"distance\":4385.3705344},{\"xCoordinate\":17.1638427,\"yCoordinate\":48.1635108,\"name\":\"Veterinárna ambulancia\",\"distance\":4490.80138967},{\"xCoordinate\":17.0457581,\"yCoordinate\":48.1826089,\"name\":\"Veterinárna poliklinika EUVET\",\"distance\":5963.64469412},{\"xCoordinate\":17.037255572542836,\"yCoordinate\":48.19209379219258,\"name\":\"W&G veterinárne združenie\",\"distance\":7133.17879078}]";
                    processSearchResult(fakeResults);
                }
            }
        });
        String urlDestination = null;
        if (mLastType == 0) {
            urlDestination = "get_vets";
        } else if (mLastType == 1) {
            urlDestination = "get_dog_parks";
        } else if (mLastType == 2) {
            urlDestination = "get_pet_stores";
        }
        at.execute(Data.SERVER_URL + urlDestination + "?x=" + mMyLongitude + "&y=" + mMyLatitude + "&distance=" + mDistanceValue);
    }

    private void processSearchResult(String result) {
        try {
            JSONArray json = new JSONArray(result);
            if (json.length() > 0) {
                processSearchResultsArray(json);
            } else {
                showNoResults();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNoResults() {
        Toast.makeText(this, "Sorry no results found, try to change distance", Toast.LENGTH_SHORT).show();
        mDistancePickerView.setVisibility(View.VISIBLE);
        mResultsOuternLayout.setVisibility(View.INVISIBLE);
    }

    private void processSearchResultsArray(JSONArray results) throws JSONException {
        mLastResultsList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            GenericObject object = new GenericObject(results.getJSONObject(i));
            mLastResultsList.add(object);
            drawResult(object);
        }
    }

    private void drawResult(final GenericObject object) {
        View.OnClickListener navigationListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWayOfTransportPicker(object);
            }
        };
        ComponentView cv = new ComponentView(this);
        cv.load().setPositions(displayResolution).setContent(object).setNavigationListener(navigationListener);
        mResultsList.addView(cv);
    }

    private void showWayOfTransportPicker(GenericObject object) {
        mLastPickedPoint = object;
        mWayOfTransportPickerLayout.setVisibility(View.VISIBLE);
    }

    public void checkResultsOnMapClickced(View view) {
        openMapWithInputNodes(mLastResultsList);
    }

    private void openMapWithInputNodes(ArrayList<GenericObject> inputPoints) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("nodes", inputPoints);
        startActivity(i);
    }


    public void walkClicked(View view) {
        if (mLastPickedPoint != null) {
            startNavigation(mLastPickedPoint);
        }
    }

    public void carClicked(View view) {
        downloadCloseParkLots();
    }

    private void downloadCloseParkLots() {
        ServerAsyncTask at = new ServerAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(String result) {
                if (result != null) {
                    parseParkLots(result);
                } else {
                    String fakeResults = "[{\"xCoordinate\":17.065452085832344,\"yCoordinate\":48.1755669725671,\"name\":\"Parkovisko McDonald's\",\"distance\":1660.75401218},{\"xCoordinate\":17.073445618810698,\"yCoordinate\":48.17532555835485,\"name\":\"Parkovisko - Partizánska lúka\",\"distance\":2212.57775191},{\"xCoordinate\":17.074043099999997,\"yCoordinate\":48.17437209999999,\"name\":\"Parkovisko - Partizánska lúka\",\"distance\":2294.23078503},{\"xCoordinate\":17.07491391373266,\"yCoordinate\":48.16905286398496,\"name\":\"WestEnd Parking\",\"distance\":2640.80952495},{\"xCoordinate\":17.07103777127606,\"yCoordinate\":48.15232907377045,\"name\":\"Parkovisko FMFI\",\"distance\":3856.4276835},{\"xCoordinate\":17.072069940118748,\"yCoordinate\":48.15237747414655,\"name\":\"Parkovisko FIIT\",\"distance\":3889.78833389},{\"xCoordinate\":17.067844832577276,\"yCoordinate\":48.14731583162296,\"name\":\"Private Club Parking\",\"distance\":4254.40411769},{\"xCoordinate\":17.1032384,\"yCoordinate\":48.1459911,\"name\":\"Garáž City\",\"distance\":5904.34764279},{\"xCoordinate\":17.1097066,\"yCoordinate\":48.1486613,\"name\":\"TatraCentrum\",\"distance\":6072.61928973},{\"xCoordinate\":17.113225716907458,\"yCoordinate\":48.14610019734595,\"name\":\"Onkologický ústav sv. Alžbety\",\"distance\":6455.07257236},{\"xCoordinate\":17.113544178606766,\"yCoordinate\":48.146046314861096,\"name\":\"Onkologický ústav sv. Alžbety\",\"distance\":6477.26925889},{\"xCoordinate\":17.1249757,\"yCoordinate\":48.156404,\"name\":\"Parkovisko\",\"distance\":6573.3505439},{\"xCoordinate\":17.1115797,\"yCoordinate\":48.1423887,\"name\":\"Garáž Opera\",\"distance\":6631.48854461},{\"xCoordinate\":17.1264696,\"yCoordinate\":48.1572307,\"name\":\"Platené parkovisko\",\"distance\":6633.46950957},{\"xCoordinate\":17.1207344,\"yCoordinate\":48.1472804,\"name\":\"Mamut\",\"distance\":6821.8419003},{\"xCoordinate\":17.114464997142534,\"yCoordinate\":48.139888759873976,\"name\":\"Osobný prístav\",\"distance\":6977.79046985},{\"xCoordinate\":17.11669632910455,\"yCoordinate\":48.140411043112756,\"name\":\"Starý most\",\"distance\":7061.54102188},{\"xCoordinate\":17.12363471938644,\"yCoordinate\":48.141852552528086,\"name\":\"parkovisko SND\",\"distance\":7355.28346813},{\"xCoordinate\":17.1119346,\"yCoordinate\":48.1311719,\"name\":\"GOLF USA customers only\",\"distance\":7546.68978817},{\"xCoordinate\":17.137909648602204,\"yCoordinate\":48.15394101815686,\"name\":\"Kovoprojekt private parking\",\"distance\":7559.41723164},{\"xCoordinate\":17.14237807301307,\"yCoordinate\":48.15920383332867,\"name\":\"Parkovisko Hotel Echo\",\"distance\":7643.17157837},{\"xCoordinate\":17.1152539,\"yCoordinate\":48.1308689,\"name\":\"Relaxx Garáž 1\",\"distance\":7735.10887088},{\"xCoordinate\":17.1161021,\"yCoordinate\":48.130842,\"name\":\"Relaxx Garáž 2\",\"distance\":7779.64601612},{\"xCoordinate\":17.136625681593493,\"yCoordinate\":48.14464265288585,\"name\":\"Zákaznícke centrum SPP\",\"distance\":7969.53426607},{\"xCoordinate\":17.138722436002343,\"yCoordinate\":48.144339142431825,\"name\":\"Parking\",\"distance\":8119.90269387},{\"xCoordinate\":17.13909982265064,\"yCoordinate\":48.14285565434573,\"name\":\"Garage\",\"distance\":8231.30398877},{\"xCoordinate\":17.146959119779776,\"yCoordinate\":48.14818735147288,\"name\":\"Alza.sk\",\"distance\":8445.118471},{\"xCoordinate\":17.152343808716182,\"yCoordinate\":48.15626727102036,\"name\":\"Nemocnica Ružinov\",\"distance\":8451.68624367},{\"xCoordinate\":17.107500231528437,\"yCoordinate\":48.11681235748554,\"name\":\"Parkovisko Kaufland\",\"distance\":8638.99637215},{\"xCoordinate\":17.1718751,\"yCoordinate\":48.1808593,\"name\":\"Merkury Market\",\"distance\":9380.4402859},{\"xCoordinate\":17.166050789251017,\"yCoordinate\":48.15709248908081,\"name\":\"Kerametal\",\"distance\":9386.45923406},{\"xCoordinate\":17.16644809039226,\"yCoordinate\":48.15592800456703,\"name\":\"Prima park\",\"distance\":9454.55693791},{\"xCoordinate\":17.181503024865993,\"yCoordinate\":48.16842904021281,\"name\":\"IKEA parking\",\"distance\":10217.98483594},{\"xCoordinate\":17.182049816677676,\"yCoordinate\":48.16629790810067,\"name\":\"Avion\",\"distance\":10297.45762743},{\"xCoordinate\":17.180266294949455,\"yCoordinate\":48.147286962951206,\"name\":\"Parkovisko pod viaduktom\",\"distance\":10748.92798664}]";
                    parseParkLots(fakeResults);
                }
            }
        });
        at.execute(Data.SERVER_URL + "get_park_lots?x=" + mLastPickedPoint.getLongitude() + "&y=" +
                mLastPickedPoint.getLatitude() + "&my_x=" + mMyLongitude + "&my_y=" + mMyLatitude);
    }

    private void parseParkLots(String result) {
        try {
            JSONArray json = new JSONArray(result);
            ArrayList<GenericObject> inputIntoMap = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                inputIntoMap.add(new GenericObject(json.getJSONObject(i)));
            }
            mLastPickedPoint.setTargetPoint(true);
            inputIntoMap.add(mLastPickedPoint);
            openMapWithInputNodes(inputIntoMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkOnImhdClicked(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://imhd.sk/ba/cestovne-poriadky"));
        startActivity(browserIntent);
    }

    public void busNavigateClicked(View view) {
        startNavigation(mStartBusStop);
    }

    private void startNavigation(GenericObject lastPicked) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" +
                lastPicked.getLatitude() + "," + lastPicked.getLongitude()));
        i.setPackage("com.google.android.apps.maps");
        startActivity(i);
    }

    public void showBusClicked(View view) {
        showBusLayout();
    }

    private void showBusLayout() {
        mBusOuternLayout.setVisibility(View.VISIBLE);
        mBusDataLayout.setVisibility(View.INVISIBLE);
        mBusProgress.setVisibility(View.VISIBLE);
        downloadBusInfo();
    }

    private void downloadBusInfo() {
        ServerAsyncTask at = new ServerAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(String result) {
                mBusProgress.setVisibility(View.INVISIBLE);
                mBusDataLayout.setVisibility(View.VISIBLE);
                if (result == null) {
                    String fakeResult = "{\"starBusStop\":{\"xCoordinate\":17.1080257,\"yCoordinate\":48.1065977,\"name\":\"Šintavská\",\"distance\":68279.32865868},\"endBusStop\":{\"xCoordinate\":17.1080257,\"yCoordinate\":48.1065977,\"name\":\"Šintavská\",\"distance\":179728.94593795}}";
                    processBusesResult(fakeResult);
                } else {
                    processBusesResult(result);
                }
            }
        });
        at.execute(Data.SERVER_URL + "get_bus_stops?my_x=" + mMyLongitude + "&my_y=" + mMyLatitude +
                "&dst_x=" + mLastPickedPoint.getLongitude() + "&dst_y=" + mLastPickedPoint.getLatitude());
    }

    private void processBusesResult(String result) {
        try {
            JSONObject resultJson = new JSONObject(result);
            GenericObject startBusStop = new GenericObject(resultJson.getJSONObject("starBusStop"));
            mStartBusStop = startBusStop;
            GenericObject endBusStop = new GenericObject(resultJson.getJSONObject("endBusStop"));
            mStartingBusStop.setText(Html.fromHtml("Closest bus stop to your position is: <b>" + startBusStop.getName() + "</b>"));
            mEndingBusStop.setText(Html.fromHtml("Closest bus stop to " + mLastPickedPoint.getName() + " is: <b>" +
                    endBusStop.getName() + "</b>"));
            mBusNavigateButton.setText("Navigate to " + startBusStop.getName());
        } catch (JSONException e) {
            Log.e(TAG, "parsing json error " + e.getMessage());
        }

    }
}
