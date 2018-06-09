package de.tum.in.tumcampusapp.component.ui.ticket;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tum.in.tumcampusapp.api.app.TUMCabeClient;
import de.tum.in.tumcampusapp.component.ui.overview.card.ProvidesCard;
import de.tum.in.tumcampusapp.component.ui.ticket.model.Event;
import de.tum.in.tumcampusapp.utils.Utils;


public class EventsController implements ProvidesCard {

    private final Context context;

    // TODO: replace by database connection (@Dao classes etc.; see NewDao etc. for reference)
    private Map<Integer ,Event> eventsMap = new HashMap<>();

    /**
     * Constructor, open/create database, create table if necessary
     *
     * @param context Context
     */
    public EventsController(Context context) {
        this.context = context;

        // TODO: test server connection!
        //downloadFromExternal(false);
        initializeDummyEvents();
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

    private void initializeDummyEvents(){
        for(int i = 1; i < 5; i++){
            Event event = new Event(i, "http://placehold.it/120x120&text=image" + i, "Unity",
                            "Die TUM Campus App wird von Freiwilligen und Studenten entwickelt. " +
                                    "Die App eignet sich sowohl zur Verwendung auf Smartphones, wie auch Tablets und bietet " +
                                    "unter anderem folgende Funktionen:Vorlesungstermine, Neuigkeiten von TUM relevanten " +
                                    "Organisationen, Veranstaltungshinweise, Mensa Speiseplan, MVV Abfahrtszeiten, " +
                                    "Umgebungspläne und viele weitere Funktionen. ",
                            "Garching, Magistrale",
                            new GregorianCalendar(2018 + i, 8, 8).getTime(),
                            "https://mpi.fs.tum.de/fuer-studierende/veranstaltungen/unity/");
            eventsMap.put(event.getId(), event);
        }

        Event event = new Event(5, "http://placehold.it/120x120&text=image" + 5, "Unity",
                "Kurze Beschreibung",
                "Garching, Magistrale",
                new GregorianCalendar(2018 + 5, 8, 8).getTime(),
                "https://mpi.fs.tum.de/fuer-studierende/veranstaltungen/unity/");
        eventsMap.put(event.getId(), event);
    }

    /**
     * Only for testing purposes as server calls are not yet implemented
     * -> TODO: replace with real data
     *
     * @return
     */
    public List<Event> getEvents() {
        return new ArrayList<>(eventsMap.values());
    }

    public List<Event> getBookedEvents() {
        List<Event> bookedEvents = new ArrayList<>();
        bookedEvents.add(eventsMap.get(1));
        bookedEvents.add(eventsMap.get(3));
        bookedEvents.add(eventsMap.get(5));
        return bookedEvents;
    }
}
