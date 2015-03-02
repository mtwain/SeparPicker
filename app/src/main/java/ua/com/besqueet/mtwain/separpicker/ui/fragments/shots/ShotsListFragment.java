package ua.com.besqueet.mtwain.separpicker.ui.fragments.shots;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.halfbit.tinybus.Subscribe;
import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.ShotsController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.data.Shot;
import ua.com.besqueet.mtwain.separpicker.events.ShotDeletedEvent;
import ua.com.besqueet.mtwain.separpicker.events.ShotItemClickEvent;
import ua.com.besqueet.mtwain.separpicker.events.ShotsListChangedEvent;

public class ShotsListFragment extends Fragment implements Constants{

    public ShotsListFragment(){}

    @InjectView(R.id.shotsList)
    ListView shotsList;
    BusController busInstance;
    ShotsController shotsInstance;
    ShotsAdapter shotsAdapter;
    ArrayList<Shot> shots = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shotsAdapter = new ShotsAdapter();
        shotsInstance = ShotsController.INSTANCE;
        busInstance = BusController.INSTANCE;
        shots = shotsInstance.getShots();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shots_list,container,false);
        ButterKnife.inject(this, rootView);
        shotsList.setAdapter(shotsAdapter);
        shotsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!UtilsController.INSTANCE.isTablet()) {
                    long id = shots.get(i).id;
                    Bundle bundle = new Bundle();
                    bundle.putLong(BUNDLE_SHOT_ID,id);
                    Fragment fragment = new ShotDetailFragment();

                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()

                            .add(R.id.containerContacts,fragment)
                            .addToBackStack("")
                            .commit();

                }else{
                    busInstance.getBus().post(new ShotItemClickEvent(shots.get(i)));
                }
            }
        });

            shotsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Видалити шот ?")
                            .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setPositiveButton("Так", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    shotsInstance.deleteShot(shots.get(i).id);
                                    shots.remove(i);
                                    shotsAdapter.notifyDataSetChanged();
                                    if(UtilsController.INSTANCE.isTablet()) {
                                        busInstance.getBus().post(new ShotDeletedEvent());
                                    }
                                }
                            });
                    builder.create().show();
                    return true;
                }
            });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!busInstance.getBus().hasRegistered(this))
            busInstance.getBus().register(this);
    }

    @Override
    public void onStop() {
        busInstance.getBus().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class ShotsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return shots.size();
        }

        @Override
        public Object getItem(int i) {
            return shots.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = ContextController.INSTANCE.getMainActivity().getLayoutInflater();
            View vi = view;
            ViewHolder holder;
            if (vi == null) {
                vi = inflater.inflate(R.layout.cell_shot, viewGroup, false);
                holder = new ViewHolder();
                holder.textName = (TextView) vi.findViewById(R.id.textTime);
                vi.setTag(holder);
            }else {
                holder = (ViewHolder) vi.getTag();
            }
            holder.textName.setText(shots.get(i).name);
            return vi;
        }
    }

    static class ViewHolder {
        TextView textName;
    }

    @Subscribe
    public void onShotsListChangedListener(ShotsListChangedEvent event){
        shots = shotsInstance.getShots();
        shotsAdapter.notifyDataSetChanged();
    }

}
