package com.shadow649.hardwareshop.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
@MappedSuperclass
public class GenericId implements Serializable {
    public final String id;

    public static final String ID_PATTERN =
            "^(([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12})$";


}
