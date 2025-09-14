package com.testimx.qaquiz.dto;

/**
 * Data transfer object for sending category information to
 * clients.  Contains only fields relevant for display; for
 * administration a different structure may be used.
 */
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Integer itemsCount;
    private String icon;

    public CategoryDto(Long id, String name, String description, Integer itemsCount, String icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.itemsCount = itemsCount;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public String getIcon() {
        return icon;
    }
}