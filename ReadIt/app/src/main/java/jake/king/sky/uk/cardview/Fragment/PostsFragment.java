package jake.king.sky.uk.cardview.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import jake.king.sky.uk.cardview.Adapter.RvAdapter;
import jake.king.sky.uk.cardview.Models.CardInfo;
import jake.king.sky.uk.cardview.R;

public class PostsFragment extends Fragment {

    private Activity activity;
    private Context context;
    private View view;
    private ArrayList<CardInfo> posts;

    private RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts, container, false);

        activity = getActivity();

        initView();

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
        view.startAnimation(animation);

        return view;
    }

    private void initView() {

        posts = (ArrayList<CardInfo>) getArguments().get("SUBS");

        recyclerView = (RecyclerView) view.findViewById(R.id.readitview_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final RvAdapter adapter = new RvAdapter(posts, getContext());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeAt(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
}
