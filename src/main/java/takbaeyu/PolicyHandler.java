package takbaeyu;

import takbaeyu.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    PointRepository pointRepository;
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDelivered_GetPointPol(@Payload Delivered delivered){

        if(delivered.isMe()){
            //LJK

            int flag=0;
            Iterator<Point> iterator = pointRepository.findAll().iterator();
            while(iterator.hasNext()){

                Point pointTmp = iterator.next();
                if((pointTmp.getMemberId() == delivered.getMemberId()) && delivered.getStatus().equals("Finish")){
                    Optional<Point> PointOptional = pointRepository.findById(pointTmp.getId());
                    Point point = PointOptional.get();
                    point.setPoint(point.getPoint()+100);
                    pointRepository.save(point);
                    flag=1;
                }

            }

            if (flag==0 && delivered.getStatus().equals("Finish")){
                Point point = new Point();
                point.setMemberId(delivered.getMemberId());
                point.setPoint((long)100);
                pointRepository.save(point);
            }
            System.out.println("##### listener GetPointPol : " + delivered.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReviewed_GetPointPol(@Payload Reviewed reviewed){

        if(reviewed.isMe()){

            //LJK

            int flag=0;
            Iterator<Point> iterator = pointRepository.findAll().iterator();
            while(iterator.hasNext()){

                Point pointTmp = iterator.next();
                if(pointTmp.getMemberId() == reviewed.getMemberId()){
                    Optional<Point> PointOptional = pointRepository.findById(pointTmp.getId());
                    Point point = PointOptional.get();
                    point.setPoint(point.getPoint()+50);
                    pointRepository.save(point);
                    flag=1;
                }

            }

            if (flag==0){
                Point point = new Point();
                point.setMemberId(reviewed.getMemberId());
                point.setPoint((long)50);
                pointRepository.save(point);
            }

            System.out.println("##### listener GetPointPol : " + reviewed.toJson());
        }
    }

}
