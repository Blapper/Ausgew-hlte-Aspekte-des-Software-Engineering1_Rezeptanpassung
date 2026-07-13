package assignment.dhbw.olaf.rezeptanpassung.web;

/**
 * Vorlage Prüfungen für Formular-Eingaben,
 * für die Controller. Überprüft Ob es sich um eine Zahl handelt.
 */
public final class EingabeValidierung {

    private EingabeValidierung() {
    }

    public static boolean istZahl( String eingabe ) {

        try {

            Double.parseDouble( eingabe.trim() );

            return true;

        } catch ( NumberFormatException e ) {

            return false;
        }
    }
}
