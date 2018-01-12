package com.aldona.library.controller;

import com.aldona.library.model.Queue;
import com.aldona.library.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.aldona.library.controller.QueueController.QUEUE_PATH;

@RestController
@RequestMapping(QUEUE_PATH)
public class QueueController {

    final static String QUEUE_PATH = "/queue";

    private QueueService queueService;

    @Autowired
    QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping
    public List<Queue> displayQueueToBook(Long idOfBook) {
        return queueService.displayQueueToBook(idOfBook);
    }

}
