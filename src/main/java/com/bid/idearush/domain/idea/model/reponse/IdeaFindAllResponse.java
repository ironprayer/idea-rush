package com.bid.idearush.domain.idea.model.reponse;

import java.util.List;

public record IdeaFindAllResponse(
        List<IdeaListResponse> ideaListResponses,
        long totalDataSize,
        long totalPage
) {
}
