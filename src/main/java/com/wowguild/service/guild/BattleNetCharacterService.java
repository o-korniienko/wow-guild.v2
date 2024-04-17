package com.wowguild.service.guild;

import com.google.gson.Gson;
import com.wowguild.entity.Character;
import com.wowguild.model.UpdateStatus;
import com.wowguild.model.blizzard_character.CharacterImageData;
import com.wowguild.sender.HttpSender;
import com.wowguild.service.entity.impl.CharacterService;
import com.wowguild.service.token.TokenManager;
import com.wowguild.tool.LogHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BattleNetCharacterService {


    @Value("${battle.net.namespace}")
    private String namespace;
    @Value("${battle.net.locale}")
    private String locale;

    private final Gson gson;
    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final LogHandler logHandler;
    private final CharacterService characterService;


    public UpdateStatus<Character> getCharacterData(String url, int rank, String characterName, Character characterDB) {
        UpdateStatus<Character> status = new UpdateStatus<>();
        String token = tokenManager.getTokenByTag("blizzard");
        String response = null;
        if (token != null) {
            url = url + "&locale=eu_EU";
            response = httpSender.sendRequest(url, HttpMethod.GET, token);
            if (response == null || response.isEmpty()) {
                String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "  " + characterName + " - error request for character : " + url;
                logHandler.saveLog(log, "character_request_error");
                status.setStatus("there were errors during character data updating");
                status.setResult(characterDB);
            } else {
                try {
                    String name;
                    int lvl;
                    String classRuID;
                    String blizzardID;
                    String race;
                    String iconURL;
                    String realmEN;
                    race = response.split("race")[2].split(",\"id\":")[1].split("},\"")[0];

                    String[] characterArray = response.split("\"name\"");

                    name = characterArray[1].split(",")[0].substring(2).replace("\"", "");
                    blizzardID = characterArray[0].split("\"id\":")[1].replace(",", "");
                    classRuID = characterArray[5].split(",")[1].substring(5).replace("}", "");
                    lvl = Integer.parseInt(response.split("\"level\":")[1].split(",")[0]);
                    realmEN = response.split("\"realm\"")[1].split("\"slug\":\"")[1].split("\"")[0];

                    iconURL = getIconURL(characterName, realmEN);

                    if (characterDB != null) {
                        characterDB.setName(name);
                        characterDB.setClassEnByInt(Integer.parseInt(classRuID));
                        characterDB.setRankByInt(rank);
                        characterDB.setLevel(lvl);
                        characterDB.setRaceByID(Integer.parseInt(race));
                        characterDB.setIconURL(iconURL);
                        characterDB.setRegionEn(realmEN);
                        characterDB.setBlizzardID(blizzardID);

                        status.setStatus("Successful");
                        status.setResult(characterDB);
                    } else {
                        Character character = new Character();
                        character.setName(name);
                        character.setClassEnByInt(Integer.parseInt(classRuID));
                        character.setRankByInt(rank);
                        character.setLevel(lvl);
                        character.setRaceByID(Integer.parseInt(race));
                        character.setIconURL(iconURL);
                        character.setRegionEn(realmEN);
                        character.setBlizzardID(blizzardID);

                        status.setStatus("Successful");
                        status.setResult(character);
                    }
                    return status;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + characterName + " - error request for character : " + url + "\nresponse : " + response;
                    logHandler.saveLog(log, "character_parser_error");
                    status.setStatus("there were errors during character data updating");
                    status.setResult(characterDB);
                }
            }
        } else {
            status.setStatus("there were errors during character data updating");
            status.setResult(characterDB);
        }

        return status;
    }


    public UpdateStatus<Character> updateCharacter(Character character, String guildData) {
        UpdateStatus<Character> result = new UpdateStatus<>();
        if (guildData != null && !guildData.isEmpty()) {
            String[] charactersStrings = guildData.split("\"character\"");
            for (int i = 1; i < charactersStrings.length; i++) {
                String blizzardID = charactersStrings[i].split("\"id\":")[1].split(",")[0];
                if (blizzardID != null && blizzardID.equalsIgnoreCase(character.getBlizzardID())) {
                    String characterLink = charactersStrings[i];
                    characterLink = characterLink.substring(17);
                    String[] characterNameArray = characterLink.split("\"name\":\"");
                    String[] characterLinkArray = characterLink.split("\"}");
                    String[] characterRankArray = characterLink.split("\"rank\"");
                    String characterRank = characterRankArray[1];
                    characterRank = characterRank.substring(1, 2);
                    characterLink = characterLinkArray[0];
                    String characterName = characterNameArray[1].split("\"")[0];
                    result = getCharacterData(characterLink, Integer.parseInt(characterRank), characterName, character);

                    if (result.getStatus().equalsIgnoreCase("Successful")) {
                        character = result.getResult();

                        characterService.save(character);
                        result.setStatus("Successful");
                        result.setResult(character);
                    } else {
                        return result;
                    }


                    break;
                }
            }
        } else {
            result.setStatus("there were errors during character data updating");
            result.setResult(character);
        }

        return result;
    }


    public boolean isContains(Character character, List<Character> characters) {
        boolean check = false;
        for (Character character_db : characters) {
            if (character.getBlizzardID().equalsIgnoreCase(character_db.getBlizzardID())) {
                check = true;
                break;
            }
        }
        return check;
    }

    private String getIconURL(String characterName, String realm) {
        characterName = characterName.trim().toLowerCase();
        realm = realm.trim().toLowerCase();
        String result = "";
        String response = "";
        String token = tokenManager.getTokenByTag("blizzard");
        if (token != null) {
            try {
                String encodedName = encodeValue(characterName);
                String url = "https://eu.api.blizzard.com/profile/wow/character/" + realm + "/" + encodedName
                        + "/character-media?namespace=" + namespace + "&locale=" + locale;
                response = httpSender.sendRequest(url, HttpMethod.GET, token);
                if (response != null && !response.isEmpty()) {
                    CharacterImageData imageData = gson.fromJson(response, CharacterImageData.class);
                    if (imageData != null && !imageData.getAssets().isEmpty()) {
                        List<CharacterImageData.AssetItem> assets = imageData.getAssets();
                        boolean isContainMain = false;
                        for (CharacterImageData.AssetItem asset : assets) {
                            if (asset.getKey().equalsIgnoreCase("main")) {
                                result = asset.getValue();
                                isContainMain = true;
                            }
                        }
                        if (!isContainMain) {
                            for (CharacterImageData.AssetItem asset : assets) {
                                if (asset.getKey().equalsIgnoreCase("main-raw")) {
                                    result = asset.getValue();
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Character name: " + characterName + " - request for icon url:\n" +
                        "response: " + response;
                logHandler.saveLog(log, "character_icon_request_parse_error");
            }
        }
        return result;
    }


    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " encodeValue got error: " + e.getMessage());
            return value;
        }
    }
}
