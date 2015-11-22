package jake.king.sky.uk.cardview.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import jake.king.sky.uk.cardview.Adapter.RvAdapter;
import jake.king.sky.uk.cardview.Models.CardInfo;
import jake.king.sky.uk.cardview.Models.SubReddit;
import jake.king.sky.uk.cardview.R;

public class PostsFragment extends Fragment {

    private Activity activity;
    private Context context;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts, container, false);

        activity = getActivity();

        displayPosts((ArrayList<SubReddit>) getArguments().get("SUBS"));

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
        view.startAnimation(animation);

        return view;
    }

    public void displayPosts(ArrayList<SubReddit> subreddits) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.readitview_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CardInfo> mockPosts = new ArrayList<CardInfo>();
        ArrayList<String> links = new ArrayList<String>();

        links.add("http:///www.google.co.uk");
        links.add("http:///www.youtube.com");

        mockPosts.add(new CardInfo("Test post 1", "Votes: 10", null, links, false));
        mockPosts.add(new CardInfo("Test post 2", "Votes: 12", null, links, true));
        mockPosts.add(new CardInfo("Test post 4", "Votes: 1", null, links, false));
        mockPosts.add(new CardInfo("Test post 4", "Votes: 1", null, links, false));
        mockPosts.add(new CardInfo("Test post 4", "Votes: 1", null, links, false));
        mockPosts.add(new CardInfo("Test post 4", "Votes: 1", null, links, false));
        mockPosts.add(new CardInfo("Test post 4", "Votes: 1", null, links, false));
        mockPosts.add(new CardInfo("Test post 4", "Votes: 1", null, links, false));
        mockPosts.add(new CardInfo("Test post 4", "Votes: 1", null, links, false));

        RvAdapter adapter = new RvAdapter(mockPosts, getContext());
        recyclerView.setAdapter(adapter);

    }

}
