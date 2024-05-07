package datenbankanbindung;

public class Sitzplatz {
	protected final int sitznummer;
	protected final Zustand zustand;
	protected final String reservierungsname;
	
	Sitzplatz(int sitznummer, Zustand zustand, String reservierungsname) {
		this.sitznummer = sitznummer;
		this.zustand = zustand;
		this.reservierungsname = reservierungsname;
	}
	
	public String toString() {
		return sitznummer + " " + zustand + " " + reservierungsname;
	}
}
