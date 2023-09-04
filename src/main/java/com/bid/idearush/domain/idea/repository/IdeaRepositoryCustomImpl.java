package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.bid.model.entity.QBid;
import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.entity.QIdea;
import com.bid.idearush.domain.idea.model.reponse.IdeaListResponse;
import com.bid.idearush.domain.idea.model.reponse.IdeaOneResponse;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.QUsers;
import com.bid.idearush.global.type.ServerIpAddress;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

public class IdeaRepositoryCustomImpl extends QuerydslRepositorySupport implements IdeaRepositoryCustom {

    private QIdea qIdea = QIdea.idea;
    private QUsers qUsers = QUsers.users;
    private QBid qBid = QBid.bid;
    private JPAQueryFactory queryFactory;
    private EntityManager entityManager;

    public IdeaRepositoryCustomImpl(EntityManager entityManager) {
        super(Idea.class);
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
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
    public Page<IdeaListResponse> findCategory(Pageable pageable, Category category, long count) {
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
                .where(ideaCategoryEq(category))
                .from(qIdea)
                .innerJoin(qIdea.users, qUsers)
                .orderBy(qIdea.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public Page<IdeaListResponse> findTitle(Pageable pageable, String keyword) {

        String sql = "SELECT * FROM idea WHERE to_tsvector('english', title) @@ to_tsquery('english', ?1) ORDER BY created_at DESC OFFSET ?2 ROWS FETCH FIRST ?3 ROWS ONLY";
        Query nativeQuery = entityManager.createNativeQuery(sql, Idea.class);
        nativeQuery.setParameter(1, keyword+":*");
        nativeQuery.setParameter(2, pageable.getOffset());
        nativeQuery.setParameter(3, pageable.getPageSize());
        List<Idea> responses = nativeQuery.getResultList();

        return new PageImpl<>(responses.stream()
                .map(idea -> new IdeaListResponse(
                        idea.getId(),
                        idea.getUsers().getNickname(),
                        idea.getTitle(),
                        idea.getContent(),
                        idea.getImageName(),
                        idea.getAuctionStatus(),
                        idea.getMinimumStartingPrice(),
                        idea.getBidWinPrice()
                ))
                .collect(Collectors.toList()),
                pageable, 0);

    }

    private BooleanExpression ideaTitleContains(String keyword) {
        return isEmpty(keyword) ? null : qIdea.title.contains(keyword);
    }

    private BooleanExpression test(String keyword) {
        return Expressions.booleanTemplate("function('full_text_search', {0}, {1})",
                qIdea.title, keyword);
    }

    private BooleanExpression ideaCategoryEq(Category category) {
        return isEmpty(category) ? null : qIdea.category.eq(category);
    }

}
