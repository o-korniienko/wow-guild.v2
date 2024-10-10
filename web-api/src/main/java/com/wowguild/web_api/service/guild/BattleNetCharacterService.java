package com.wowguild.web_api.service.guild;

import com.wowguild.common.entity.Character;
import com.wowguild.common.dto.UpdateStatus;
import com.wowguild.common.model.blizzard.CharacterImageData;
import com.wowguild.common.model.blizzard.CharacterProfile;
import com.wowguild.common.model.blizzard.GuildProfile;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.common.service.entity.impl.CharacterService;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.LogHandler;
import com.wowguild.web_api.tool.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BattleNetCharacterService {


    @Value("${battle.net.namespace}")
    private String namespace;
    @Value("${battle.net.locale}")
    private String locale;
    @Value("${battle.net.character.profile.api}")
    private String characterProfileApi;

    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final LogHandler logHandler;
    private final CharacterService characterService;
    @Qualifier("characterProfileParser")
    private final Parser<CharacterProfile> characterProfileParser;
    @Qualifier("characterImageDataParser")
    private final Parser<CharacterImageData> characterImageDataParser;


    public UpdateStatus<Character> updateCharacter(Character character, GuildProfile guildProfile) {
        UpdateStatus<Character> result = new UpdateStatus<>();
        if (guildProfile != null) {
            List<GuildProfile.Member> members = guildProfile.getMembers();
            for (GuildProfile.Member member : members) {
                if (String.valueOf(member.getCharacter().getId()).equals(character.getBlizzardID())) {
                    String characterName = member.getCharacter().getName();
                    String characterLink = member.getCharacter().getKey().getHref();
                    String characterRank = String.valueOf(member.getRank());

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
                    CharacterProfile characterProfile = characterProfileParser.parseTo(response);

                    if (characterProfile != null) {
                        String name = characterProfile.getName();
                        int lvl = characterProfile.getLevel();
                        int classID = characterProfile.getCharacter_class().getId();
                        String blizzardID = String.valueOf(characterProfile.getId());
                        int race = characterProfile.getRace().getId();
                        String realmEN = characterProfile.getRealm().getSlug();
                        String iconURL = getIconURL(characterName, realmEN);

                        if (characterDB != null) {
                            characterDB.setName(name);
                            characterDB.setClassEnByInt(classID);
                            characterDB.setRankByInt(rank);
                            characterDB.setLevel(lvl);
                            characterDB.setRaceByID(race);
                            characterDB.setIconURL(iconURL);
                            characterDB.setRegionEn(realmEN);
                            characterDB.setBlizzardID(blizzardID);

                            status.setStatus("Successful");
                            status.setResult(characterDB);
                        } else {
                            Character character = new Character();
                            character.setName(name);
                            character.setClassEnByInt(classID);
                            character.setRankByInt(rank);
                            character.setLevel(lvl);
                            character.setRaceByID(race);
                            character.setIconURL(iconURL);
                            character.setRegionEn(realmEN);
                            character.setBlizzardID(blizzardID);

                            status.setStatus("Successful");
                            status.setResult(character);
                        }
                        return status;
                    }
                    String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + characterName + " - error request for character : " + url + "\nresponse : " + response;
                    logHandler.saveLog(log, "character_parser_error");
                    status.setStatus("there were errors during character data updating");
                    status.setResult(characterDB);

                } catch (NumberFormatException e) {
                    log.error(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    log.error(e.getMessage());
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

    public boolean isContained(Character character, List<Character> characters) {
        for (Character character_db : characters) {
            if (character.getBlizzardID().equalsIgnoreCase(character_db.getBlizzardID())) {
                return true;
            }
        }
        return false;
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
                String url = characterProfileApi + realm + "/" + encodedName
                        + "/character-media?namespace=" + namespace + "&locale=" + locale;
                response = httpSender.sendRequest(url, HttpMethod.GET, token);
                if (response != null && !response.isEmpty()) {
                    CharacterImageData imageData = characterImageDataParser.parseTo(response);
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
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
