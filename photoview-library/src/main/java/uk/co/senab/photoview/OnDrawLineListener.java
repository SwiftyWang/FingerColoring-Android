package uk.co.senab.photoview;

/**
 * Created by Swifty.Wang on 2015/9/9.
 */
public interface OnDrawLineListener {
    void OnDrawFinishedListener(boolean drawed, int startX, int startY, int endX, int endY);

    void OnGivenFirstPointListener(int startX, int startY);

    void OnGivenNextPointListener(int endX, int endY);
}
