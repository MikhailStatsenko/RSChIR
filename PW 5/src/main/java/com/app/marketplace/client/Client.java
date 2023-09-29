package com.app.marketplace.client;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("clients")
public class Client {
    @Id
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
}

