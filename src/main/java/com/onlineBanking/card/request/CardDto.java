package com.onlineBanking.card.request;

public class CardDto {
	private Long id;
    private String name;
    
    private Integer daily_limit;
    private Integer monthly_limit;

    // Constructors
    public CardDto(Long id, String name,  Integer daily_limit, Integer monthly_limit) {
        this.id = id;
        this.name = name;
        this.daily_limit = daily_limit;
        this.monthly_limit = monthly_limit;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDailyLimit() {
        return daily_limit;
    }

    public void setDailyLimit(Integer daily_limit) {
        this.daily_limit = daily_limit;
    }

    public Integer getMonthlyLimit() {
        return monthly_limit;
    }

    public void setMonthlyLimit(Integer monthly_limit) {
        this.monthly_limit = monthly_limit;
    }
    
}
