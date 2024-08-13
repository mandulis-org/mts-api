package org.mandulis.mts.dto;

import lombok.*;
import org.mandulis.mts.entity.Ticket.Priority;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkedTicketResponse {
    private Long id;
    private String title;
    private String description;
    private String userName;
    private String categoryName;
    private Priority priority;
    private List<String> comments;
    private List<String> attachments;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
