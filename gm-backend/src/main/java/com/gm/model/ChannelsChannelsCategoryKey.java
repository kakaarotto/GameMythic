package com.gm.model;

import java.io.Serializable;

/**
 * @author pujie
 * Composite primary key
 */
public class ChannelsChannelsCategoryKey implements Serializable {
    private Integer channelsId;
    private Integer gameCategoryId;

    // Override hashCode and equals method
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((channelsId == null) ? 0 : channelsId.hashCode());
        result = PRIME * result + ((gameCategoryId == null) ? 0 : gameCategoryId.hashCode());
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

        final ChannelsChannelsCategoryKey other = (ChannelsChannelsCategoryKey)obj;
        if(channelsId == null){
            if(other.channelsId != null){
                return false;
            }
        }else if(!channelsId.equals(other.channelsId)){
            return false;
        }
        if(gameCategoryId == null){
            if(other.gameCategoryId != null){
                return false;
            }
        }else if(!gameCategoryId.equals(other.gameCategoryId)){
            return false;
        }

        return true;
    }
}
