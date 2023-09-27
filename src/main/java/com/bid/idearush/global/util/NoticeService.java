package com.bid.idearush.global.util;

import com.bid.idearush.domain.bid.repository.BidRepository;
import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.DealStatus;
import com.bid.idearush.global.kafka.KafkaProducerService;
import com.bid.idearush.domain.idea.model.entity.BidReservation;
import com.bid.idearush.domain.idea.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final KafkaProducerService kafkaProducerService;
    private final IdeaRepository ideaRepository;
    private final BidRepository bidRepository;
    private final ReservationRepository reservationRepository;

    public void noticeBidEvent(Long bidUserId, Idea idea, Long bidPrice) {
        List<Long> userIds = bidRepository.findAllByIdeaDistinctUser(idea.getId());
        userIds.remove(bidUserId);

        for(Long userId : userIds) {
            String message = "아이디어 + '" + idea.getTitle() + "'가 " + bidPrice + "원에 입찰되었습니다.";
            kafkaProducerService.sendMessage("notice", "NOTICE_BID_PRICE_UPDATE#"
                    + message + "#"
                    + userId);
        }
    }


    public void noticeBeforeEvent(LocalDateTime currentTime, Integer hopeTime){
        List<Idea> ideas =  ideaRepository.findBeforeTime(currentTime.plusMinutes(hopeTime - 1), currentTime.plusMinutes(hopeTime + 1));

        for(Idea idea : ideas) {
            List<BidReservation> bidReservations = reservationRepository.findAllByIdea(idea);
            System.out.println(idea.getId() + " : " + bidReservations.size());

            for(BidReservation bidReservation : bidReservations) {
                String message = "아이디어 + '" + idea.getTitle() + "'가 시작 " + hopeTime  + "분 전입니다.";
                kafkaProducerService.sendMessage("notice", "NOTICE_BID_START_BEFORE#"
                        + message + "#"
                        + bidReservation.getUsers().getId());
            }
        }
    }

    public void noticeBidEndEvent(Long userId, Idea idea){
        String message = idea.getDealStatus() == DealStatus.BID_WIN
                ? "아이디어 + '" + idea.getTitle() + "'가 " + idea.getBidWinPrice() + "원에 낙찰되었습니다."
                : "아이디어 + '" + idea.getTitle() + "'가 " + "유찰되었습니다.";

        kafkaProducerService.sendMessage("notice", "NOTICE_BID_END#"
                + message + "#"
                + userId);

    }

}