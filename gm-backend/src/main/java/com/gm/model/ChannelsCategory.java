package com.gm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * @author pujie
 */
@Entity
@Table(name = "channels_category")
@Data
@ToString
public class ChannelsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "add_time")
    private LocalDateTime addTime = LocalDateTime.now();

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    @Column
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "channels_channels_categary", joinColumns = {@JoinColumn(name = "channels_category_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "channels_id", referencedColumnName = "id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 30)
    private Set<Channels> channels = new HashSet<>();

}
