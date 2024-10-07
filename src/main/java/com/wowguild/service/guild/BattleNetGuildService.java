package com.wowguild.service.guild;

import com.wowguild.entity.Character;
import com.wowguild.entity.rank.Boss;
import com.wowguild.model.UpdateStatus;
import com.wowguild.model.blizzard.GuildProfile;
import com.wowguild.sender.HttpSender;
import com.wowguild.service.token.TokenManager;
import com.wowguild.tool.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleNetGuildService {

    @Value("${battle.net.realm}")
    private String realm;
    @Value("${battle.net.guild.name}")
    private String guildName;
    @Value("${battle.net.namespace}")
    private String namespace;
    @Value("${battle.net.locale}")
    private String locale;

    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final BattleNetCharacterService battleNetCharacterService;
    private final Parser<GuildProfile> guildProfileParser;

    private static final Comparator<Boss> BY_ID = (o1, o2) -> (int) (o1.getId() - o2.getId());

    public GuildProfile getGuildData() {
        String stringPosts = "";
        String token = tokenManager.getTokenByTag("blizzard");
        if (token != null) {
            String url = "https://eu.api.blizzard.com/data/wow/guild/" + realm + "/" + guildName +
                    "/roster?namespace=" + namespace + "&locale=" + locale;

            stringPosts = httpSender.sendRequest(url, HttpMethod.GET, token);
        }
        return guildProfileParser.parseTo(stringPosts);
    }

    public List<Character> parseGuildData(GuildProfile guildProfile) {
        List<Character> characterList = new ArrayList<>();
        Character character;
        try {
            if (guildProfile != null) {
                List<GuildProfile.Member> members = guildProfile.getMembers();
                for (GuildProfile.Member member : members) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    String characterName = member.getCharacter().getName();
                    String characterLink = member.getCharacter().getKey().getHref();
                    String characterRank = String.valueOf(member.getRank());

                    UpdateStatus<Character> status = battleNetCharacterService.getCharacterData(characterLink,
                            Integer.parseInt(characterRank), characterName, null);
                    character = status.getResult();

                    if (character != null) {
                        characterList.add(character);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return characterList;
    }

}