package mmt.core;

import mmt.core.exceptions.NoSuchStationNameException;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;

/**
 * A Service has am id, a cost, and a list of stations.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
public class Service implements Serializable {

    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = 1830220111682854957L;

    /**
     * Id of the service.
     */
    private final int _id;

    /**
     * Cost of the service.
     */
    private double _cost;

    /**
     * List of stations associated with the service.
     */
    private List<Station> _stations;

    /**
     * Boolean representing if the list of stations is sorted or not.
     */
    private boolean _sorted;

    /**
     * Constructor.
     * Creates a service with all its attributes.
     *
     * @param id of the service to create.
     * @param cost of the service to create.
     */
    Service(int id, double cost) {
        _id = id;
        _cost = cost;
        _stations = new ArrayList<>();
        _sorted = true;
    }

    /**
     * Returns the id of the service.
     *
     * @return the service's id.
     */
    public int getId() {
        return _id;
    }

    /**
     * Returns the cost of the service.
     *
     * @return the service's cost.
     */
    double getCost() {
        return _cost;
    }

    /**
     * Returns the cost of a certain part of the service.
     *
     * @param start station to calculate the cost.
     * @param end station to calculate the cost.
     * @return service's cost between start and end.
     */
    double getCost(Station start, Station end) {
        Duration betweenDuration = getDuration(start, end);
        if (betweenDuration.isNegative())
            return -1;
        return _cost / getDuration().toMinutes() * betweenDuration.toMinutes();
    }

    /**
     * Returns the first station of the service.
     *
     * @return service's first station.
     */
    public Station getFirstStation() {
        return getStations().get(0);
    }

    /**
     * Returns the last station of the service.
     *
     * @return service's last station.
     */
    public Station getLastStation() {
        return getStations().get(_stations.size() - 1);
    }

    /**
     * Returns the duration of the service.
     *
     * @return the service's duration.
     */
    Duration getDuration() {
        return Duration.between(getFirstStation().getTime(this), getLastStation().getTime(this));
    }

    /**
     * Returns the duration of a certain part of the service.
     *
     * @param start station to calculate the duration.
     * @param end station to calculate the duration.
     * @return certain service's part duration.
     */
    Duration getDuration(Station start, Station end) {
        return Duration.between(start.getTime(this), end.getTime(this));
    }

    /**
     * Returns the stations of the service.
     *
     * @return the service's stations.
     */
    public List<Station> getStations() {
        if (!_sorted) {
            _stations.sort(Comparator.comparing(s -> s.getTime(this)));
            _sorted = true;
        }
        return Collections.unmodifiableList(_stations);
    }

    /**
     * Returns a "sublist" of stations of the service.
     *
     * @param start station of the "sublist".
     * @param end station of the "sublist".
     * @return service's stations "sublist".
     */
    List<Station> getStations(Station start, Station end) {
        List<Station> stations = getStations();
        int startIndex = stations.indexOf(start);
        int endIndex = stations.indexOf(end);
        if (startIndex > endIndex)
            return Collections.emptyList();
        return Collections.unmodifiableList(stations.subList(startIndex, endIndex + 1));
    }

    /**
     * Returns a "sublist" of all the stations after a station given as parameter.
     *
     * @param station after which the "sublist" will be created.
     * @return the "sublist" of stations after the station given as parameter.
     */
    List<Station> getStationsAfter(Station station) {
        if (!_stations.contains(station))
            return null;
        List<Station> stations = getStations();
        return Collections.unmodifiableList(stations.subList(stations.indexOf(station) + 1, stations.size()));
    }

    /**
     * Adds a station to the service's list of stations.
     *
     * @param station to add.
     */
    void addStation(Station station) {
        _stations.add(station);
        _sorted = false;
    }

    /**
     * Returns a string representing a Service.
     *
     * @return a string representing a Service.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(String.format(Locale.US, "Serviço #%d @ %.2f", _id, _cost));
        for (Station station: getStations())
            string.append("\n").append(station.printStation(this));
        return string.toString();
    }
}
