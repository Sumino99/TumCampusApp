package de.tum.in.tumcampusapp.component.ui.ticket;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tum.in.tumcampusapp.api.app.TUMCabeClient;
import de.tum.in.tumcampusapp.component.ui.overview.card.ProvidesCard;
import de.tum.in.tumcampusapp.component.ui.ticket.model.Event;
import de.tum.in.tumcampusapp.utils.Utils;
import de.tum.in.tumcampusapp.utils.sync.SyncManager;

import static de.tum.in.tumcampusapp.utils.CacheManager.VALIDITY_ONE_DAY;

public class EventsController implements ProvidesCard {

    private static final int TIME_TO_SYNC = VALIDITY_ONE_DAY;
    private final Context context;

    // TODO: replace by database connection (@Dao classes etc.; see NewDao etc. for reference)
    private HashMap<Integer, Event> eventsMap = new HashMap<>();

    /**
     * Constructor, open/create database, create table if necessary
     *
     * @param context Context
     */
    public EventsController(Context context) {
        this.context = context;
    }

    /**
     * Download events from external interface (JSON)
     *
     * @param force True to force download over normal sync period, else false
     */
    public void downloadFromExternal(boolean force) {
        TUMCabeClient api = TUMCabeClient.getInstance(context);

        // Load all news since the last sync
        List<Event> events = new ArrayList<>();
        try {
            events = api.getEvents();
        } catch (IOException e) {
            Utils.log(e);
            return;
        }

        // Add events to map
        for (Event event : events){
            eventsMap.put(event.getId(), event);
        }
    }

    @Override
    public void onRequestCard(@NotNull Context context) {
        // TODO
    }

}
