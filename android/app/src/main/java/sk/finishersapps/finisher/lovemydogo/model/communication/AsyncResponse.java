package sk.finishersapps.finisher.lovemydogo.model.communication;

/**
 * Created by Filip on 22.11.2017.
 * Serves to handle callback on MainThread
 */
public interface AsyncResponse {
    void processFinish(String result);
}
