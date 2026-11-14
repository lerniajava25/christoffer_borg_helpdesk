package com.helpdesk.repository;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldSaveTicketWithUser() {
        User user = new User("Christoffer", "secret", "mail@mail.com", true);
        userRepository.save(user);
        Ticket ticket = new Ticket(user, "Fel", "Datorn startar inte");
        ticketRepository.save(ticket);

        Optional<Ticket> result = ticketRepository.findById(ticket.getId());
        Ticket foundTicket = result.orElseThrow();
        String title = foundTicket.getTitle();

        assertEquals("Fel", title);

        User ticketOwner = foundTicket.getCreatedBy();
        assertEquals("Christoffer", ticketOwner.getUsername());
    }
}