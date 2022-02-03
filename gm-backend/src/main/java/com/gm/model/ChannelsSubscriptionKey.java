package com.gm.model;


import java.io.Serializable;


/**
 * @author pujie
 * Composite primary key
 */
public class ChannelsSubscriptionKey  implements Serializable {
    private Integer userId;
    private Integer channelsId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChannelsId() {
        return channelsId;
    }

    public void setChannelsId(Integer channelsId) {
        this.channelsId = channelsId;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((userId == null) ? 0 : userId.hashCode());
        result = PRIME * result + ((channelsId == null) ? 0 : channelsId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        final ChannelsSubscriptionKey other = (ChannelsSubscriptionKey)obj;
        if(userId == null){
            if(other.userId != null){
                return false;
            }
        }else if(!userId.equals(other.userId)){
            return false;
        }
        if(channelsId == null){
            if(other.channelsId != null){
                return false;
            }
        }else if(!channelsId.equals(other.channelsId)){
            return false;
        }

        return true;
    }
}
