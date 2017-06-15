package city.studyplex.dev.picland.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by GM.PiratiK on 28.05.2017.
 */

public class PathHelper {
    public static String getImageFolderPath(String path) {
        String buffer[] = path.split("/");
        String result = "";
        for (int i = 0; i < buffer.length - 1; i++)
            result += buffer[i] + "/";
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public static String getAlbumName(String path) {
        String buffer[] = path.split("/");
        return buffer[buffer.length - 1];
    }

    public static String quoteReplace(String psString) {
        return psString.replace("'", "^");
    }

    public static String quoteReverse(String psString) {
        return psString.replace("^", "'");
    }

    public static void showToast(Context x, String s) {
        Toast t = Toast.makeText(x, s, Toast.LENGTH_SHORT);
        t.show();
    }
}
