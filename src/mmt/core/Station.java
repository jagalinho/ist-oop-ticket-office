package mmt.core;

import mmt.core.exceptions.NonUniqueStationAtServiceException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;

/**
 * A Station has a name and a map of Services.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
public class Station implements Serializable {

    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = -2327971238328157083L;

    /**
     * Name of the station.
     */
    private String _name;

    /**
     * Times of the stations services.
     */
    private Map<Service, LocalTime> _times;

    /**
     * Constructor.
     * Creates a Station with all its attributes.
     *
     * @param name of the station to create.
     */
    Station(String name) {
        _name = name;
        _times = new HashMap<>();
    }

    /**
     * Returns the name of the station.
     *
     * @return the station's name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the services of the station.
     *
     * @return the station's services.
     */
    List<Service> getServices() {
        return new ArrayList<>(_times.keySet());
    }

    /**
     * Returns the time of the station's services given as parameter.
     *
     * @param service from which the time will be returned.
     * @return time of the service given as parameter.
     */
    public LocalTime getTime(Service service) {
        return _times.get(service);
    }

    /**
     * Adds a service to the station's services.
     *
     * @param service to add to the station.
     * @param time of the service to add.
     * @throws NonUniqueStationAtServiceException if theres already a station with the same name as the given one.
     */
    void addService(Service service, LocalTime time) throws NonUniqueStationAtServiceException {
        if (_times.containsKey(service))
            throw new NonUniqueStationAtServiceException(getName(), service.getId());
        _times.put(service, time);
    }

    /**
     * Returns a string representing a Station.
     *
     * @param service to take the time from.
     * @return a string representing a Station.
     */
    String printStation(Service service) {
        return String.format("%s %s", getTime(service), _name);
    }
}
