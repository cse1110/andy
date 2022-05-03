package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.mockito.*;


class SoftWhereTests {

    //Methods have setup inside them, since I cannot use @BeforeEach and am
    //not sure if if I write a separate method to setup this will work, so yeah
    //there is a ton of code duplication to make sure grading will
    //work as expected. I also need to not have any dependency between tests,
    //so I cannot depend on one test making the setup. I will also not risk
    //putting setup into lines 26-32 without a method because I have no clue
    //what your grading system does and if it would work.
    //I have just chosen the safest way.

    ReservationRepository reservationRepository;
    TripRepository tripRepository;
    SoftWhere softwhere;
    Person person1, person2, person3, person4, person5, person6;

    Trip trip1, trip2, trip3;
    Info info1, info2, info3;

    Reservation res1, res2, res3, res4;

    //test when it is possible to register for the given trip
    @Test
    void testPossibleToRegister() {
        reservationRepository = mock(ReservationRepository.class);
        tripRepository = mock(TripRepository.class);

        softwhere = new SoftWhere(tripRepository, reservationRepository);

        person1 = new Person("fname1", "mname1", "lname1", LocalDate.of(1998, 2, 5));
        person4 = new Person("fname4", "mname4", "lname4", LocalDate.of(1995, 11, 17));
        person5 = new Person("fname5", "mname5", "lname5", LocalDate.of(1978, 1, 1));
        info2 = new Info(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 7, 15), Destination.MALTA);
        trip2 = new Trip("Trip to Malta", info2, 2050, 3);
        res4 = new Reservation(trip2, new ArrayList<>(List.of(person4, person5)));

        when(tripRepository.getTripById(2L)).thenReturn(trip2);
        when(reservationRepository.getAllReservationsByTrip(trip2)).thenReturn(new ArrayList<>(List.of(res4)));

        List<Person> people = new ArrayList<>(List.of(person1));

        assertTrue(softwhere.makeReservation(2L, people));

        verify(tripRepository, times(1)).getTripById(2L);
        verify(reservationRepository, times(1)).getAllReservationsByTrip(trip2);
        verify(reservationRepository, times(1)).save(new Reservation(trip2, people));

        verifyNoMoreInteractions(tripRepository);
        verifyNoMoreInteractions(reservationRepository);
    }

    //test when it is impossible to make a reservation
    //as there are not enough places left
    @Test
    void testNotEnoughPlacesLeft() {
        reservationRepository = mock(ReservationRepository.class);
        tripRepository = mock(TripRepository.class);

        softwhere = new SoftWhere(tripRepository, reservationRepository);

        person3 = new Person("fname3", "mname3", "lname3", LocalDate.of(1998, 11, 17));
        person4 = new Person("fname4", "mname4", "lname4", LocalDate.of(1995, 11, 17));

        person1 = new Person("fname1", "mname1", "lname1", LocalDate.of(1998, 2, 5));
        person2 = new Person("fname2", "mname2", "lname2", LocalDate.of(2001, 4, 3));
        person5 = new Person("fname5", "mname5", "lname5", LocalDate.of(1978, 1, 1));
        person6 = new Person("fname6", "mname6", "lname6", LocalDate.of(2004, 10, 4));
        info1 = new Info(LocalDate.of(2021, 6, 24), LocalDate.of(2021, 6, 30), Destination.GREECE);
        trip1 = new Trip("Trip to Greece", info1, 1999, 5);
        res1 = new Reservation(trip1, new ArrayList<>(List.of(person1, person2)));
        res2 = new Reservation(trip1, new ArrayList<>(List.of(person5, person6)));

        when(tripRepository.getTripById(1L)).thenReturn(trip1);
        when(reservationRepository.getAllReservationsByTrip(trip1)).thenReturn(new ArrayList<>(List.of(res1, res2)));

        List<Person> people = new ArrayList<>(List.of(person3, person4));

        assertFalse(softwhere.makeReservation(1L, people));

        verify(tripRepository, times(1)).getTripById(1L);
        verify(reservationRepository, times(1)).getAllReservationsByTrip(trip1);

        verifyNoMoreInteractions(tripRepository);
        verifyNoMoreInteractions(reservationRepository);
    }
}

