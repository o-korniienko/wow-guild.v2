package com.wowguild.service.guild;

import com.wowguild.model.UpdateStatus;
import com.wowguild.entity.Character;
import com.wowguild.entity.rank.Boss;
import com.wowguild.sender.HttpSender;
import com.wowguild.service.token.TokenManager;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BattleNetGuildService {


    private final Gson gson;

    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final BattleNetCharacterService battleNetCharacterService;

    private static final Comparator<Boss> BY_ID = (o1, o2) -> (int) (o1.getId() - o2.getId());

    public String getGuildData() {
        String stringPosts = "";
        String token = tokenManager.getTokenByTag("blizzard");
        if (token != null) {
            String url = "https://eu.api.blizzard.com/data/wow/guild//tarren-mill/tauren-milfs/roster?namespace=profile-eu&locale=eu_EU";
            try {
                stringPosts = httpSender.sendRequest(url, HttpMethod.GET, token);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }
        return stringPosts;
    }

    public List<Character> parseGuildData(String response) {
        List<Character> characterList = new ArrayList<>();
        Character character;
        String characterName;
        if (response != null && !response.isEmpty()) {
            try {
                String[] charactersStrings = response.split("\"character\"");
                for (int r = 1; r < charactersStrings.length; r++) {
                    character = null;
                    characterName = "";
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String characterLink = charactersStrings[r];
                    characterLink = characterLink.substring(17);
                    String[] characterNameArray = characterLink.split("\"name\":\"");
                    String[] characterLinkArray = characterLink.split("\"}");
                    String[] characterRankArray = characterLink.split("\"rank\"");
                    String characterRank = characterRankArray[1];
                    characterRank = characterRank.substring(1, 2);
                    characterLink = characterLinkArray[0];
                    characterName = characterNameArray[1].split("\"")[0];


                    UpdateStatus<Character> status = battleNetCharacterService.getCharacterData(characterLink, Integer.parseInt(characterRank), characterName, null);
                    character = status.getResult();

                    if (character != null) {
                        characterList.add(character);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return characterList;
    }

}
