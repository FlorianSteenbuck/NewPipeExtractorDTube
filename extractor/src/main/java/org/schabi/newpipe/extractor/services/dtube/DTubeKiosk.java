package org.schabi.newpipe.extractor.services.dtube;

public enum DTubeKiosk {
    NEW("New","newvideos","created"),
    HOT("Hot","hotvideos","hot"),
    TRENDING("Trending","trendingvideos","trending");

    private String name;
    private String id;
    private String by;

    DTubeKiosk(String name, String id, String by) {
        this.name = name;
        this.id = id;
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static DTubeKiosk getKioskById(String id) {
        for (DTubeKiosk value:DTubeKiosk.values()) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
    }
}
