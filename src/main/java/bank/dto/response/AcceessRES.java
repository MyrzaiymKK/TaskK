package bank.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceessRES {
    private String name;
    private String category;
    private double money;
    private String location;
    private String status;
}
