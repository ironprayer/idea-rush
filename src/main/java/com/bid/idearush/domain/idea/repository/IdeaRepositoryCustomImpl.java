package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.entity.QIdea;
import com.bid.idearush.domain.idea.model.reponse.IdeaListResponse;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.QUsers;
import com.bid.idearush.global.type.ServerIpAddress;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

public class IdeaRepositoryCustomImpl extends QuerydslRepositorySupport implements IdeaRepositoryCustom {

    private QIdea qIdea = QIdea.idea;
    private QUsers qUsers = QUsers.users;
    private JPAQueryFactory queryFactory;

    public IdeaRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
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
                .innerJoin(qIdea.users, qUsers)
                .fetchOne());
    }

    @Override
    public Page<IdeaListResponse> findIdeaAll(Pageable pageable, long count) {
        List<IdeaListResponse> results = queryFactory.select(Projections.constructor(IdeaListResponse.class,
                        qIdea.id,
                        qUsers.nickname.as("writer"),
                        qIdea.title,
                        qIdea.content,
                        qIdea.imageName.concat(ServerIpAddress.s3Address).as("imageUrl"),
                        qIdea.auctionStatus.as("status"),
                        qIdea.minimumStartingPrice,
                        qIdea.bidWinPrice
                ))
                .from(qIdea)
                .innerJoin(qIdea.users, qUsers)
                .orderBy(qIdea.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public Page<IdeaListResponse> findCategoryAndTitleAll(Category category, String keyword, Pageable pageable, long count) {
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
                .innerJoin(qIdea.users, qUsers)
                .orderBy(qIdea.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, count);
    }

    private BooleanExpression ideaTitleContains(String keyword) {
        return isEmpty(keyword) ? null : qIdea.title.contains(keyword);
    }

    private BooleanExpression ideaCategoryEq(Category category) {
        return isEmpty(category) ? null : qIdea.category.eq(category);
    }

}
