package mmt.core;

import java.util.List;

/**
 * One Category of the passengers (Frequent).
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
enum Frequent implements Category {
    INSTANCE;

    /**
     * Returns the value representing the discount of the category.
     *
     * @return the discount of this category.
     */
    @Override
    public double getDiscount() {
        return 0.85;
    }

    /**
     * Returns a string representing this category.
     *
     * @return a string representing this category.
     */
    @Override
    public String toString() {
        return "FREQUENTE";
    }

    /**
     * Returns the category of the passenger given as parameter.
     *
     * @param passenger to get the category.
     * @return Category of the passenger.
     */
    @Override
    public Category getCategory(Passenger passenger) {
        List<Itinerary> itineraries = passenger.getItineraries();
        final double costs;
        if (itineraries.size() < 10)
            costs = itineraries.stream().mapToDouble(Itinerary::getCost).sum();
        else
            costs = itineraries.subList(itineraries.size() - 10, itineraries.size()).stream().mapToDouble(Itinerary::getCost).sum();
        if (costs > 2500)
            return Special.INSTANCE;
        else if (costs <= 250)
            return Normal.INSTANCE;
        return Frequent.INSTANCE;
    }
}