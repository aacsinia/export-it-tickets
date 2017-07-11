package com.nudgeit.rest;

import com.nudgeit.domain.ExportStatus;
import com.nudgeit.domain.Ticket;
import com.nudgeit.rest.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The Ticket resource is used  to create, update, delete and list tickets.
 */
@RestController
@RequestMapping("/api/v1/tickets")
class TicketResource {

    private final TicketService ticketService;

    @Autowired
    TicketResource(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Retrieves  specific ticket with the given id
     *
     * @param id of the ticket
     * @return the ticket with the given id
     */
    @GetMapping(value = "/{id}")
    public Ticket get(@PathVariable Long id) {
        Ticket ticket = ticketService.get(id);
        if (null == ticket) {
            throw new NotFoundException("Ticket not found: " + id);
        }
        return ticket;
    }

    /**
     * Retrieves a list of tickets
     *
     * @param sort    fields name to sort by results.{@code +field_name} to sort ascending, {@code -field_name} to sort descending
     * @param filter  a map where the key is the field to filter and the value is the filtering value
     * @param page    page number assuming given {@code perPage} parameter
     * @param perPage number of items to return
     * @return the list of the tickets
     */
    @GetMapping()
    public List<Ticket> get(@RequestParam List<String> sort,
                            @RequestParam Map<String, String> filter,
                            @RequestParam(defaultValue = "0") Long page,
                            @RequestParam(defaultValue = "25", name = "per_page") Long perPage) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * Creates a new ticket
     *
     * @param ticket
     * @return created ticket
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping()
    public Ticket create(@RequestBody Ticket ticket) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Update the ticket having the given id
     *
     * @param id     of the ticket to be updated
     * @param ticket data to be updated
     * @return updated ticket
     */
    @PutMapping(value = "/{id}")
    public Ticket update(@PathVariable Long id, @RequestBody Ticket ticket) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Delete the ticket with the given id
     *
     * @param id of the ticket to be deleted
     */
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * @return a new export id that will be used to trigger export download
     */
    @GetMapping(value = "/export")
    public ExportStatus startExport() {
        // TODO count based onsort & filters (like ticket status, date's interval)
        long count = this.ticketService.exportCount();
        return this.ticketService.startExport(count);
    }

    /**
     * Export a list of tickets
     *
     * @param sort   fields name to sort by results.{@code +field_name} to sort ascending, {@code -field_name} to sort descending
     * @param filter a map where the key is the field to filter and the value is the filtering value
     * @return the list of the tickets
     */
    @GetMapping(value = "/export/{exportId}", produces = "application/zip")
    public void export(HttpServletResponse response, @PathVariable Long exportId) {
        // TODO implement sort & filters (like ticket status, date's interval)
        response.addHeader("Content-Type", "application/zip");
        response.addHeader("Content-Disposition", "attachment; filename=it-tickets.zip");
        response.setCharacterEncoding("UTF-8");
        try {
            this.ticketService.export(response.getOutputStream(), exportId);
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while exporting results", e);
        }
    }

    /**
     * Returns the status of an ongoing export
     *
     * @param exportId to retrieve the status
     * @return the export status
     */
    @GetMapping(value = "/export/{exportId}/status")
    public ExportStatus exportStatus(@PathVariable Long exportId) {
        ExportStatus status = this.ticketService.getExportStatus(exportId);
        if (status == null) {
            throw new NotFoundException("no export with this id");
        }
        return status;
    }
}
