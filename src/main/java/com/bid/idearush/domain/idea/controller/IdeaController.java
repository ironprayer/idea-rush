package com.bid.idearush.domain.idea.controller;

import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.service.IdeaService;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.global.security.AuthPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/ideas")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @PutMapping("{id}")
    void updateIdea(@PathVariable(name = "id") Long ideaId,
                    @RequestPart(name = "idea") IdeaRequest ideaRequest,
                    @RequestPart(required = false) MultipartFile image) {
        Long dummyUserId = 1L;

        ideaService.update(dummyUserId, ideaId, ideaRequest, image);

    }

    @DeleteMapping("{id}")
    void deleteIdea(@PathVariable(name = "id") Long ideaId) {
        Long dummyUserId = 1L;

        ideaService.deleteIdea(dummyUserId, ideaId);
    }

    @GetMapping
    public List<IdeaResponse> findAllIdea(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            @RequestParam Integer page
    ) {
        return ideaService.findAllIdea(keyword, category,page);
    }

    @GetMapping("/{id}")
    public IdeaResponse findOneQuery(
            @PathVariable Long id
    ) {
        return ideaService.findOneIdea(id);
    }

    @PostMapping
    public void createIdea(@RequestPart(name = "idea") IdeaRequest ideaRequest,
                           @RequestPart(required = false) MultipartFile image,
                           @AuthenticationPrincipal AuthPayload authPayload) {
        ideaService.createIdea(ideaRequest, image, authPayload.userId());
    }

}