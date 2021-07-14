package mmt.app.itineraries;

import mmt.app.exceptions.*;
import mmt.core.Itinerary;
import mmt.core.TicketOffice;
import mmt.core.exceptions.*;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Form;
import pt.tecnico.po.ui.Input;

import java.util.List;

/**
 * §3.4.3. Add new itinerary.
 */
public class DoRegisterItinerary extends Command<TicketOffice> {

    private Input<Integer> _passengerId;
    private Input<String> _departure;
    private Input<String> _arrival;
    private Input<String> _date;
    private Input<String> _time;
    private Form _choice;
    private Input<Integer> _itineraryChoice;


    /**
     * @param receiver
     */
    public DoRegisterItinerary(TicketOffice receiver) {
        super(Label.REGISTER_ITINERARY, receiver);
        _passengerId = _form.addIntegerInput(Message.requestPassengerId());
        _departure = _form.addStringInput(Message.requestDepartureStationName());
        _arrival = _form.addStringInput(Message.requestArrivalStationName());
        _date = _form.addStringInput(Message.requestDepartureDate());
        _time = _form.addStringInput(Message.requestDepartureTime());
        _choice = new Form();
        _itineraryChoice = _choice.addIntegerInput(Message.requestItineraryChoice());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        try {
            _form.parse();
            List<Itinerary> itineraries = _receiver.getItineraries(_passengerId.value(), _departure.value(), _arrival.value(), _date.value(), _time.value());
            for (int i = 0; i < itineraries.size(); i++)
                _display.addLine("\n" + itineraries.get(i).printItinerary(i + 1));
            _display.display();
            if (!itineraries.isEmpty()) {
                _choice.parse();
                _receiver.commitItinerary(_passengerId.value(), _itineraryChoice.value());
            }
        } catch (NoSuchPassengerIdException e) {
            throw new NoSuchPassengerException(e.getId());
        } catch (NoSuchStationNameException e) {
            throw new NoSuchStationException(e.getName());
        } catch (NoSuchItineraryChoiceException e) {
            throw new NoSuchItineraryException(e.getPassengerId(), e.getItineraryId());
        } catch (BadDateSpecificationException e) {
            throw new BadDateException(e.getDate());
        } catch (BadTimeSpecificationException e) {
            throw new BadTimeException(e.getTime());
        }
    }
}
