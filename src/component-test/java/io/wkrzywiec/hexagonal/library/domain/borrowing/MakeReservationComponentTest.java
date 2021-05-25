package io.wkrzywiec.hexagonal.library.domain.borrowing;

import io.wkrzywiec.hexagonal.library.domain.BaseComponentTest;
import io.wkrzywiec.hexagonal.library.domain.borrowing.application.model.BookStatus;
import io.wkrzywiec.hexagonal.library.domain.borrowing.application.model.ChangeBookStatusRequest;
import io.wkrzywiec.hexagonal.library.domain.borrowing.core.model.BookReservationCommand;
import io.wkrzywiec.hexagonal.library.domain.inventory.infrastructure.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

public class MakeReservationComponentTest extends BaseComponentTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Reserve available book")
    @Sql({"/book-and-user.sql", "/available-book.sql"})
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void givenBookIsAvailable_thenMakeReservation_thenBookIsReserved() {
        //given
        Long homoDeusBookId = databaseHelper.getHomoDeusBookId();
        Long activeUserId = databaseHelper.getJohnDoeUserId();

        ChangeBookStatusRequest reservationRequest =
                ChangeBookStatusRequest.builder()
                        .userId(activeUserId)
                        .status(BookStatus.RESERVED)
                        .build();

        //when
        given()
                .contentType("application/json")
                .body(reservationRequest)
        .when()
                .patch( baseURL + "/books/" + homoDeusBookId + "/status")
                .prettyPeek()
        .then();

        Long reservationId = databaseHelper.getPrimaryKeyOfReservationByBookId(homoDeusBookId);
        assertTrue(reservationId > 0);
    }
}
