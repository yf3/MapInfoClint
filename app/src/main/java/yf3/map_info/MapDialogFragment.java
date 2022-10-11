package yf3.map_info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

@Deprecated
public class MapDialogFragment extends DialogFragment {
    private static final String ARG_LATITUDE = "mLatitude";
    private static final String ARG_LONGITUDE = "mLongitude";

    private double mLatitude;
    private double mLongitude;

    private MapView mapView;

    public MapDialogFragment() {
        // Required empty public constructor
    }

    public static MapDialogFragment newInstance(double latitude, double longitude) {
        MapDialogFragment fragment = new MapDialogFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLatitude = getArguments().getDouble(ARG_LATITUDE);
            mLongitude = getArguments().getDouble(ARG_LONGITUDE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_public_token));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_map_dialog, null);

        initMapView(view, savedInstanceState);

        builder.setView(view);
        return builder.create();
    }

    private void initMapView(View view, Bundle saveInstanceSate) {
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(saveInstanceSate);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        LatLng markerPosition = new LatLng(mLatitude, mLongitude);
                        mapboxMap.setCameraPosition(new CameraPosition.Builder().target(markerPosition).build());
                        SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);

                        style.addImage("temp_marker", BitmapFactory.decodeResource(getResources(), R.drawable.mapbox_marker_icon_default));
                        Symbol symbol = symbolManager.create(new SymbolOptions()
                                .withLatLng(markerPosition).withIconImage("temp_marker"));
                    }
                });
            }
        });
    }

}
