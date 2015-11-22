package jake.king.sky.uk.cardview.Models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class CardInfo implements Serializable{

    //Title
    public String postTitle;
    //Info includes: Authour, score,
    public String postInfo;
    //Thumbnail
    public Bitmap postPicture;
    //permalink, url
    public ArrayList<String> links;
    //over_18
    public Boolean nsfw;

    public CardInfo(String postTitle, String postInfo, Bitmap postPicture, ArrayList<String> links, Boolean nsfw) {
        this.postTitle = postTitle;
        this.postInfo = postInfo;
        this.postPicture = postPicture;
        this.links = links;
        this.nsfw = nsfw;
    }
}
