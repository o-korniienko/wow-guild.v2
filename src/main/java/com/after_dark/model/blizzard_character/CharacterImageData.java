package com.after_dark.model.blizzard_character;

import java.util.List;

public class CharacterImageData {

        private List<AssetItem>  assets;

    public List<AssetItem> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetItem> assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return "CharacterImageData{" +
                "assets=" + assets +
                '}';
    }


    public class AssetItem {

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "AssetItem{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

}



