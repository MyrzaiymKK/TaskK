package bank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccesssRequest {
    private String name;
    private String category;
    private double money;
    private String location;
    private String status;
}
