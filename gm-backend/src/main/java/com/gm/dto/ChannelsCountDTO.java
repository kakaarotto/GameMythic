package com.gm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelsCountDTO extends ChannelsDTO {

    private List<ContentDTO> contents;

    //
    private Long subscriptionCount;
    public String getSubscriptionCountText() {
        if(this.getSubscriptionCount() != null) {
            if(this.getSubscriptionCount() > 1000) {
                DecimalFormat df = new DecimalFormat("0.0");
                return  df.format(this.getSubscriptionCount() / 1000d) + "k";
            }
            else if(this.getSubscriptionCount() > 0) {
                return this.getSubscriptionCount().toString();
            }
        }
        return "0";
    }

    private Long viewCount;
    public String getViewCountText() {
        if(this.getViewCount() != null) {
            if(this.getViewCount() > 1000) {
                DecimalFormat df = new DecimalFormat("0.0");
                return  df.format(this.getViewCount() / 1000d) + "k";
            }
            else if(this.getViewCount() > 0) {
                return this.getViewCount().toString();
            }
        }
        return "0";
    }
}
