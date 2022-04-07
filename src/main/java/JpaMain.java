import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import domain.entity.Address;
import domain.Genre;
import domain.RoleType;
import domain.dto.ScreenInfoDTO;
import domain.dto.SeatDTO;
import domain.dto.TicketInfoDTO;
import domain.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class JpaMain {
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("cinema");
    static QMovie qMovie = QMovie.movie;
    static QTheater qTheater = QTheater.theater;
    static QSeat qSeat = QSeat.seat;
    static QScreen qScreen = QScreen.screen;
    static QTicket qTicket = QTicket.ticket;
    static QTicketSeat qTicketSeat = QTicketSeat.ticketSeat;
    static QMovieWorker qMovieWorker = QMovieWorker.movieWorker;
    static QWorker qWorker = QWorker.worker;

    public static void main(String[] args) {
        // 필요없는 양방향은 삭제하기 - insert, getReference
        // 영속성 컨텍스트에 없는 엔티티를 검색 조건으로 사용해 보기
        // insert 할 때 select 안 나가게

//        insertData();
//
//        // 1 : 사용자 추가 - 임베디드 값타입
//        createUser("고길동", 54, "서울", "경리단길", "18392");
//
//        // 2 : 사용자 정보 수정 - MappedSuperClass
//        editUserName(1L, "아무개");
//
//        // 3 : 영화 정보 조회 - 상세 정보
//        showMovieInfo(1L);
//
//        // 4 : 영화 정보 조회 - 검색 조건(QueryDSL)
//        findMovie(1L, 3L, null);
//        findMovie("이터널스_감독", "이터널스_주연2", null);
//
//        // 5 : 영화 정보 조회 - 페이징
//        showMovies(2);
//
//        // 6 : 상영 정보 조회
//        showScreenInfo(1L);
//
//        // 7 : 예매
//        showScreenInfo(1L);
//        ticketing(3L, 1L, Arrays.asList(5L, 8L));
//        showTicketInfo(3L);
//        showScreenInfo(1L);
//
//        // 8 : 예매 취소
//        showTicketInfo(3L);
//        showScreenInfo(1L);
//        cancelTicket(3L);
//        showTicketInfo(3L);
//        showScreenInfo(1L);
//
//        // 9 : 예매 정보
//        showTicketInfo(1L);
//
//        // 10 : 사용자 삭제 : 영속성 전이
//        deleteUser(3L);

        emf.close();
    }

    public static void doOneTransaction(OneTransaction transaction) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            transaction.execute(em, tx);
        }
        catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public static void insertData() {
        doOneTransaction((em, tx) -> {
            tx.begin();

            User[] users = {
                    new User("홍길동", 45, new Address("경북 구미시", "대학로", "39177")),
                    new User("임꺽정", 22, new Address("경북 구미시", "대학로", "39177"))
            };
            for (int i = 0; i < users.length; i++) {
                em.persist(users[i]);
            }

            Movie[] movies = {
                    new Movie("이터널스", LocalDate.of(2021, 11, 3), Genre.ACTION, 155),
                    new Movie("연애 빠진 로맨스", LocalDate.of(2021, 11, 24), Genre.ROMANCE, 94),
                    new Movie("엔칸토: 마법의 세계", LocalDate.of(2021, 11, 24), Genre.ANIMATION, 109),
                    new Movie("프렌치 디스패치", LocalDate.of(2021, 11, 18), Genre.ROMANCE, 107)
            };
            for (int i = 0; i < movies.length; i++) {
                em.persist(movies[i]);

                Director director = new Director(
                        movies[i].getName() + "_감독",
                        LocalDate.of(2000, i + 1, 2),
                        null);
                em.persist(director);

                MovieWorker movieWorker = new MovieWorker(RoleType.DIRECTOR);
                movieWorker.setMovie(movies[i]);
                movieWorker.setWorker(director);
                em.persist(movieWorker);

                for (int j = 0; j < 2; j++) {
                    Actor actor = new Actor(
                            movies[i].getName() + "_주연" + (j+1),
                            LocalDate.of(2000, i + 5, j + 12),
                            null,
                            null);
                    em.persist(actor);

                    movieWorker = new MovieWorker(RoleType.LEAD);
                    movieWorker.setMovie(movies[i]);
                    movieWorker.setWorker(actor);
                    em.persist(movieWorker);
                }
                for (int j = 0; j < 3; j++) {
                    Actor actor = new Actor(
                            movies[i].getName() + "_조연" + (j+1),
                            LocalDate.of(2000, i + 9, j + 22),
                            null,
                            null);
                    em.persist(actor);

                    movieWorker = new MovieWorker(RoleType.SUPPORTING);
                    movieWorker.setMovie(movies[i]);
                    movieWorker.setWorker(actor);
                    em.persist(movieWorker);
                }
            }

            Theater[] theaters = {
                    new Theater("1상영관", 1),
                    new Theater("2상영관", 2)
            };
            Screen[][][] screens = new Screen[theaters.length][3][3];
            Seat[][][] seats = new Seat[theaters.length][2][5];
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            for (int i = 0; i < theaters.length; i++) {
                em.persist(theaters[i]);

                for (int j = 0; j < seats[i].length; j++) {
                    for (int k = 0; k < seats[i][j].length; k++) {
                        seats[i][j][k] = new Seat((char)(j + 'A'), k + 1, true);
                        seats[i][j][k].setTheater(theaters[i]);
                        em.persist(seats[i][j][k]);
                    }
                }

                for (int j = 0; j < screens[i].length; j++) {
                    for (int k = 0; k < screens[i][j].length; k++) {
                        screens[i][j][k] = new Screen(now.plusDays(j).plusHours(3 * k));
                        screens[i][j][k].setMovie(movies[i]);
                        screens[i][j][k].setTheater(theaters[i]);
                        em.persist(screens[i][j][k]);
                    }
                }
            }

            for (int i = 0; i < users.length; i++) {
                Ticket ticket = new Ticket();
                ticket.setUser(users[i]);
                ticket.setScreen(screens[0][0][0]);
                em.persist(ticket);

                for (int j = 0; j < 2; j++) {
                    TicketSeat ticketSeat = new TicketSeat();
                    ticketSeat.setTicket(ticket);
                    ticketSeat.setSeat(seats[0][i][j]);
                    em.persist(ticketSeat);
                }
            }

            tx.commit();
        });
    }

    public static void createUser(String name, Integer age, String city, String street, String zipCode) {
        doOneTransaction((em, tx) -> {
            tx.begin();
            em.persist(new User(name, age, new Address(city, street, zipCode)));
            tx.commit();
        });
    }

    public static void editUserName(Long userId, String name) {
        doOneTransaction((em, tx) -> {
            tx.begin();
            em.find(User.class, userId).setName(name);
            tx.commit();
        });
    }

    public static void showMovieInfo(Long moveId) {
        doOneTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            Movie movie = query.selectDistinct(qMovie)
                    .from(qMovie)
                    .join(qMovie.movieWorkers, qMovieWorker).fetchJoin()
                    .join(qMovieWorker.worker, qWorker).fetchJoin()
                    .where(qMovie.id.eq(moveId))
                    .fetchOne();
            System.out.println(movie);
        });
    }

    public static void findMovie(Long directorId, Long actorId, LocalDate openingDate) {
        doOneTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            List<Movie> movies = query.selectDistinct(qMovie)
                    .from(qMovie)
                    .join(qMovie.movieWorkers, qMovieWorker).fetchJoin()
                    .join(qMovieWorker.worker, qWorker).fetchJoin()
                    .where(
                            workedWith(directorId),
                            workedWith(actorId),
                            movieOpeningDateEq(openingDate))
                    .fetch();
            movies.stream().forEach(e -> System.out.println(e));
        });
    }

    public static void findMovie(String directorName, String actorName, LocalDate openingDate) {
        doOneTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            List<Movie> movies = query.selectDistinct(qMovie)
                    .from(qMovie)
                    .join(qMovie.movieWorkers, qMovieWorker).fetchJoin()
                    .join(qMovieWorker.worker, qWorker).fetchJoin()
                    .where(
                            workedWith(directorName),
                            workedWith(actorName),
                            movieOpeningDateEq(openingDate))
                    .fetch();
            movies.stream().forEach(e -> System.out.println(e));
        });
    }

    public static BooleanExpression workedWith(Long workerId) {
        if (workerId == null)
            return null;
        return JPAExpressions
                .selectDistinct(qMovieWorker)
                .from(qMovieWorker)
                .where(qMovieWorker.worker.id.eq(workerId),
                        qMovieWorker.movie.eq(qMovie))
                .exists();
    }

    public static BooleanExpression workedWith(String workerName) {
        if (workerName == null)
            return null;
        return JPAExpressions
                .selectDistinct(qMovieWorker)
                .from(qMovieWorker)
                .join(qMovieWorker.worker, qWorker)
                .where(qWorker.name.eq(workerName),
                        qMovieWorker.movie.eq(qMovie))
                .exists();
    }

    public static BooleanExpression movieOpeningDateEq(LocalDate openingDate) {
        if (openingDate == null)
            return null;
        return qMovie.openingDate.eq(openingDate);
    }

    public static void showMovies(int page) {
        int limit = 2;
        doOneTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            List<Movie> movies = query.selectFrom(qMovie)
                    .orderBy(qMovie.openingDate.desc(), qMovie.name.asc())
                    .offset((page - 1) * limit)
                    .limit(limit)
                    .fetch();
            movies.stream().forEach(e -> System.out.println(e));
            System.out.println("- Page " + page + " -");
        });
    }

    public static void showScreenInfo(Long screenId) {
        doOneTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);

            ScreenInfoDTO screenInfoDTO = query
                    .select(Projections.fields(ScreenInfoDTO.class,
                            qMovie.name.as("movieName"),
                            qScreen.startTime.as("startTime"),
                            qScreen.endTime.as("endTime")))
                    .from(qScreen)
                    .join(qScreen.movie, qMovie)
                    .where(qScreen.id.eq(screenId))
                    .fetchOne();

            List<SeatDTO> seatDTOs = query
                    .select(Projections.fields(SeatDTO.class,
                            qSeat.row.as("seatRow"),
                            qSeat.column.as("seatCol"),
                            qSeat.notIn(
                                    JPAExpressions.select(qSeat)
                                            .from(qTicketSeat)
                                            .join(qTicketSeat.ticket, qTicket).on(qTicket.isCanceled.eq(false))
                                            .join(qTicket.screen, qScreen).on(qScreen.id.eq(screenId))
                                            .join(qTicketSeat.seat, qSeat)
                            ).as("canTicketing")))
                    .from(qSeat)
                    .join(qScreen).on(qScreen.theater.eq(qSeat.theater), qScreen.id.eq(screenId))
                    .where(qSeat.status.eq(true))
                    .fetch();
            screenInfoDTO.setSeats(seatDTOs);

            System.out.println("screenInfoDTO = " + screenInfoDTO);
        });
    }

    public static void ticketing(Long userId, Long screenId, List<Long> seatIds) {
        doOneTransaction((em, tx) -> {
            tx.begin();

//            em.createNativeQuery("INSERT INTO TICKETS (USER_ID, SCREEN_ID, IS_CANCELED) VALUES (:userId, :screenId, 0);")
//                    .setParameter("userId", userId)
//                    .setParameter("screenId", screenId)
//                    .executeUpdate();
//
//            Object result = em.createNativeQuery("SELECT @@IDENTITY;").getSingleResult();
//            Long ticketId = ((BigInteger) result).longValue();
//            for (Long seatId : seatIds) {
//                em.createNativeQuery("INSERT INTO TICKET_SEATS (TICKET_ID, SEAT_ID) VALUES (:ticketId, :seatId);")
//                        .setParameter("ticketId", ticketId)
//                        .setParameter("seatId", seatId)
//                        .executeUpdate();
//            }

            Ticket ticket = new Ticket();
            ticket.setUser(em.getReference(User.class, userId));
            ticket.setScreen(em.getReference(Screen.class, screenId));
            em.persist(ticket);

            for (Long seatId : seatIds) {
                TicketSeat ticketSeat = new TicketSeat();
                ticketSeat.setTicket(ticket);
                ticketSeat.setSeat(em.getReference(Seat.class, seatId));
                em.persist(ticketSeat);
            }

            tx.commit();
        });
    }

    public static void cancelTicket(Long ticketId) {
        doOneTransaction((em, tx) -> {
            tx.begin();
            em.find(Ticket.class, ticketId).setIsCanceled(true);
            tx.commit();
        });
    }

    public static void showTicketInfo(Long ticketId) {
        doOneTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);

            TicketInfoDTO ticketInfoDTO = query
                    .select(Projections.fields(TicketInfoDTO.class,
                            qMovie.name.as("movieName"),
                            qTheater.name.as("theaterName"),
                            qScreen.startTime.as("startTime"),
                            qTicket.isCanceled.as("isCanceled")))
                    .from(qTicket)
                    .join(qTicket.screen, qScreen)
                    .join(qScreen.movie, qMovie)
                    .join(qScreen.theater, qTheater)
                    .where(qTicket.id.eq(ticketId))
                    .fetchOne();

            List<SeatDTO> seats = query
                    .select(Projections.fields(SeatDTO.class,
                            qSeat.row.as("seatRow"),
                            qSeat.column.as("seatCol")))
                    .from(qTicketSeat)
                    .join(qTicketSeat.seat, qSeat)
                    .where(qTicketSeat.ticket.id.eq(ticketId))
                    .fetch();
            ticketInfoDTO.setSeats(seats);

            System.out.println("예매 티켓(" + ticketId + ") 정보 = " + ticketInfoDTO);
        });
    }

    public static void deleteUser(Long userId) {
        doOneTransaction((em, tx) -> {
            tx.begin();
            em.remove(em.find(User.class, userId));
            tx.commit();
        });
    }
}

@FunctionalInterface
interface OneTransaction {
    void execute(EntityManager em, EntityTransaction tx);
}