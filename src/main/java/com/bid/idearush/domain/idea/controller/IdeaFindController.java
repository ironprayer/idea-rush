package com.bid.idearush.domain.idea.controller;

import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.service.IdeaFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ideas")
public class IdeaFindController {

    private final IdeaFindService ideaFindService;

    @GetMapping
    public List<IdeaResponse> findAllQuery(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
    ) {
        return ideaFindService.findAll(keyword, category);
    }

    @GetMapping("/{id}")
    public IdeaResponse findOneQuery(
            @PathVariable Long id
    ) {
        return ideaFindService.findOne(id);
    }

}
