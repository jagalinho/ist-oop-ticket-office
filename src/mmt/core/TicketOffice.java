package mmt.core;

import mmt.core.exceptions.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A TicketOffice has a TrainCompany and a file that will were all the status are saved or loaded from.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
public class TicketOffice {

    /**
     * TrainCompany associated with the TicketOffice.
     */
    private TrainCompany _trainCompany;

    /**
     * String representing the name of the file associated with the TicketOffice.
     */
    private String _file;

    /**
     * Constructor.
     * Creates a TicketOffice with all its attributes.
     */
    public TicketOffice() {
        _trainCompany = new TrainCompany();
    }

    /**
     * Returns the company associated with the TicketOffice.
     *
     * @return the company associated with the TicketOffice.
     */
    private TrainCompany getCompany() {
        return _trainCompany;
    }

    /**
     * Returns a list of services of the TrainCompany associated with the TicketOffice.
     *
     * @return a list of services of the TrainCompany associated with the TicketOffice.
     */
    public List<Service> getServices() {
        return _trainCompany.getServices();
    }

    /**
     * Returns a list of services which the first station is the one given as parameter.
     *
     * @param stationName name of the first station.
     * @return a list of services which the first station is the one given as parameter.
     * @throws NoSuchStationNameException if there is no station with the name given as parameter.
     */
    public List<Service> getServiceStart(String stationName) throws NoSuchStationNameException {
        Station station = _trainCompany.getStation(stationName);
        return _trainCompany.getServices()
                .stream()
                .filter(s -> s.getFirstStation().equals(station))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of services which the last station is the one given as parameter.
     *
     * @param stationName name of the last station.
     * @return a list of services which the last station is the one given as parameter.
     * @throws NoSuchStationNameException if there is no station with the name given as parameter.
     */
    public List<Service> getServiceEnd(String stationName) throws NoSuchStationNameException {
        Station station = _trainCompany.getStation(stationName);
        return _trainCompany.getServices()
                .stream()
                .filter(s -> s.getLastStation().equals(station))
                .collect(Collectors.toList());
    }

    /**
     * Returns a service with the id given as parameter.
     *
     * @param id of the service to return.
     * @return a service with the id given as parameter.
     * @throws NoSuchServiceIdException if there is no service with the id given as parameter.
     */
    public Service getServiceById(int id) throws NoSuchServiceIdException {
        return _trainCompany.getServiceById(id);
    }

    /**
     * Returns a list of all the passengers of the TrainCompany that belongs to the TicketOffice.
     *
     * @return a list of all the passengers of the TrainCompany that belongs to the TicketOffice.
     */
    public List<Passenger> getPassengers() {
        return _trainCompany.getPassengers();
    }

    /**
     * Returns a passenger with the id given as parameter.
     *
     * @param id of the passenger to return.
     * @return a passenger with the id given as parameter.
     * @throws NoSuchPassengerIdException if there is no passenger with the id given as parameter.
     */
    public Passenger getPassengerById(int id) throws NoSuchPassengerIdException {
        return _trainCompany.getPassengerById(id);
    }

    /**
     * Adds a passenger to the TrainCompany that belongs to the TicketOffice.
     *
     * @param name of the passenger to add.
     * @throws NonUniquePassengerNameException if already exists a passenger with the name given as parameter.
     */
    public void newPassenger(String name) throws NonUniquePassengerNameException {
        _trainCompany.addPassenger(name);
    }

    /**
     * Changes a passenger name to the one given as parameter.
     *
     * @param id of the passenger which the name will be changed.
     * @param newName to change the name to.
     * @throws NoSuchPassengerIdException if there is no passenger with the id given as parameter.
     * @throws NonUniquePassengerNameException if already exists a passenger with the newName.
     */
    public void changePassengerName(int id, String newName) throws NoSuchPassengerIdException, NonUniquePassengerNameException {
        _trainCompany.changePassengerName(id, newName);
    }

    /**
     * Returns a list of stations of the TrainCompany associated with the TicketOffice.
     *
     * @return a list of stations of the TrainCompany associated with the TicketOffice.
     */
    public List<Station> getStations() {
        return _trainCompany.getStations();
    }

    public void addStation(String name) throws NonUniquePassengerNameException {
        _trainCompany.addStationName(name);
    }

    /**
     * Erases the TrainCompany's passengers list.
     */
    public void reset() {
        _trainCompany.erasePassengers();
    }

    /**
     * Saves all the data into the TicketOffice's associated file.
     *
     * @throws MissingFileAssociationException if there is no file associated with the TicketOffice.
     * @throws IOException if an I/O Error occurs during serialization.
     */
    public void save() throws MissingFileAssociationException, IOException {
        if (_file == null)
            throw new MissingFileAssociationException();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(_file))) {
            out.writeObject(_trainCompany);
        }
    }

    /**
     * Associates a file with the TicketOffice.
     *
     * @param filename of the file to associate.
     */
    public void associateFile(String filename) {
        _file = filename;
    }

    /**
     * Returns the name of the file associated with the TicketOffice.
     *
     * @return the name of the file associated with the TicketOffice.
     */
    public String getAssociatedFile() {
        return _file;
    }

    /**
     * Loads all the data from the file TicketOffice's associated file.
     *
     * @param filename from which the data will be load from.
     * @throws IOException if an I/O Error occurs during serialization.
     * @throws ClassNotFoundException class of a serializable object cannot be found.
     */
    public void load(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            _trainCompany = (TrainCompany) in.readObject();
        }
    }

    /**
     * Imports the data from the file to the TrainCompany associated with the TicketOffice.
     *
     * @param datafile from which the data will be imported from.
     * @throws ImportFileException if any error is found while parsing the file.
     */
    public void importFile(String datafile) throws ImportFileException {
        new Parser().parseFile(datafile, _trainCompany);
    }

    /**
     * Returns a list of all the possible itineraries that conform with the specifications given as parameters
     * and saves it temporarily to the passenger with the passenger id given as parameter.
     *
     * @param passengerId id of the passenger.
     * @param strDeparture name of the departure station.
     * @param strArrival name of the arrival station.
     * @param strDate string representing the date for which the itineraries should be searched.
     * @param strTime string representing the departure time for which the itineraries should be searched.
     * @return a list of all the possible itineraries that conform with the specifications given as parameters.
     * @throws NoSuchPassengerIdException if there is no passenger with the id given as parameter.
     * @throws NoSuchStationNameException if there is no station with either of the names given as parameter.
     * @throws BadDateSpecificationException if the format of the string representing the date doesn't conform
     *                                       to the specification (YYYY-MM-DD).
     * @throws BadTimeSpecificationException if the format of the string representing the departure time doesn't conform
     *                                       to the specification (HH:MM).
     */
    public List<Itinerary> getItineraries(int passengerId, String strDeparture, String strArrival, String strDate, String strTime) throws NoSuchPassengerIdException, NoSuchStationNameException, BadDateSpecificationException, BadTimeSpecificationException {
        Passenger passenger = getPassengerById(passengerId);

        Station departure = _trainCompany.getStation(strDeparture);
        Station arrival = _trainCompany.getStation(strArrival);

        final LocalDate date;
        final LocalTime time;

        try {
            date = LocalDate.parse(strDate);
        } catch (DateTimeParseException dtpe) {
            throw new BadDateSpecificationException(strDate);
        }

        try {
            time = LocalTime.parse(strTime);
        } catch (DateTimeParseException dtpe) {
            throw new BadTimeSpecificationException(strTime);
        }

        passenger.setTempItineraries(_trainCompany.searchItineraries(departure, arrival, date, time));

        return passenger.getTempItineraries();
    }

    /**
     * Permanently adds an itinerary from the passenger's temporary list.
     *
     * @param passengerId the id of the passenger to add the itinerary.
     * @param itineraryChoice the id of the itinerary (from the passenger's temporary list) to add.
     * @throws NoSuchPassengerIdException if there is no passenger with the id given as parameter.
     * @throws NoSuchItineraryChoiceException if there is no itinerary with such id in the passenger's temporary list.
     */
    public void commitItinerary(int passengerId, int itineraryChoice) throws NoSuchPassengerIdException, NoSuchItineraryChoiceException {
        List<Itinerary> itineraries = getPassengerById(passengerId).getTempItineraries();

        if (itineraryChoice < 0 || itineraryChoice > itineraries.size())
            throw new NoSuchItineraryChoiceException(passengerId, itineraryChoice);

        if (itineraryChoice != 0)
            _trainCompany.addItinerary(passengerId, itineraries.get(itineraryChoice - 1));
    }
}
