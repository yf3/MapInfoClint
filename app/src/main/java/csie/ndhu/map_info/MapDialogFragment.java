package csie.ndhu.map_info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

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


/**
 * Activities that contain this fragment must implement the
 * {@link MapDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 *  Open from an activity:
 *  MapDialogFragment dialogFragment = MapDialogFragment.newInstance(latitude, longitude);
 *  dialogFragment.show(getSupportFragmentManager(), "map");
 */

public class MapDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private double mLatitude;
    private double mLongitude;

    private OnFragmentInteractionListener mListener;

    private MapView mapView;

    private Button buttonSavePhoto;

    public MapDialogFragment() {
        // Required empty public constructor
    }

    public static MapDialogFragment newInstance(double latitude, double longitude) {
        MapDialogFragment fragment = new MapDialogFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, latitude);
        args.putDouble(ARG_PARAM2, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLatitude = getArguments().getDouble(ARG_PARAM1);
            mLongitude = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_public_token));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_map_dialog, null);

        initMapView(view, savedInstanceState);

        buttonSavePhoto = view.findViewById(R.id.buttonSavePhoto);
        buttonSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction();
            }
        });

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
