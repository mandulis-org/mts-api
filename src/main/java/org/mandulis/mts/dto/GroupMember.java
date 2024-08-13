package org.mandulis.mts.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMember {
    private Long id;
    private String name;
    private String email;
}
