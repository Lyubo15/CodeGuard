package com.tu.codeguard.dto;

import java.util.List;

public record User(String id, String username, List<String> roles) {
}
