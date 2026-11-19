package com.helpdesk.controller;

import com.helpdesk.model.Comment;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.User;
import com.helpdesk.service.CommentService;
import com.helpdesk.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import com.helpdesk.model.Ticket;
import com.helpdesk.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AgentController {

   private final TicketService ticketService;
   private final UserService userService;
   private final CommentService commentService;

    public AgentController(TicketService ticketService, UserService userService, CommentService commentService) {
       this.ticketService = ticketService;
       this.userService = userService;
       this.commentService = commentService;
    }

    @GetMapping("/agent/tickets")
    public String allTickets(Model model) {

        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "agent-tickets";
    }

    @GetMapping("/agent/tickets/assigned")
    public String assignedTickets(Model model, Principal principal) {
        String agentName = principal.getName();
        Optional<User> result = userService.getUserByUsername(agentName);
        User userAgent = result.orElseThrow();
        List<Ticket> tickets = ticketService.getTicketsByAgentId(userAgent.getId());
        model.addAttribute("tickets", tickets);
        model.addAttribute("pageTitle", "Assigned tickets");
        return "agent-tickets";
    }

    @GetMapping("/agent/tickets/{id}")
    public String ticketDetails(@PathVariable("id") Long id, Model model) {

        Optional<Ticket> result = ticketService.getTicketById(id);
        Ticket ticket = result.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        model.addAttribute("ticket", ticket);
        List<Comment> comments = commentService.getCommentsByTicketId(id);
        model.addAttribute("comments", comments);
        return "agent-ticket-details";
    }

    @PostMapping("/agent/tickets/{id}/assign")
    public String assignTicket(@PathVariable("id") Long id,
                               Principal principal) {
       String username = principal.getName();
       Optional<User> result = userService.getUserByUsername(username);
       User agent = result.orElseThrow();
       Optional<Ticket> ticketResult = ticketService.getTicketById(id);
       Ticket ticket = ticketResult.orElseThrow(
               () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
       );
       if (ticket.getAssignedAgent() != null) {
           throw new ResponseStatusException(HttpStatus.CONFLICT);
       }
       ticketService.assignTicket(ticket, agent);

       return "redirect:/agent/tickets/" + id;
   }

    @PostMapping("/agent/tickets/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") TicketStatus status) {

        Optional<Ticket> ticketResult = ticketService.getTicketById(id);
        Ticket ticket = ticketResult.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        ticketService.updateStatus(ticket, status);

        return "redirect:/agent/tickets/" + id;
    }

    @PostMapping("/agent/tickets/{id}/comments")
    public String createComment(
            @PathVariable("id") Long id,
            @RequestParam("content") String content,
            Principal principal) {
        if(content.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String username = principal.getName();
        Optional<User> result = userService.getUserByUsername(username);
        User user = result.orElseThrow();
        Optional<Ticket> ticketResult = ticketService.getTicketById(id);
        Ticket ticket = ticketResult.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        Comment comment = new Comment(content, ticket, user);
        commentService.saveComment(comment);
        return "redirect:/agent/tickets/" + id;
    }

}
