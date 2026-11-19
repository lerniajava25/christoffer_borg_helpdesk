package com.helpdesk.controller;

import com.helpdesk.model.Comment;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.User;
import com.helpdesk.service.CommentService;
import com.helpdesk.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
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
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;
    private final CommentService commentService;

    public TicketController(TicketService ticketService, UserService userService, CommentService commentService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/tickets")
    public String tickets(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> result = userService.getUserByUsername(username);
        User user = result.orElseThrow();
        List<Ticket> userTickets = ticketService.getTicketsByUserId(user.getId());
        model.addAttribute("tickets", userTickets);
        model.addAttribute("username", username);
        return "tickets";
    }

    @GetMapping("/tickets/new")
    public String newTicket() {
        return "new-ticket";
    }

    @PostMapping("/tickets/new")
    public String createTicket(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            Principal principal) {
        if (title.isBlank() || description.isBlank() || title.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String username = principal.getName();
        Optional<User> result = userService.getUserByUsername(username);
        User user = result.orElseThrow();
        Ticket ticket = new Ticket(user, title, description);
        ticketService.saveTicket(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/tickets/{id}")
    public String ticketDetails(@PathVariable("id") Long id, Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> result = userService.getUserByUsername(username);
        User user = result.orElseThrow();
        Optional<Ticket> ticketResult = ticketService.getTicketById(id);
        Ticket ticket = ticketResult.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        if (!user.getId().equals(ticket.getCreatedBy().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("ticket", ticket);
        List<Comment> comments = commentService.getCommentsByTicketId(id);
        model.addAttribute("comments", comments);
        return "ticket-details";
    }

    @PostMapping("/tickets/{id}/comments")
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
        if (!user.getId().equals(ticket.getCreatedBy().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Comment comment = new Comment(content, ticket, user);
        commentService.saveComment(comment);
        return "redirect:/tickets/" + id;
        }
}
