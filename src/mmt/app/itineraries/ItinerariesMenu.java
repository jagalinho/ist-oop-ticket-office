package mmt.app.itineraries;

import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Menu;

/** Menu operations on itineraries. */
public class ItinerariesMenu extends Menu {

  /**
   * @param receiver
   */
  public ItinerariesMenu(TicketOffice receiver) {
    super(Label.TITLE, new Command<?>[] { //
        new DoShowAllItineraries(receiver), //
        new DoShowPassengerItineraries(receiver), //
        new DoRegisterItinerary(receiver),
        new DoShowMostExpensiveItinerary(receiver),//
    });
  }

}
