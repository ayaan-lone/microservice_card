package com.onlineBanking.card.request;

public class CardDto {
	private Long id;
    private String name;
    
    private Long daily_limit;
    private Long  monthly_limit;

    // Constructors
    public CardDto(Long id, String name,  Long daily_limit, Long monthly_limit) {
        this.id = id;
        this.name= name;
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

    public Long getDailyLimit() {
        return daily_limit;
    }

    public void setDailyLimit(Long daily_limit) {
        this.daily_limit = daily_limit;
    }

    public Long getMonthlyLimit() {
        return monthly_limit;
    }

    public void setMonthlyLimit(Long monthly_limit) {
        this.monthly_limit = monthly_limit;
    }
    
}
