package mmt.core;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An Itinerary has a day and trips.
 * An Itinerary will be acquired by a passenger.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */

public class Itinerary implements Serializable {
    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = -5362029440415892681L;

    /**
     * Day of the itinerary.
     */
    private LocalDate _day;

    /**
     * Trips that make the itinerary.
     */
    private LinkedList<Trip> _trips;

    /**
     * Constructor.
     * Creates an empty Itinerary with a day.
     *
     * @param day of the itinerary to create.
     */
    Itinerary(LocalDate day) {
        _day = day;
        _trips = new LinkedList<>();
    }

    /**
     * Constructor.
     * Creates an Itinerary with a day and a trip.
     *
     * @param day of the itinerary to create.
     * @param service of the trip to create the itinerary.
     * @param start of the trip to create the itinerary.
     * @param end of the trip to create the itinerary.
     */
    Itinerary(LocalDate day, Service service, Station start, Station end) {
        this(day);
        addTripStart(service, start, end);
    }

    /**
     * Returns the Itinerary's day.
     *
     * @return the itinerary's _day.
     */
    public LocalDate getDay() {
        return _day;
    }

    /**
     * Returns the starting time of the Itinerary.
     *
     * @return the Itinerary's starting time.
     */
    LocalTime getStartTime() {
        Trip startTrip = getFirstTrip();
        return startTrip.getStart().getTime(startTrip.getService());
    }

    /**
     * Returns the ending time of the Itinerary.
     *
     * @return the Itinerary's ending time.
     */
    LocalTime getEndTime() {
        Trip endTrip = getLastTrip();
        return endTrip.getEnd().getTime(endTrip.getService());
    }

    /**
     * Returns the first trip of _trips.
     *
     * @return _trips' first trip.
     */
    Trip getFirstTrip() {
        return _trips.getFirst();
    }

    /**
     * Returns the last trip of _trips.
     *
     * @return _trips' last trip.
     */
    Trip getLastTrip() {
        return _trips.getLast();
    }

    /**
     * Returns the list of trips.
     *
     * @return Unmodifiable List of _trips.
     */
    List<Trip> getTrips() {
        return Collections.unmodifiableList(_trips);
    }

    /**
     * Adds a valid trip to the start of the list of trips if the established conditions are respected.
     * A trip is valid if:
     *      - service contains both start and end;
     *      - has more than one station;
     *      - start is before the end in the given service.
     * The conditions are:
     *      - the end of the trip has to be the same as the start of the itinerary;
     *      - the end time of the trip has to be before the start time of the itinerary;
     *      - the service of the trip can't be already present in the itinerary;
     *      - none of the stations of the trip can be already present in the itinerary,
     *      with the exception of the last station of the trip and the first station of the first trip of the itinerary.
     *
     * @param service of the trip to add.
     * @param start of the trip to add.
     * @param end of the trip to add.
     * @return true if the trip was successfully added and false otherwise.
     */
    boolean addTripStart(Service service, Station start, Station end) {
        List<Station> stations = service.getStations(start, end);
        if (!stations.contains(start) || !stations.contains(end)
            || stations.size() <= 1
            || (!_trips.isEmpty() && (!end.equals(getFirstTrip().getStart()) || end.getTime(service).isAfter(getFirstTrip().getStartTime())))
            || getAllServices().contains(service)
            || stations.subList(0, stations.size() - 1).parallelStream().anyMatch(getAllStations()::contains))
            return false;
        _trips.addFirst(new Trip(service, start, end));
        return true;
    }

    /**
     * Adds a valid trip to the end of the list of trips if the established conditions are respected.
     * A trip is valid if:
     *      - service contains both start and end;
     *      - has more than one station;
     *      - start is before the end in the given service.
     * The conditions are:
     *      - the start of the trip has to be the same as the end of the itinerary;
     *      - the start time of the trip has to be after the end time of the itinerary;
     *      - the service of the trip can't be already present in the itinerary;
     *      - none of the stations of the trip can be already present in the itinerary,
     *      with the exception of the last station of the trip and the first station of the first trip of the itinerary.
     *
     * @param service of the trip to add.
     * @param start of the trip to add.
     * @param end of the trip to add.
     * @return true if the trip was successfully added and false otherwise.
     */
    boolean addTripEnd(Service service, Station start, Station end) {
        List<Station> stations = service.getStations(start, end);
        if (!stations.contains(start) || !stations.contains(end)
            || stations.indexOf(end) < stations.indexOf(start)
            || stations.size() <= 1
            || (!_trips.isEmpty() && (!start.equals(getLastTrip().getEnd()) || start.getTime(service).isBefore(getLastTrip().getEndTime())))
            || getAllServices().contains(service)
            || stations.subList(1, stations.size()).parallelStream().anyMatch(getAllStations()::contains))
            return false;
        _trips.addLast(new Trip(service, start, end));
        return true;
    }

    /**
     * Returns a list of all the services in one itinerary.
     *
     * @return all itinerary's services.
     */
    List<Service> getAllServices() {
        return _trips.stream().map(Trip::getService).collect(Collectors.toList());
    }

    /**
     * Returns a list of all the station in one itinerary.
     *
     * @return all itinerary's stations.
     */
    List<Station> getAllStations() {
        LinkedList<Station> stations = _trips.stream().flatMap(t -> t.getStationsExcludingFirst().stream()).collect(Collectors.toCollection(LinkedList::new));
        if (!_trips.isEmpty())
            stations.addFirst(getFirstTrip().getStart());
        return stations;
    }

    /**
     * Returns the total cost of the itinerary.
     *
     * @return the total cost of the itinerary.
     */
    public double getCost() {
        return _trips.stream().mapToDouble(Trip::getCost).sum();
    }

    /**
     * Returns the total duration of the itinerary.
     *
     * @return the total duration of the itinerary.
     */
    Duration getDuration() {
        Trip firstTrip = getFirstTrip();
        Trip lastTrip = getLastTrip();
        return Duration.between(firstTrip.getStart().getTime(firstTrip.getService()), lastTrip.getEnd().getTime(lastTrip.getService()));
    }

    /**
     * Returns a string representing the itinerary.
     *
     * @param id that will be given to the itinerary.
     * @return a string of trips to print.
     */
    public String printItinerary(int id) {
        StringBuilder string = new StringBuilder(String.format(Locale.US, "Itinerário %d para %s @ %.2f", id, _day, getCost()));
        for (Trip trip : _trips)
            string.append("\n").append(trip);
        return string.toString();
    }
}