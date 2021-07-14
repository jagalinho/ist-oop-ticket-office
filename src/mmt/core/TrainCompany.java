package mmt.core;

import mmt.core.exceptions.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Train Company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
class TrainCompany implements Serializable {

    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = 201708301010L;

    /**
     * Passengers of the TrainCompany.
     */
    private Map<Integer, Passenger> _passengers;

    /**
     * Services of the TrainCompany.
     */
    private Map<Integer, Service> _services;

    /**
     * Stations of the TrainCompany.
     */
    private Map<String, Station> _stations;

    /**
     * Counter for passengers' id.
     */
    private int _passengerCounter;

    /**
     * Constructor.
     * <p>
     * Creates a TrainCompany with all its attributes.
     */
    TrainCompany() {
        _services = new HashMap<>();
        _passengers = new HashMap<>();
        _stations = new HashMap<>();
    }

    /**
     * Returns the map of passengers.
     *
     * @return Unmodifiable Map of _passengers.
     */
    List<Passenger> getPassengers() {
        return new ArrayList<>(_passengers.values());
    }

    /**
     * Returns the pasenger with the id given as parameter.
     *
     * @param id of the passenger to return.
     * @return passenger with the id given as parameter.
     * @throws NoSuchPassengerIdException if there is no passenger with the id given as parameter.
     */
    Passenger getPassengerById(int id) throws NoSuchPassengerIdException {
        if (!(_passengers.containsKey(id)))
            throw new NoSuchPassengerIdException(id);
        return _passengers.get(id);
    }

    /**
     * Returns the list of all services.
     *
     * @return list of all services.
     */
    List<Service> getServices() {
        return new ArrayList<>(_services.values());
    }

    /**
     * Returns the service with the id given as parameter.
     *
     * @param id of service to search.
     * @return service with id given as parameter.
     * @throws NoSuchServiceIdException if the id given
     *                                  doesn't exist in the map of services.
     */
    Service getServiceById(int id) throws NoSuchServiceIdException {
        if (!(_services.containsKey(id)))
            throw new NoSuchServiceIdException(id);
        return _services.get(id);
    }

    /**
     * Returns the list of all stations.
     *
     * @return list of all stations.
     */
    List<Station> getStations() {
        return new ArrayList<>(_stations.values());
    }

    /**
     * Adds a Station for a Service to the TrainCompany, if it doesn't exist already.
     *
     * @param serviceId of the service to add to the new station.
     * @param name of the station to add.
     * @param time of departure in the station to add.
     * @throws NoSuchServiceIdException if there is no service with the id given as parameter.
     * @throws NonUniqueStationAtServiceException if already exists a station in the service with the name given as
     *                                            parameter.
     */
    void addStation(int serviceId, String name, LocalTime time) throws NoSuchServiceIdException, NonUniqueStationAtServiceException {
        Service service = getServiceById(serviceId);

        _stations.putIfAbsent(name, new Station(name));
        Station station = _stations.get(name);

        station.addService(service, time);
        service.addStation(station);
    }

    void addStationName(String name) throws NonUniquePassengerNameException {
        if (_stations.containsKey(name))
            throw new NonUniquePassengerNameException(name);
        _stations.put(name, new Station(name));
    }

    /**
     * Returns the station with the name given as parameter.
     *
     * @param name of the station to search for.
     * @return station with name given as parameter.
     * @throws NoSuchStationNameException if the name given
     *                                    doesn't exist in the map of stations.
     */
    Station getStation(String name) throws NoSuchStationNameException {
        if (!(_stations.containsKey(name)))
            throw new NoSuchStationNameException(name);
        return _stations.get(name);
    }

    /**
     * Adds a service to the TrainCompany.
     *
     * @param id   of the service to add.
     * @param cost of the service to add.
     * @throws NonUniqueServiceIdException if there already exists a service with
     *                                     the same id as the one given as parameter.
     */
    void addService(int id, double cost) throws NonUniqueServiceIdException {
        if (_services.containsKey(id))
            throw new NonUniqueServiceIdException(id);
        _services.put(id, new Service(id, cost));
    }

    /**
     * Adds a passenger to the TrainCompany.
     *
     * @param name of the passenger to add.
     * @throws NonUniquePassengerNameException if there already exists a passenger
     *                                         with the name given as parameter.
     */
    void addPassenger(String name) throws NonUniquePassengerNameException {
        if (getPassengers().parallelStream().map(Passenger::getName).anyMatch(name::equals))
            throw new NonUniquePassengerNameException(name);
        _passengers.put(_passengerCounter, new Passenger(_passengerCounter, name));
        _passengerCounter += 1;
    }

    /**
     * Changes a passenger's name.
     *
     * @param id      of the passenger who wants to change the name
     * @param newName of the passenger
     * @throws NoSuchPassengerIdException      if the is no passenger with the id
     *                                         given as parameter.
     * @throws NonUniquePassengerNameException if there already exists a passenger
     *                                         with the name given as parameter.
     */
    void changePassengerName(int id, String newName) throws NoSuchPassengerIdException, NonUniquePassengerNameException {
        if (getPassengers().parallelStream().map(Passenger::getName).anyMatch(newName::equals))
            throw new NonUniquePassengerNameException(newName);
        getPassengerById(id).setName(newName);
    }

    /**
     * Erases all passengers from the TrainCompany.
     */
    void erasePassengers() {
        _passengers.clear();
        _passengerCounter = 0;
    }

    /**
     * Adds an itinerary to a specific passenger in the TrainCompany.
     *
     * @param passengerId who wants to add an itinerary.
     * @param itinerary   itinerary to add.
     */
    void addItinerary(int passengerId, Itinerary itinerary) throws NoSuchPassengerIdException {
        getPassengerById(passengerId).addItinerary(itinerary);
    }

    /**
     * Returns a list of all the possible itineraries that conform with the specifications given as parameters.
     *
     * @param start Station from which the itinerary will start.
     * @param end Station to which the itinerary will end.
     * @param day of the itinerary.
     * @param time of the departure station.
     * @return a list of all the possible itineraries that conform with the specifications given as parameters.
     */
    List<Itinerary> searchItineraries(Station start, Station end, LocalDate day, LocalTime time) {
        List<Itinerary> itineraries = searchItinerariesAux(start, end, day, time, new ArrayList<>());
        itineraries.sort(Comparator.comparing(Itinerary::getStartTime).thenComparing(Itinerary::getEndTime));
        return itineraries;
    }

    /**
     * Auxiliary method to {@link #searchItineraries}.
     */
    private List<Itinerary> searchItinerariesAux(Station start, Station end, LocalDate day, LocalTime time, List<Itinerary> itineraries) {
        List<Service> services = start.getServices().stream()
            .filter(service -> start.getTime(service).isAfter(time))
            .collect(Collectors.toList());

        services.stream()
            .filter(service -> service.getStations().contains(end)
                && end.getTime(service).isAfter(start.getTime(service)))
            .map(service -> new Itinerary(day, service, start, end))
            .forEach(itineraries::add);

        if (itineraries.isEmpty())
            services.forEach(service ->
                service.getStationsAfter(start).stream()
                    .map(station -> searchItinerariesAux(station, end, day, station.getTime(service), new ArrayList<>(itineraries)))
                    .flatMap(List::stream)
                    .filter(itinerary -> itinerary.addTripStart(service, start, itinerary.getFirstTrip().getStart()))
                    .min(Comparator.comparing(Itinerary::getEndTime))
                    .ifPresent(itineraries::add));

        return itineraries;
    }
}