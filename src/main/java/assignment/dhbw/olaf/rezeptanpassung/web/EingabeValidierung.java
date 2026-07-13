package assignment.dhbw.olaf.rezeptanpassung.web;

/**
 * Sammelt wiederverwendbare Prüfungen für Formular-Eingaben,
 * die von mehreren Controllern genutzt werden.
 */
public final class EingabeValidierung {

    private EingabeValidierung() {
    }

    /**
     * Prüft, ob eine Eingabe eine reine Zahl ist
     */
    public static boolean istZahl( String eingabe ) {

        try {

            Double.parseDouble( eingabe.trim() );

            return true;

        } catch ( NumberFormatException e ) {

            return false;
        }
    }
}
