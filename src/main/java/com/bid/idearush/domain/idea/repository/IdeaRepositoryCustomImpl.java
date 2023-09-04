package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.bid.model.entity.QBid;
import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.entity.QIdea;
import com.bid.idearush.domain.idea.model.reponse.IdeaListResponse;
import com.bid.idearush.domain.idea.model.reponse.IdeaOneResponse;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.QUsers;
import com.bid.idearush.global.type.ServerIpAddress;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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
    private QBid qBid = QBid.bid;
    private JPAQueryFactory queryFactory;

    public IdeaRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Idea.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<IdeaOneResponse> findIdeaOne(Long ideaId) {
        Expression<Long> maxBidPriceSubquery = JPAExpressions
                .select(qBid.bidPrice.max())
                .from(qBid)
                .where(qBid.idea.id.eq(ideaId));
        return Optional.ofNullable(queryFactory.select(Projections.constructor(IdeaOneResponse.class,
                        qUsers.id,
                        qUsers.nickname.as("writer"),
                        qIdea.title,
                        qIdea.content,
                        qIdea.imageName.prepend(ServerIpAddress.s3Address).as("imageUrl"),
                        qIdea.auctionStatus.as("status"),
                        qIdea.minimumStartingPrice,
                        qIdea.bidWinPrice,
                        maxBidPriceSubquery,
                        qIdea.category,
                        qIdea.auctionStartTime
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
                        qIdea.id,
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
