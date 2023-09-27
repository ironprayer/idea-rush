package com.bid.idearush.global.schedule;

import com.bid.idearush.domain.bid.model.entity.Bid;
import com.bid.idearush.domain.bid.repository.BidRepository;
import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.global.util.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class Schedule {

    private final IdeaRepository ideaRepository;
    private final BidRepository bidRepository;
    private final NoticeService noticeService;

    @Scheduled(cron = "0 * * * * *")
    @SchedulerLock(name = "endBidOfIdea", lockAtMostFor = "50s", lockAtLeastFor = "50s")
    @Transactional
    public void endBidOfIdea() {
        log.info("endBidOfIdea Start");
        List<Idea> ideas =  ideaRepository.findEndIdeas();

        for(Idea idea : ideas) {
            Long userId = idea.getUsers().getId();
            Optional<Bid> topBid = bidRepository.findTopByIdeaOrderByBidPriceDesc(idea);
            if(topBid.isPresent()){
                idea.updateBidSuccess(topBid.get().getBidPrice());
            } else {
                idea.updateBidFail();
            }

//            noticeService.noticeBidEndEvent(userId, idea);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @SchedulerLock(name = "beforeTimeBidOfIdea", lockAtMostFor = "50s", lockAtLeastFor = "50s")
    public void beforeTimeBidOfIdea() {
        log.info("beforeTimeBidOfIdea Start");
        int[] hopeTimes = {10, 20, 30};
        LocalDateTime currentTime =  LocalDateTime.now();

        for(int hopeTime : hopeTimes){
//            noticeService.noticeBeforeEvent(currentTime, hopeTime);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @SchedulerLock(name = "startBidOfIdea", lockAtMostFor = "50s", lockAtLeastFor = "50s")
    @Transactional
    public void startBidOfIdea() {
        log.info("startBidOfIdea Start");
        LocalDateTime currentTime = LocalDateTime.now();
        ideaRepository.updatePrepareToOngoing(currentTime);
    }

}
