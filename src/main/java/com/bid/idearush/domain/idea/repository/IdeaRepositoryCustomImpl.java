package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.entity.QIdea;
import com.bid.idearush.domain.idea.model.reponse.IdeaFindAllResponse;
import com.bid.idearush.domain.idea.model.reponse.IdeaListResponse;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.QUsers;
import com.bid.idearush.global.type.ServerIpAddress;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

public class IdeaRepositoryCustomImpl extends QuerydslRepositorySupport implements IdeaRepositoryCustom {

    private QIdea qIdea = QIdea.idea;
    private QUsers qUsers = QUsers.users;
    private JPAQueryFactory queryFactory;

    public IdeaRepositoryCustomImpl(EntityManager em, JPAQueryFactory jpaQueryFactory) {
        super(Idea.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<IdeaListResponse> findIdeaOne(Long ideaId) {
        return Optional.ofNullable(queryFactory.select(Projections.constructor(IdeaListResponse.class,
                        qUsers.nickname.as("writer"),
                        qIdea.title,
                        qIdea.content,
                        qIdea.imageName.concat(ServerIpAddress.s3Address).as("imageUrl"),
                        qIdea.auctionStatus.as("status"),
                        qIdea.minimumStartingPrice,
                        qIdea.bidWinPrice
                ))
                .where(qIdea.id.eq(ideaId))
                .from(qIdea)
                .leftJoin(qIdea.users, qUsers)
                .fetchOne());
    }

    @Override
    public IdeaFindAllResponse findIdeaAll(Pageable pageable) {
        List<IdeaListResponse> results = queryFactory.select(Projections.constructor(IdeaListResponse.class,
                        qUsers.nickname.as("writer"),
                        qIdea.title,
                        qIdea.content,
                        qIdea.imageName.concat(ServerIpAddress.s3Address).as("imageUrl"),
                        qIdea.auctionStatus.as("status"),
                        qIdea.minimumStartingPrice,
                        qIdea.bidWinPrice
                ))
                .from(qIdea)
                .leftJoin(qIdea.users, qUsers)
                .orderBy(qIdea.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(qIdea.id)
                .from(qIdea);

        long dataSize = count.fetchCount();
        long totalPages = dataSize % pageable.getPageSize() == 0 ? dataSize / pageable.getPageSize() : dataSize / pageable.getPageSize() + 1;
        return new IdeaFindAllResponse(results, dataSize, totalPages);
    }

    @Override
    public IdeaFindAllResponse findCategoryAndTitleAll(Category category, String keyword, Pageable pageable) {
        List<IdeaListResponse> results = queryFactory.select(Projections.constructor(IdeaListResponse.class,
                        qUsers.nickname.as("writer"),
                        qIdea.title,
                        qIdea.content,
                        qIdea.imageName.concat(ServerIpAddress.s3Address).as("imageUrl"),
                        qIdea.auctionStatus.as("status"),
                        qIdea.minimumStartingPrice,
                        qIdea.bidWinPrice
                ))
                .where(
                        ideaTitleContains(keyword),
                        ideaCategoryEq(category)
                )
                .from(qIdea)
                .leftJoin(qIdea.users, qUsers)
                .orderBy(qIdea.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(qIdea.id)
                .from(qIdea);

        long dataSize = count.fetchCount();
        long totalPages = dataSize % pageable.getPageSize() == 0 ? dataSize / pageable.getPageSize() : dataSize / pageable.getPageSize() + 1;
        return new IdeaFindAllResponse(results, dataSize, totalPages);
    }

    private BooleanExpression ideaTitleContains(String keyword) {
        return isEmpty(keyword) ? null : qIdea.title.contains(keyword);
    }

    private BooleanExpression ideaCategoryEq(Category category) {
        return isEmpty(category) ? null : qIdea.category.eq(category);
    }

}
