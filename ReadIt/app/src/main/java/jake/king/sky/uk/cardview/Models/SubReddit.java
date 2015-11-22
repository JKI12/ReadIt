package jake.king.sky.uk.cardview.Models;

import java.io.Serializable;

/**
 * Created by JK on 22/11/2015.
 */
public class SubReddit implements Serializable {

    public String subredditName;
    public String subredditLink;

    public SubReddit(String subredditname, String subredditLink) {
        this.subredditName = subredditname;
        this.subredditLink = subredditLink;
    }
}
