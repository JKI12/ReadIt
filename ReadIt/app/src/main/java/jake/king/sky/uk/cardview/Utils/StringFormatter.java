package jake.king.sky.uk.cardview.Utils;

public class StringFormatter {

    public String formatName(String userName){
        String formattedUserName = userName.replace("\"", "");
        String formatFirstLetter = formattedUserName.substring(0,1).toUpperCase();
        String newName = formatFirstLetter + formattedUserName.substring(1, formattedUserName.length());
        return newName;
    }

}
