package mmt.core;

import mmt.core.exceptions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

/**
 * Parser reads the file and copies the data to the TrainCompany.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
class Parser {

    /**
     * TrainCompany for which the data will be copied to.
     */
    private TrainCompany _trainCompany;

    /**
     * Parses the data from the file to the _trainCompany.
     *
     * @param fileName     of the file to read.
     * @param trainCompany to copy the data to.
     * @throws ImportFileException if any error is found while parsing the file.
     */
    void parseFile(String fileName, TrainCompany trainCompany) throws ImportFileException {
        _trainCompany = trainCompany;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException ioe) {
            throw new ImportFileException(ioe);
        }
    }

    /**
     * Parses the data contained in the string (which represents a line of the import file).
     *
     * @param line to parse.
     * @throws ImportFileException if any error is found while parsing the line.
     */
    private void parseLine(String line) throws ImportFileException {
        String[] components = line.split("\\|");

        switch (components[0]) {
            case "PASSENGER":
                parsePassenger(components);
                break;

            case "SERVICE":
                parseService(components);
                break;

            case "ITINERARY":
                parseItinerary(components);
                break;

            default:
                throw new ImportFileException("invalid type of line: " + components[0]);
        }
    }

    /**
     * Interprets the string components given as parameter as a passenger and pareses it to the _trainCompany.
     *
     * @param components to parse, that represent a passenger.
     * @throws ImportFileException if the components are not formatted correctly, or the method is
     *                             trying to parse a passenger with same name as an already existing one.
     */
    private void parsePassenger(String[] components) throws ImportFileException {
        if (components.length != 2)
            throw new ImportFileException("invalid number of arguments in passenger line: " + components.length);

        String passengerName = components[1];

        try {
            _trainCompany.addPassenger(passengerName);
        } catch (NonUniquePassengerNameException e) {
            throw new ImportFileException(e);
        }
    }

    /**
     * Interprets the string components given as parameter as a service and pareses it to the _trainCompany.
     *
     * @param components to parse, that represent a service.
     * @throws ImportFileException if the components are not formatted correctly, or the method is
     *                             trying to parse a service with an id that is the same as an already existing one or
     *                             a service with a duplicate station(s).
     */
    private void parseService(String[] components) throws ImportFileException {
        double cost = Double.parseDouble(components[2]);
        int serviceId = Integer.parseInt(components[1]);

        try {
            _trainCompany.addService(serviceId, cost);
        } catch (NonUniqueServiceIdException nuside) {
            throw new ImportFileException("Multiple services with same id. ID = " + serviceId);
        }

        for (int i = 3; i < components.length; i += 2) {
            LocalTime ltime = LocalTime.parse(components[i]);
            String stationName = components[i + 1];

            try {
                _trainCompany.addStation(serviceId, stationName, ltime);
            } catch (NoSuchServiceIdException | NonUniqueStationAtServiceException e) {
                throw new ImportFileException(e);
            }
        }
    }

    /**
     * Interprets the string components given as parameter as an itinerary of a passenger
     * and pareses it to the _trainCompany.
     *
     * @param components to parse, that represent an itinerary of a passenger.
     * @throws ImportFileException if the components are not formatted correctly, or the method is
     *                             trying to parse an itinerary with trips that correspond to services
     *                             or stations that don't exist.
     */
    private void parseItinerary(String[] components) throws ImportFileException {
        if (components.length < 4)
            throw new ImportFileException("Invalid number of elements in itinerary line: " + components.length);

        int passengerId = Integer.parseInt(components[1]);
        LocalDate date = LocalDate.parse(components[2]);

        Itinerary itinerary = new Itinerary(date);

        for (int i = 3; i < components.length; i++) {
            String segmentDescription[] = components[i].split("/");

            int serviceId = Integer.parseInt(segmentDescription[0]);
            String departureStation = segmentDescription[1];
            String arrivalStation = segmentDescription[2];

            try {
                if (!itinerary.addTripEnd(_trainCompany.getServiceById(serviceId), _trainCompany.getStation(departureStation), _trainCompany.getStation(arrivalStation)))
                    throw new ImportFileException();
            } catch (NoSuchServiceIdException | NoSuchStationNameException nsid) {
                throw new ImportFileException(nsid);
            }
        }

        try {
            _trainCompany.addItinerary(passengerId, itinerary);
        } catch (NoSuchPassengerIdException nspid) {
            throw new ImportFileException(nspid);
        }
    }
}