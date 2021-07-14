package mmt.core;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

/**
 * A Trip has a starting and an ending Station, and its associated with a Service.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
class Trip implements Serializable {
    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = -2363183161366495392L;

    /**
     * Service of the Trip.
     */
    private Service _service;

    /**
     * Station from which the Trip starts.
     */
    private Station _start;

    /**
     * Station to which the Trip end.
     */
    private Station _end;

    /**
     * Constructor.
     * Creates a Trip with all its attributes.
     *
     * @param service of the trip.
     * @param start station of the trip.
     * @param end station of the trip.
     */
    Trip(Service service, Station start, Station end) {
        _service = service;
        _start = start;
        _end = end;
    }

    /**
     * Returns the starting station of the Trip.
     *
     * @return the starting station of the Trip.
     */
    Station getStart() {
        return _start;
    }

    /**
     *  Returns the ending station of the Trip.
     *
     * @return the ending station of the Trip.
     */
    Station getEnd() {
        return _end;
    }

    /**
     * Returns the starting time of the Trip.
     *
     * @return the starting time of the Trip.
     */
    LocalTime getStartTime() {
        return _start.getTime(_service);
    }

    /**
     * Returns the ending time of the Trip.
     *
     * @return the ending time of the Trip.
     */
    LocalTime getEndTime() {
        return _end.getTime(_service);
    }

    /**
     * Returns the total duration of the Trip.
     *
     * @return the total duration of the Trip.
     */
    Duration getDuration() {
        return Duration.between(getStartTime(), getEndTime());
    }

    /**
     * Returns the list of stations in the Trip.
     *
     * @return the list of stations in the Trip.
     */
    List<Station> getStations() {
        return _service.getStations(_start, _end);
    }

    /**
     * Returns the list of stations excluding the first.
     *
     * @return the list of stations excluding the first.
     */
    List<Station> getStationsExcludingFirst() {
        List<Station> stations = getStations();
        return Collections.unmodifiableList(stations.subList(1, stations.size()));
    }

    /**
     * Returns the list of stations excluding the last.
     *
     * @return the list of stations excluding the last.
     */
    List<Station> getStationsExcludingLast() {
        List<Station> stations = getStations();
        return Collections.unmodifiableList(stations.subList(0, stations.size() - 1));
    }

    /**
     * Returns the service associated with the Trip.
     *
     * @return the service associated with the Trip.
     */
    Service getService() {
        return _service;
    }

    /**
     * Returns the total cost of the Trip.
     *
     * @return the total cost of the Trip.
     */
    double getCost() {
        return _service.getCost(getStart(), getEnd());
    }

    /**
     * Returns a string representing a Trip.
     *
     * @return a string representing a Trip.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(String.format(Locale.US, "Serviço #%d @ %.2f", _service.getId(), getCost()));
        for (Station station: getStations())
            string.append("\n").append(station.printStation(_service));
        return string.toString();
    }
}
