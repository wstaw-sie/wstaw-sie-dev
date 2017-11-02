package drawer.com.bean;

import java.util.ArrayList;
import java.util.List;

public class Person {

	String firstNameBasic;
	String secondNameBasic;
//	String firstNameAddit;
//	String secondNameAddit;	
	String address;
	String intencja;
	String tekst;
	int omadla;
	public List<Integer> history; //historia wyciągnięta z bazy danych, aktualizowana wg potrzeb przy losowaniu
	List<Integer> historyStat; //historia wyciągnięta z bazy danych, niezmienna
	int id;
	
	public String getFirstNameBasic() {
		return firstNameBasic;
	}

	public String getSecondNameBasic() {
		return secondNameBasic;
	}

//	public String getFirstNameAddit() {
//		return firstNameAddit;
//	}
//
//	public String getSecondNameAddit() {
//		return secondNameAddit;
//	}

	public String getAddress() {
		return address;
	}

	public int getId() {
		return id;
	}
	
	public int getOmadla() {
		return omadla;
	}
	
	public List<Integer> getHistory() {
		return history;
	}

	public List<Integer> getHistoryStat() {
		return historyStat;
	}
	
	public Person(int id, String firstNameBasic, String secondNameBasic, String address, String intencja,
			int omadla, List<Integer> history, String tekst) {
		super();
		this.id = id;
		this.firstNameBasic = firstNameBasic;
		this.secondNameBasic = secondNameBasic;
		this.address = address;
		this.intencja=intencja;
		this.omadla=omadla;
		this.history =new ArrayList<Integer>();
		this.history= history;
		this.historyStat = history;
		this.tekst = tekst;
	}

	public String getIntencja() {
		return intencja;
	}

	public void setIntencja(String intencja) {
		this.intencja = intencja;
	}

	@Override
	public String toString() {
		return "Person [firstNameBasic=" + firstNameBasic
				+ ", secondNameBasic=" + secondNameBasic + ", address="
				+ address + ", intencja=" + intencja + ", tekst=" + tekst
				+ ", omadla=" + omadla + ", history=" + history
				+ ", historyStat=" + historyStat + ", id=" + id + "]";
	}
}
