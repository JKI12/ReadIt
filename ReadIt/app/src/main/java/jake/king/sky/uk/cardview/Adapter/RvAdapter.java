package jake.king.sky.uk.cardview.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jake.king.sky.uk.cardview.Models.CardInfo;
import jake.king.sky.uk.cardview.R;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.CardViewHolder> {

    private ArrayList<CardInfo> posts;
    private Context context;
    private int lastPosition = -1;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvPostTitle;
        TextView tvPostInfo;
        ImageView ivPostImage;

        CardViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_story);
            tvPostTitle = (TextView) itemView.findViewById(R.id.card_story_text_title);
            tvPostInfo = (TextView) itemView.findViewById(R.id.card_story_text_content);
            ivPostImage = (ImageView) itemView.findViewById(R.id.card_story_image);
        }
    }

    public RvAdapter(ArrayList<CardInfo> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_story, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, final int i) {
        cardViewHolder.tvPostTitle.setText(posts.get(i).postTitle);
        cardViewHolder.tvPostInfo.setText(posts.get(i).postInfo);
        cardViewHolder.ivPostImage.setImageBitmap(posts.get(i).postPicture);
        if(posts.get(i).nsfw){
            int color = cardViewHolder.cardView.getContext().getResources().getColor(R.color.red_300);
            cardViewHolder.cardView.setCardBackgroundColor(color);
            cardViewHolder.ivPostImage.setImageDrawable(cardViewHolder.cardView.getContext().getDrawable(R.drawable.nsfw));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void removeAt(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, posts.size());
    }

}
