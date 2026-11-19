package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.User;
import com.helpdesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByCreatedById(userId);
    }

    public List<Ticket> getTicketsByAgentId(Long agentId) {
        return ticketRepository.findByAssignedAgentId(agentId);
    }

    public Ticket assignTicket(Ticket ticket, User agent) {
        ticket.setAssignedAgent(agent);
        return ticketRepository.save(ticket);
    }

    public Ticket updateStatus(Ticket ticket, TicketStatus status) {
        ticket.setStatus(status);
    return ticketRepository.save(ticket);
    }

}
