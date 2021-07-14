package mmt.core;

/**
 * An interface class that represents a passenger's category.
 *
 * @author Grupo 38
 * @author João Galinho (87667)
 * @author Filipe Henriques (87653)
 * @version 1.0
 */
public interface Category {

    /**
     * Returns the discount of the category.
     *
     * @return the discount of the category.
     */
    double getDiscount();


    /**
     * Returns the category of the passenger given as parameter.
     *
     * @param passenger to get the category.
     * @return Category of the passenger.
     */
    Category getCategory(Passenger passenger);
}