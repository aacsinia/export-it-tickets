package com.nudgeit.rest;


import com.nudgeit.domain.ExportStatus;
import com.nudgeit.domain.Ticket;
import com.nudgeit.domain.repository.TicketRepository;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
public class TicketService {

    private static final long DOWNLOAD_TIMEOUT = 1000 * 30;

    private static Random random = new Random();

    // TOOD: replace this with a fast distributed solution (cache, database) - used this for a simplified solution (will lead to OOO!!!)
    private final Map<Long, ExportStatus> exportStatus = new HashMap<>();

    private final TicketRepository ticketRepository;

    @Autowired
    TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket get(Long id) {
        return ticketRepository.findOne(id);
    }

    public ExportStatus startExport(Long total) {
        ExportStatus es = new ExportStatus(total);
        // TODO: use persistence generated ID or change to check for uniques
        es.setId((long) random.nextInt()); // avoid JavaScript only supports 53 bit integers
        exportStatus.put(es.getId(), es);
        return es;
    }

    public void updateExportStatus(Long exportId, Long delta) {
        if (null == exportStatus.get(exportId)) {
            throw new IllegalStateException("no export with id " + exportId);
        }
        exportStatus.get(exportId).incCurrent(delta);
    }

    /**
     * Return items count based on filter request
     * TODO: implement filter for count on reposstoitory
     */
    @Transactional(readOnly = true)
    public Long exportCount() {
        return ticketRepository.count();
    }

    public ExportStatus getExportStatus(Long exportId) {
        ExportStatus status = exportStatus.get(exportId);
        if (null == status) {
            return null;
        }
        if (System.currentTimeMillis() - status.getLastUpdate() > DOWNLOAD_TIMEOUT) {
            status.setError("Download timeout");
            exportStatus.remove(exportId);
        }
        return status;
    }

    /**
     * @param out to write tickets to a zip stream
     *            TODO: implement filter for export on repository
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public void export(OutputStream out, Long exportId) throws IOException {
        try (Stream<Ticket> ticketStream = ticketRepository.export()) {
            ZipOutputStream zipOut = new ZipOutputStream(out);
            zipOut.setLevel(Deflater.BEST_COMPRESSION);
            zipOut.putNextEntry(new ZipEntry("it-tickets.csv"));
            PrintWriter writer = new PrintWriter(zipOut);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            ticketStream.forEach(ticket -> {
                try {
                    beanToCsv.write(ticket);
                    this.updateExportStatus(exportId, 1L);
                } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.flush();
            zipOut.closeEntry();
            zipOut.finish();
            zipOut.close();
            out.close();
        }
    }

}
