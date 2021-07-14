package mmt.core;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A Passenger has an id, a name, a value  spent in the Train Company, has a list of all
 * itineraries used by the passenger as well as a list of temporary ones, that will be used to
 * calculate the passenger's category.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
public class Passenger implements Serializable {

    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = -2735800031670193147L;

    /**
     * Passenger's id.
     */
    private final int _id;

    /**
     * Passenger's name.
     */
    private String _name;

    /**
     * Value spent by the passenger.
     */
    private double _spent;

    /**
     * Passenger's itineraries.
     */
    private List<Itinerary> _itineraries;

    /**
     * Passenger's temporary itineraries.
     */
    private List<Itinerary> _tempItineraries;

    /**
     * Passenger's category.
     */
    private Category _category;

    /**
     * Constructor.
     * Creates a Passenger with its attributes.
     *
     * @param id   of the passenger to create.
     * @param name of the passenger to create.
     */
    Passenger(int id, String name) {
        _id = id;
        _name = name;
        _itineraries = new ArrayList<>();
        _category = Normal.INSTANCE;
    }

    /**
     * Returns the name of the passenger.
     *
     * @return the passenger's name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Function that changes the passenger's name.
     *
     * @param name to change the passenger's name to.
     */
    void setName(String name) {
        _name = name;
    }

    /**
     * Returns the id os te passenger.
     *
     * @return passenger's id.
     */
    public int getId() {
        return _id;
    }

    /**
     * Returns the Category of the passenger.
     *
     * @return the passenger's category.
     */
    Category getCategory() {
        _category = _category.getCategory(this);
        return _category;
    }

    /**
     * Returns the itineraries used by the passenger.
     *
     * @return Unmodifiable list of itineraries.
     */
    public List<Itinerary> getItineraries() {
        return Collections.unmodifiableList(_itineraries);
    }

    /**
     * Adds an itinerary to the passenger.
     *
     * @param itinerary to add at the list of itineraries use by the passenger.
     */
    void addItinerary(Itinerary itinerary) {
        _spent += itinerary.getCost() * getCategory().getDiscount();
        _itineraries.add(itinerary);
    }

    /**
     * Returns the temporary itineraries.
     *
     * @return Unmodifiable list of the temporary itineraries.
     */
    List<Itinerary> getTempItineraries() {
        return Collections.unmodifiableList(_tempItineraries);
    }

    /**
     * Changes the list of temporary itineraries.
     *
     * @param tempItineraries to be the new temporary itineraries.
     */
    void setTempItineraries(List<Itinerary> tempItineraries) {
        _tempItineraries = tempItineraries;
    }

    /**
     * Returns the value spent by the passenger.
     *
     * @return value spent by the passenger.
     */
    double getSpent() {
        return _spent;
    }

    /**
     * Returns the amount of time spent travelling.
     *
     * @return amount of time spent traveling.
     */
    Duration getFullTravelTime() {
        return _itineraries.stream().map(Itinerary::getDuration).reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Returns a string representing a Passenger.
     *
     * @return a string representing a Passenger.
     */
    @Override
    public String toString() {
        Duration fullTravelTime = getFullTravelTime();
        return String.format(Locale.US, "%d|%s|%s|%d|%.2f|%02d:%02d", _id, _name, getCategory(), _itineraries.size(), _spent, fullTravelTime.toHours(), fullTravelTime.toMinutes() % 60);
    }
}