package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.mapper.CreditCardMapper;
import com.krukovska.paymentsystem.persistence.model.CreditCard;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CreditCardRepositoryTest extends AbstractDBTest{
    private final CreditCardRepository repository = new CreditCardRepository(DataSource.getInstance(), new CreditCardMapper());

    @Test
    void getByCarNumber() throws SQLException {

        List<CreditCard> result = repository.findByField(new Field("card_number", "1111222233334444"));

        assertThat(result, hasSize(1));
        CreditCard card = result.get(0);
        assertThat(card.getId(), equalTo(2L));
        assertThat(card.getCardNumber(), equalTo("1111222233334444"));
        assertThat(card.getAccount().getId(), equalTo(2L));
        assertThat(card.isExpired(),equalTo(false));

    }

    @Test
    void getByAccountId() throws SQLException {

        List<CreditCard> result = repository.findByField(new Field("account_id", 1L));

        assertThat(result, hasSize(1));
        CreditCard card = result.get(0);
        assertThat(card.getId(), equalTo(1L));
        assertThat(card.getCardNumber(), equalTo("0000111122223333"));
        assertThat(card.getAccount().getId(), equalTo(1L));
        assertThat(card.isExpired(),equalTo(false));

    }


    @Test
    void getTableName() {
        assertThat(repository.getTableName(), equalToIgnoringCase("credit_cards"));
    }
}