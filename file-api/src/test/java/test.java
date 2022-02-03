import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class test {

    public static void main(String[] args) {

        LocalDateTime nowTime= LocalDateTime.now();

        LocalDateTime endTime = LocalDateTime.of(2021, 10, 22, 10, 10, 10);
        System.out.println(nowTime.isAfter(endTime));
        System.out.println(nowTime.isBefore(endTime));
    }

}
