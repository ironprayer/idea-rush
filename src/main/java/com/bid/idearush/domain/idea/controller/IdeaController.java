package com.bid.idearush.domain.idea.controller;

import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.service.IdeaService;
import lombok.RequiredArgsConstructor;
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
                    @RequestPart(required = false) MultipartFile image){
        Long dummyUserId = 1L;

        ideaService.update(dummyUserId, ideaId, ideaRequest, image);

    }

}
