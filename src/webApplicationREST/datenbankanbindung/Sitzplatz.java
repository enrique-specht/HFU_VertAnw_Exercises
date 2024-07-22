package webApplicationREST.datenbankanbindung;

public class Sitzplatz {
	public final int sitznummer;
	public final Zustand zustand;
	public final String reservierungsname;
	
	Sitzplatz(int sitznummer, Zustand zustand, String reservierungsname) {
		this.sitznummer = sitznummer;
		this.zustand = zustand;
		this.reservierungsname = reservierungsname;
	}
	
	public String toString() {
		return sitznummer + " " + zustand + " " + reservierungsname;
	}
}
