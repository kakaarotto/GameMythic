package com.gm.query;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class BaseQuery {

    /**
     * Full text query keywords
     */
    public String q;

    public Boolean enabled = Boolean.TRUE;

}

