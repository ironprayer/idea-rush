package com.bid.idearush.domain.idea.controller;

import com.bid.idearush.domain.idea.controller.reponse.IdeasResponse;
import com.bid.idearush.domain.idea.controller.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.controller.request.IdeaRequest;
import com.bid.idearush.domain.idea.service.IdeaService;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.global.security.AuthPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/ideas")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @PutMapping("{id}")
    void updateIdea(@PathVariable(name = "id") Long ideaId,
                    @RequestPart(name = "idea") IdeaRequest ideaRequest,
                    @RequestPart(required = false) MultipartFile image,
                    @AuthenticationPrincipal AuthPayload authPayload) {
        ideaService.update(authPayload.userId(), ideaId, ideaRequest, image);

    }

    @DeleteMapping("{id}")
    void deleteIdea(@PathVariable(name = "id") Long ideaId,
                    @AuthenticationPrincipal AuthPayload authPayload) {
        ideaService.deleteIdea(authPayload.userId(), ideaId);
    }

    @GetMapping
    public Page<IdeasResponse> findAllIdea(
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