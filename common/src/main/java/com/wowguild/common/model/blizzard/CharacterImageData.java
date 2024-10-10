package com.wowguild.common.model.blizzard;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CharacterImageData {

    private List<AssetItem> assets;

    @Data
    @ToString
    public static class AssetItem {

        private String key;
        private String value;

    }

}