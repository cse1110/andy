package delft;

import java.time.LocalDate;
import java.util.List;

class ElementNotFoundException extends RuntimeException {
}

enum Destination {
	GREECE, MALTA, TAHITI, ZANZIBAR
}

class Info {

	private LocalDate startDate;

	private LocalDate endDate;

	private Destination destination;

	public Info(LocalDate startDate, LocalDate endDate, Destination destination) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.destination = destination;
	}
}

class Trip {

	private Long id;

	private String name;

	private Info info;

	private Integer price;

	private Integer capacity;

	public Trip(String name, Info info, Integer price, Integer capacity) {
		this.name = name;
		this.info = info;
		this.price = price;
		this.capacity = capacity;
	}

	public Integer getCapacity() {
		return capacity;
	}
}

interface TripRepository {

	Trip getTripById(Long id) throws ElementNotFoundException;

	void update(Trip trip, int capacity);
}

class Person {

	private String firstName;

	private String middleName;

	private String lastName;

	private LocalDate dateOfBirth;

	public Person(String firstName, String middleName, String lastName, LocalDate dateOfBirth) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}
}

class Reservation {

	private String code;

	private Trip trip;

	private List<Person> people;

	public Reservation(Trip trip, List<Person> people) {
		this.trip = trip;
		this.people = people;
	}

	public List<Person> getPeople() {
		return people;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Reservation that = (Reservation) o;
		if (code != null ? !code.equals(that.code) : that.code != null)
			return false;
		if (trip != null ? !trip.equals(that.trip) : that.trip != null)
			return false;
		return people != null ? people.equals(that.people) : that.people == null;
	}
}

interface ReservationRepository {

	void save(Reservation reservation);

	List<Reservation> getAllReservationsByTrip(Trip trip);
}

class SoftWhere {

	private final TripRepository tRepository;

	private final ReservationRepository rRepository;

	public SoftWhere(TripRepository tRepository, ReservationRepository rRepository) {
		this.tRepository = tRepository;
		this.rRepository = rRepository;
	}

	/**
	 * The capacity that is left for a specific trip.
	 *
	 * @param trip
	 *            the trip
	 * @return the number of spots left on a trip
	 */
	private int capacityLeft(Trip trip) {
		return trip.getCapacity() - rRepository.getAllReservationsByTrip(trip).stream().map(r -> r.getPeople().size())
				.reduce(0, Integer::sum);
	}

	/**
	 * Makes a reservation for a trip for a list of people if there is enough
	 * capacity left for them.
	 *
	 * @param tripId
	 *            the id of the trip
	 * @param people
	 *            the people who want to reserve the trip
	 * @return true if the reservation is successful
	 */
	public boolean makeReservation(Long tripId, List<Person> people) {
		try {
			Trip trip = tRepository.getTripById(tripId);
			if (capacityLeft(trip) < people.size())
				return false;
			rRepository.save(new Reservation(trip, people));
			return true;
		} catch (ElementNotFoundException e) {
			return false;
		}
	}
}
