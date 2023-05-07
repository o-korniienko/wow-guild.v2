package com.after_dark.service;

import com.after_dark.model.*;
import com.after_dark.model.blizzard_character.CharacterImageData;
import com.after_dark.model.character.Character;
import com.after_dark.model.rank.Boss;
import com.after_dark.model.rank.CharacterRank;
import com.after_dark.model.rank.Rank;
import com.after_dark.model.rank.Zone;
import com.after_dark.model.wow_logs_models.Report;
import com.after_dark.model.wow_logs_models.WOWLogsCharacterRankData;
import com.after_dark.model.wow_logs_models.WOWLogsFightData;
import com.after_dark.model.wow_logs_models.WOWLogsReportData;
import com.after_dark.repos.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepos characterRepos;
    @Autowired
    private TokenRepo tokenRepo;
    @Autowired
    private BossRepo bossRepo;
    @Autowired
    private RankRepo rankRepo;
    @Autowired
    private CharacterRankRepo characterRankRepo;
    @Autowired
    private WOWLogsReportRepo wowLogsReportRepo;
    @Autowired
    private ZoneRepo zoneRepo;

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Value("${spring.battle_net_id}")
    private String BNET_ID;
    @Value("${spring.battle_net_secret}")
    private String BNET_SECRET;
    @Value("${spring.wow_logs_id}")
    private String WOW_LOGS_ID;
    @Value("${spring.wow_logs_secret}")
    private String WOW_LOGS_SECRET;
    @Value("${spring.wow_logs_key}")
    private String WOW_LOGS_KEY;

    private Comparator<Character> BY_GUILD_RANK = new Comparator<Character>() {
        @Override
        public int compare(Character o1, Character o2) {
            return o1.getRank().compareTo(o2.getRank());
        }
    };
    private Comparator<Character> BY_LEVEL = new Comparator<Character>() {
        @Override
        public int compare(Character o1, Character o2) {
            return o2.getLevel() - o1.getLevel();
        }
    };

    private static final Comparator<Boss> BY_ID = new Comparator<Boss>() {
        @Override
        public int compare(Boss o1, Boss o2) {
            return (int) (o1.getId() - o2.getId());
        }
    };

    private Comparator<Boss> BY_ENCOUNTER_ID = new Comparator<Boss>() {
        @Override
        public int compare(Boss o1, Boss o2) {
            return (int) (o1.getEncounterID() - o2.getEncounterID());
        }
    };

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout
        clientHttpRequestFactory.setConnectTimeout(30 * 1000);

        //Read timeout
        clientHttpRequestFactory.setReadTimeout(30 * 1000);
        return clientHttpRequestFactory;
    }


    public List<Character> getMembers() {
        List<Character> characters = characterRepos.findAll();
        Collections.sort(characters, BY_LEVEL.thenComparing(BY_GUILD_RANK));
        return characters;
    }

    public String getGuildDataFromBlizzardDB() {

        List<Character> characterList = new ArrayList<>();
        String stringPosts = "";
        Character character;
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        String token = getToken("blizzard");
        if (token != null) {
            String url = "https://eu.api.blizzard.com/data/wow/guild//tarren-mill/tauren-milfs/roster?namespace=profile-eu&locale=eu_EU&access_token=" + token;
            try {
                stringPosts = restTemplate.getForObject(url, String.class);

            } catch (RestClientException e) {
                e.printStackTrace();
            }
        }

        return stringPosts;
    }

    private List<Character> parseBlizzardGuildResponse(String response) {
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


                    character = getCharacterDataFromBlizzardAPI(characterLink, Integer.parseInt(characterRank), characterName, null);


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

    private Character getCharacterDataFromBlizzardAPI(String url, int rank, String characterName, Character characterFromDB) {

        Character character = null;
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        String token = getToken("blizzard");
        String response = null;

        if (token != null) {
            try {
                url = url + "&locale=eu_EU&access_token=" + token;

                response = restTemplate.getForObject(new URI(url.trim()), String.class);

            } catch (RestClientException e) {
                e.printStackTrace();
                System.out.println(characterName);
                System.out.println(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                System.out.println(characterName);
                System.out.println(url);
            }
            if (response == null || response.isEmpty()) {
                String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "  " + characterName + " - error request for character : " + url;
                saveLog(log, "character_request_error");
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


                    if (characterFromDB != null) {
                        characterFromDB.setName(name);
                        characterFromDB.setRankByInt(rank);
                        characterFromDB.setLevel(lvl);
                        characterFromDB.setRaceByID(Integer.parseInt(race));
                        characterFromDB.setIconURL(iconURL);
                        characterFromDB.setRegionEn(realmEN);

                        characterFromDB.setBlizzardID(blizzardID);
                        return characterFromDB;
                    } else {
                        character = new Character();
                        character.setName(name);
                        character.setClassEnByInt(Integer.parseInt(classRuID));
                        character.setRankByInt(rank);
                        character.setLevel(lvl);
                        character.setRaceByID(Integer.parseInt(race));
                        character.setIconURL(iconURL);
                        character.setRegionEn(realmEN);
                        character.setBlizzardID(blizzardID);
                        return character;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + characterName + " - error request for character : " + url + "\nresponse : " + response;
                    saveLog(log, "character_parser_error");

                }


            }

        }
        return character;
    }

    private String getIconURL(String characterName, String realm) {
        characterName = characterName.trim().toLowerCase();
        realm = realm.trim().toLowerCase();
        String result = "";
        String[] result2;
        String token = getToken("blizzard");
        String urlStr = "";
        ResponseEntity<String> response = null;
        RestTemplate template = new RestTemplate(getClientHttpRequestFactory());
        if (token != null) {


            HttpURLConnection con;
            try {
                urlStr = "https://eu.api.blizzard.com/profile/wow/character/" + realm + "/" + characterName + "/character-media?namespace=profile-eu&locale=eu-EU&access_token=" + token;

                response = template.getForEntity(urlStr, String.class);

                if (response != null) {
                    if (response.getStatusCodeValue() == 200) {
                        result2 = response.getBody().split("assets");
                        CharacterImageData imageData = gson.fromJson(response.getBody(), CharacterImageData.class);

                        if (imageData != null && !imageData.getAssets().isEmpty()){
                            List<CharacterImageData.AssetItem> assets = imageData.getAssets();
                            boolean isContainMain = false;
                            for (CharacterImageData.AssetItem asset : assets) {
                                if (asset.getKey().equalsIgnoreCase("main")){
                                    result = asset.getValue();
                                    isContainMain = true;
                                }
                            }
                            if (!isContainMain){
                                for (CharacterImageData.AssetItem asset : assets) {
                                    if (asset.getKey().equalsIgnoreCase("main-raw")){
                                        result = asset.getValue();
                                    }
                                }
                            }
                        }

                        /*if (result2.length > 1) {
                            result = result2[1].split("\"main\"")[1].split("value\":\"")[1].split("\"")[0];
                        } else {
                            result = result2[0].split("render_url\":\"")[1].split("\"}")[0];

                        }*/
                    }
                } else {

                }


            } catch (Exception e) {
                String log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + characterName + " - request for icon url: " + urlStr + "\n" +
                        "response for icon url: " + response;
                saveLog(log, "character_icon_request_parse_error");

            }

        }
        return result;
    }

    private String getToken(String tag) {
        Token tokenFromDB = tokenRepo.findByTag(tag);

        String token = null;
        if (tokenFromDB != null) {

            if (!isTokenExpired(tokenFromDB)) {
                token = tokenFromDB.getAccess_token();
                return token;
            } else {
                if (tag.equals("blizzard")) {
                    Token newToken = getTokenFromBlizzard();
                    token = newToken.getAccess_token();
                    tokenFromDB.setAccess_token(newToken.getAccess_token());
                    tokenFromDB.setCreateTime(newToken.getCreateTime());
                    tokenFromDB.setExpires_in(newToken.getExpires_in());
                    tokenRepo.save(tokenFromDB);
                    return token;
                }
                if (tag.equals("wow_logs")) {
                    Token newToken = getTokenFromWOWLogs();
                    token = newToken.getAccess_token();
                    tokenFromDB.setAccess_token(newToken.getAccess_token());
                    tokenFromDB.setCreateTime(newToken.getCreateTime());
                    tokenFromDB.setExpires_in(newToken.getExpires_in());
                    tokenRepo.save(tokenFromDB);
                    return token;
                }
            }
        } else {
            if (tag.equals("blizzard")) {
                Token newToken = getTokenFromBlizzard();
                token = newToken.getAccess_token();
                tokenRepo.save(newToken);
            }
            if (tag.equals("wow_logs")) {
                Token newToken = getTokenFromWOWLogs();
                token = newToken.getAccess_token();
                tokenRepo.save(newToken);
            }
        }
        return token;
    }

    private Token getTokenFromBlizzard() {
        HttpURLConnection con;
        Token tokenToSave = new Token();
        try {
            String encodedCredentials = Base64.getEncoder().encodeToString(String.format("%s:%s", BNET_ID,
                    BNET_SECRET).getBytes("UTF-8"));

            URL url1 = new URL("https://eu.battle.net/oauth/token");
            con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", String.format("Basic %s", encodedCredentials));
            con.setDoOutput(true);
            con.getOutputStream().write("grant_type=client_credentials".getBytes("UTF-8"));
            int responseCode = con.getResponseCode();
            System.out.println("response code: " + responseCode + " response message: " + con.getResponseMessage());
            if (responseCode == 200) {
                String response = IOUtils.toString(con.getInputStream(), "UTF-8");
                TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);

                tokenToSave.setAccess_token(tokenResponse.getAccess_token());
                tokenToSave.setExpires_in(tokenResponse.getExpires_in());
                tokenToSave.setCreateTime(LocalDateTime.now());
                tokenToSave.setTag("blizzard");
                return tokenToSave;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isTokenExpired(Token token) {
        long expiresIn = token.getExpires_in();
        LocalDateTime createTokenDate = token.getCreateTime();
        LocalDateTime dateTokenWorkUntil = createTokenDate.plusSeconds(expiresIn);
        LocalDateTime nowDate = LocalDateTime.now();
        if (nowDate.isAfter(dateTokenWorkUntil)) {
            return true;
        }

        return false;
    }

    public List<Character> updateMembersFromBlizzardDB() {
        List<Character> charactersFromBlizzardDB = parseBlizzardGuildResponse(getGuildDataFromBlizzardDB());

        //characterRepos.deleteAll();
        List<Character> charactersFromOurDB = characterRepos.findAll();

        //charactersFromOurDB = addAllCharactersToDB(charactersFromBlizzardDB);

        if (charactersFromOurDB == null || charactersFromOurDB.size() == 0) {
            charactersFromOurDB = addAllCharactersToDB(charactersFromBlizzardDB);
        } else {
            for (Character character : charactersFromOurDB) {
                if (!isCharacterInThisDB(character, charactersFromBlizzardDB)) {
                    characterRepos.delete(character);
                }
            }

            for (Character character : charactersFromBlizzardDB) {
                if (!isCharacterInThisDB(character, charactersFromOurDB)) {
                    characterRepos.save(character);
                }
            }
            charactersFromOurDB = characterRepos.findAll();
            for (Character our_character : charactersFromOurDB) {
                for (Character blizzard_character : charactersFromBlizzardDB) {
                    if (our_character.getBlizzardID().equalsIgnoreCase(blizzard_character.getBlizzardID())) {
                        our_character.setIconURL(blizzard_character.getIconURL());

                        our_character.setClassEn(blizzard_character.getClassEn());
                        our_character.setLevel(blizzard_character.getLevel());
                        our_character.setIconURL(blizzard_character.getIconURL());
                        our_character.setName(blizzard_character.getName());
                        our_character.setRace(blizzard_character.getRace());
                        our_character.setRank(blizzard_character.getRank());


                    }
                }
            }
        }
        Collections.sort(charactersFromOurDB, BY_LEVEL.thenComparing(BY_GUILD_RANK));
        return charactersFromOurDB;
    }

    private boolean isCharacterInThisDB(Character character, List<Character> characters) {
        boolean check = false;
        for (Character character_db : characters) {
            if (character.getBlizzardID().equalsIgnoreCase(character_db.getBlizzardID())) {
                check = true;
                break;
            }
        }


        return check;
    }

    public List<Character> addAllCharactersToDB(List<Character> characters) {
        /*for (Character character : characters) {
            characterRepos.save(character);
        }*/
        characterRepos.saveAll(characters);

        return characters;
    }

    private Token getTokenFromWOWLogs() {
        Token token = new Token();
        try {
            String url = "https://ru.warcraftlogs.com/oauth/token";
            MultiValueMap<String, String> bodyParamMap = new LinkedMultiValueMap<String, String>();
            bodyParamMap.add("grant_type", "client_credentials");
            bodyParamMap.add("client_id", WOW_LOGS_ID);
            bodyParamMap.add("client_secret", WOW_LOGS_SECRET);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorisation", "Basic");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyParamMap, headers);
            RestTemplate template = new RestTemplate(getClientHttpRequestFactory());
            ResponseEntity<TokenResponse> response = template.postForEntity(url, request, TokenResponse.class);
            TokenResponse tokenResponse = response.getBody();
            token.setTag("wow_logs");
            token.setAccess_token(tokenResponse.getAccess_token());
            token.setExpires_in(tokenResponse.getExpires_in());
            token.setCreateTime(LocalDateTime.now());

        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return token;
    }

    public Map<String, List<Boss>> UpdateRankingData() {
        Map<String, List<Boss>> result = new HashMap<>();
        boolean isThereNoErrors = true;
        String token = getToken("wow_logs");
        //System.out.println(token);
        Map<String, String> body = new HashMap<>();
        body.put("query", "{reportData {reports(guildName:\"Tauren Milfs\", guildServerSlug:\"tarren-mill\", guildServerRegion:\"EU\") {data{code,endTime}}}}");
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");
        headers.set("Authorisation", "Bearer");
        headers.setBearerAuth(token);
        String url = "https://www.warcraftlogs.com/api/v2/client";
        HttpEntity request = new HttpEntity<>(body, headers);
        RestTemplate template = new RestTemplate(getClientHttpRequestFactory());
        ResponseEntity<String> response = null;
        try {
            response = template.exchange(url, HttpMethod.POST, request, String.class);

            String reportsData = response.getBody().split("\"reports\":")[1];
            reportsData = reportsData.substring(0, reportsData.length() - 3);


            WOWLogsReportData reportData = gson.fromJson(reportsData, WOWLogsReportData.class);
            isThereNoErrors = getReportsDataAndSaveItInDB(reportData);
        } catch (RestClientException e) {
            e.printStackTrace();
            isThereNoErrors = false;
        }

        if (isThereNoErrors) {
            result.put("Successful", getBosses());
        } else {
            result.put("There were errors during updating rank data", getBosses());
        }
        return result;
    }

    private boolean getReportsDataAndSaveItInDB(WOWLogsReportData reportData) {
        List<Report> reports = reportData.getData();
        List<WOWLogsFightData> fightData = new ArrayList<>();
        WOWLogsFightData ftData = null;
        for (Report report : reports) {
            boolean isReportAlreadyInDb = isReportAlreadyInDB(report);
            if (!isReportAlreadyInDb) {
                wowLogsReportRepo.save(report);
                ftData = getReportData(report.getCode());
                if (ftData != null) {
                    fightData.add(ftData);
                }
            }
        }


        return parsesCharactersAndBosses(fightData);

    }

    private boolean isReportAlreadyInDB(Report report) {
        boolean result = false;
        List<Report> reports = wowLogsReportRepo.findAll();

        for (Report reportFromDB : reports) {
            if (reportFromDB.getCode().equalsIgnoreCase(report.getCode())) {
                result = true;
            }
        }
        return result;
    }


    private WOWLogsFightData getReportData(String code) {
        WOWLogsFightData data = null;
        String token = getToken("wow_logs");
        Map<String, String> body = new HashMap<>();
        String requestString = "{reportData {report(code:\"" + code + "\" ){fights{kill,name, difficulty, encounterID}zone{name,expansion{name},brackets{type,bucket,min,max}}rankedCharacters{name, canonicalID, server{slug}}}}}";
        body.put("query", requestString);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");
        headers.set("Authorisation", "Bearer");
        headers.setBearerAuth(token);
        String url = "https://www.warcraftlogs.com/api/v2/client";
        HttpEntity request = new HttpEntity<>(body, headers);
        RestTemplate template = new RestTemplate(getClientHttpRequestFactory());
        ResponseEntity<String> response = null;
        try {
            response = template.exchange(url, HttpMethod.POST, request, String.class);


            String reportsData = response.getBody().split("\"report\":")[1];
            reportsData = reportsData.substring(0, reportsData.length() - 3);


            data = gson.fromJson(reportsData, WOWLogsFightData.class);
            return data;
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;

        }

    }

    private boolean parsesCharactersAndBosses(List<WOWLogsFightData> reportsData) {
        boolean isThereNoErrors = true;
        Set<Character> characters = new HashSet<>();
        Set<Boss> bosses = new HashSet<>();
        for (WOWLogsFightData reportData : reportsData) {
            if (reportData.getRankedCharacters() == null || reportData.getFights() == null || reportData.getZone() == null || reportData.getZone().getName().equalsIgnoreCase("Mythic+ Dungeons")) {
                continue;
            }
            List<WOWLogsFightData.RankedCharacter> rankedCharacters = reportData.getRankedCharacters();

            for (WOWLogsFightData.RankedCharacter rankedCharacter : rankedCharacters) {
                Character character = null;
                try {
                    List<Character> charactersFromDB = characterRepos.findByName(rankedCharacter.getName());
                    if (charactersFromDB.size() > 1) {
                        for (Character character1 : charactersFromDB) {
                            if (character1.getRegionEn().equalsIgnoreCase(rankedCharacter.getServer().getSlug())) {
                                character = character1;
                            }
                        }
                    } else {
                        if (charactersFromDB.size() == 1) {
                            character = charactersFromDB.get(0);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(rankedCharacter.getName());
                    isThereNoErrors = false;
                }
                if (character != null) {
                    character.setCanonicalID(rankedCharacter.getCanonicalID());
                    characterRepos.save(character);
                } else {
                    character = characterRepos.findByCanonicalID(rankedCharacter.getCanonicalID());

                }

                try {
                    if (character != null) {

                        characters.add(character);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(character);
                    isThereNoErrors = false;
                }
            }


            List<WOWLogsFightData.Fight> fights = reportData.getFights();

            Zone zoneFromDB = zoneRepo.findByZoneName(reportData.getZone().getName().trim());
            if (zoneFromDB == null){
                zoneFromDB = new Zone();
                zoneFromDB.setZoneName(reportData.getZone().getName().trim());
                zoneFromDB.setExpansionName(reportData.getZone().getExpansion().getName().trim());
                zoneFromDB.setMaxLevel(reportData.getZone().getBrackets().getMax());

                zoneRepo.save(zoneFromDB);
            }

            for (WOWLogsFightData.Fight fight : fights) {

                if (fight.isKill() && fight.getEncounterID() != 0 && reportData.getZone() != null) {
                    Boss boss = new Boss();
                    boss.setDifficulty(fight.getDifficulty());
                    boss.setName(fight.getName());
                    boss.setEncounterID(fight.getEncounterID());
                    boss.setZone(zoneFromDB);
                    bosses.add(boss);
                }
            }


        }

        Set<Boss> finalBossList = bosses;


        for (Boss boss : bosses) {
            finalBossList = checkIfAllDifficultyOfBossesAdded(boss, finalBossList);
        }


        if (characters != null && finalBossList != null) {
            if (isThereNoErrors) {
                isThereNoErrors = getAndParseCharactersRanksFromWOWLogsAndSaveIt(characters, finalBossList);
            } else {
                getAndParseCharactersRanksFromWOWLogsAndSaveIt(characters, finalBossList);
            }
        }
        return isThereNoErrors;
    }

    private Set<Boss> checkIfAllDifficultyOfBossesAdded(Boss boss, Set<Boss> bosses) {
        Set<Boss> result = new HashSet<>();
        boolean is3DifficultyAdded = false;
        boolean is4DifficultyAdded = false;
        boolean is5DifficultyAdded = false;
        for (Boss boss1 : bosses) {
            if (boss1.getEncounterID() == boss.getEncounterID()) {

                if (boss1.getDifficulty() == 3) {
                    is3DifficultyAdded = true;
                }
                if (boss1.getDifficulty() == 4) {
                    is4DifficultyAdded = true;
                }
                if (boss1.getDifficulty() == 5) {
                    is5DifficultyAdded = true;
                }
            }
            result.add(boss1);
        }

        if (!is3DifficultyAdded && (boss.getDifficulty() == 4 || boss.getDifficulty() == 5)) {
            Boss boss3Difficulty = new Boss();
            boss3Difficulty.setName(boss.getName().trim());
            boss3Difficulty.setDifficulty(3);
            boss3Difficulty.setZone(boss.getZone());
            boss3Difficulty.setEncounterID(boss.getEncounterID());
            result.add(boss3Difficulty);
        }
        if (!is4DifficultyAdded && (boss.getDifficulty() == 3 || boss.getDifficulty() == 5)) {
            Boss boss4Difficulty = new Boss();
            boss4Difficulty.setName(boss.getName().trim());
            boss4Difficulty.setDifficulty(4);
            boss4Difficulty.setZone(boss.getZone());
            boss4Difficulty.setEncounterID(boss.getEncounterID());
            result.add(boss4Difficulty);
        }
        if (!is5DifficultyAdded && (boss.getDifficulty() == 4 || boss.getDifficulty() == 3)) {
            Boss boss5Difficulty = new Boss();
            boss5Difficulty.setName(boss.getName().trim());
            boss5Difficulty.setDifficulty(5);
            boss5Difficulty.setZone(boss.getZone());
            boss5Difficulty.setEncounterID(boss.getEncounterID());
            result.add(boss5Difficulty);
        }

        return result;
    }

    private boolean getAndParseCharactersRanksFromWOWLogsAndSaveIt(Set<Character> characters, Set<Boss> bosses) {
        boolean is429Error = false;
        boolean isThereNoErrors = true;
        for (int i = 0; i < 2; i++) {
            if (is429Error) {
                break;
            }
            String metric;
            if (i == 0) {
                metric = "dps";
            } else {
                metric = "hps";
            }
            for (Character character : characters) {
                if (is429Error) {
                    break;
                }
                if (character != null) {
                    List<CharacterRank> ranks = character.getRanks();
                    if (ranks == null) {
                        ranks = new ArrayList<>();
                    }

                    for (Boss boss : bosses) {

                        String response = "";


                        response = getCharacterDataFromWOWLogsApi(boss, character, metric);
                        if (response == null) {

                            isThereNoErrors = false;
                        }
                        if (response.contains("429 Too Many Requests")) {
                            is429Error = true;
                            isThereNoErrors = false;
                            break;
                        }


                        ranks = parseCharacterWOWLogsData(response, character, boss, ranks);


                    }
                    character.setRanks(ranks);
                    characterRepos.save(character);
                }
            }
        }
        return isThereNoErrors;
    }

    private String getCharacterDataFromWOWLogsApi(Boss boss, Character character, String metric) {
        String token = getToken("wow_logs");
        String result = "";

        String url = null;
        HttpEntity request = null;
        RestTemplate template = null;
        ResponseEntity<String> response = null;
        if (token != null && !token.isEmpty()) {
            long encounterId = boss.getEncounterID();
            int difficulty = boss.getDifficulty();
            String characterName = character.getName();
            String server = character.getRegionEn();

            Map<String, String> body = new HashMap<>();
            String requestString = "{characterData{character(name:\"" + characterName + "\", serverSlug:\"" + server + "\", serverRegion:\"EU\"){encounterRankings(encounterID:" + encounterId + ", difficulty:" + difficulty + ", metric:" + metric + ")}}}";
            body.put("query", requestString);
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", "application/json");
            headers.set("Authorisation", "Bearer");
            headers.setBearerAuth(token);
            url = "https://www.warcraftlogs.com/api/v2/client";
            try {
                request = new HttpEntity<>(body, headers);
                template = new RestTemplate(getClientHttpRequestFactory());
                response = null;
                response = template.exchange(url, HttpMethod.POST, request, String.class);
                if (response != null) {
                    result = response.getBody();
                    if (result.contains("Invalid difficulty\\/size specified")) {
                        System.out.println(requestString);
                        System.out.println(difficulty);
                    }
                }
            } catch (RestClientException e) {
                e.printStackTrace();
                if (e.getMessage().contains("429")) {
                    result = e.getMessage();
                } else {
                    System.out.println(e.getMessage());
                    result = null;
                }
            }
        }


        return result;
    }

    private List<CharacterRank> parseCharacterWOWLogsData(String response, Character character, Boss boss, List<CharacterRank> ranks) {

        WOWLogsCharacterRankData wowLogsRankData = null;
        if (response != null && !response.isEmpty()) {

            String result = null;
            try {
                result = response.split("\"encounterRankings\":")[1];
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(response);
            }

            String metric = null;


            result = result.substring(0, result.length() - 4);
            if (!result.contains("\"ranks\":[]")) {
                wowLogsRankData = gson.fromJson(result, WOWLogsCharacterRankData.class);
                if (wowLogsRankData != null) {
                    List<WOWLogsCharacterRankData.WOWLogsRank> wowLogsRanks = wowLogsRankData.getRanks();
                    List<Rank> fightRanks = new ArrayList<>();
                    try {
                        for (WOWLogsCharacterRankData.WOWLogsRank logsRank : wowLogsRanks) {

                            if (!isRankInOurDBAlready(logsRank, character, wowLogsRankData.getMetric())) {
                                Rank rank = new Rank();
                                rank.setDate(Instant.ofEpochMilli(Long.valueOf(logsRank.getStartTime())).atZone(ZoneId.systemDefault()).toLocalDateTime());
                                rank.setAmount((long) logsRank.getAmount());
                                rank.setKillIlvl(logsRank.getBracketData());
                                rank.setFightID(String.valueOf(logsRank.getReport().getFightID()));
                                rank.setReportCode(logsRank.getReport().getCode());
                                rank.setMetric(wowLogsRankData.getMetric());
                                fightRanks.add(rank);
                            }
                        }


                        CharacterRank characterRank = null;
                        List<CharacterRank> currentCharacterRanks = character.getRanks();
                        if (currentCharacterRanks != null) {
                            for (CharacterRank rank : currentCharacterRanks) {
                                Boss boss1 = rank.getBoss();
                                if (boss1 != null) {
                                    if (boss1.getDifficulty() == boss.getDifficulty() && boss1.getName().equalsIgnoreCase(boss.getName()) && boss1.getEncounterID() == boss.getEncounterID() && rank.getMetric().equalsIgnoreCase(metric)) {
                                        characterRank = rank;
                                    }
                                }
                            }
                        }


                        if (characterRank == null) {
                            characterRank = new CharacterRank();
                            List<Boss> bossList = bossRepo.findByEncounterID(boss.getEncounterID());
                            Boss bossFromDB = null;
                            if (bossList != null && bossList.size() > 0) {
                                for (Boss boss1 : bossList) {
                                    if (boss1.getDifficulty() == boss.getDifficulty()) {
                                        bossFromDB = boss1;
                                    }
                                }
                            }
                            if (bossFromDB == null) {
                                bossRepo.save(boss);
                                characterRank.setBoss(boss);
                            } else {
                                characterRank.setBoss(bossFromDB);
                            }
                            characterRank.setRanks(fightRanks);
                            characterRank.setTotalKills(wowLogsRankData.getTotalKills());
                            characterRank.setMaxAmount((long) wowLogsRankData.getBestAmount());
                            characterRank.setAverage((long) getAverageDPS(fightRanks));
                            characterRank.setMetric(wowLogsRankData.getMetric());
                            rankRepo.saveAll(fightRanks);
                            characterRankRepo.save(characterRank);
                        } else {
                            List<Rank> ranksFromDB = characterRank.getRanks();
                            List<Rank> updatedRanks = new ArrayList<>();
                            if (ranksFromDB == null || ranksFromDB.size() == 0) {
                                updatedRanks = fightRanks;
                            } else {
                                updatedRanks = ranksFromDB;
                                for (Rank rank : fightRanks) {
                                    updatedRanks.add(rank);
                                }
                            }
                            characterRank.setRanks(updatedRanks);
                            characterRank.setMaxAmount((long) wowLogsRankData.getBestAmount());
                            characterRank.setAverage((long) getAverageDPS(updatedRanks));
                            characterRank.setMetric(wowLogsRankData.getMetric());
                            rankRepo.saveAll(updatedRanks);
                            characterRankRepo.save(characterRank);
                        }
                        ranks.add(characterRank);

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(wowLogsRankData);
                        System.out.println(result);
                    }
                }
            }


        }
        return ranks;
    }

    private boolean isRankInOurDBAlready(WOWLogsCharacterRankData.WOWLogsRank logsRank, Character character, String metric) {
        boolean result = false;
        if (logsRank.getReport().getCode() == null) {
            return true;
        }
        List<CharacterRank> characterRankList = character.getRanks();
        if (characterRankList != null && characterRankList.size() > 0) {
            for (CharacterRank characterRank : characterRankList) {
                List<Rank> ranks = characterRank.getRanks();
                if (ranks != null) {
                    for (Rank rank : ranks) {
                        if (rank.getMetric().equalsIgnoreCase(metric) && rank.getReportCode().equalsIgnoreCase(logsRank.getReport().getCode()) && rank.getFightID().equalsIgnoreCase(String.valueOf(logsRank.getReport().getFightID()))) {
                            result = true;
                        }
                    }
                }
            }
        }

        return result;
    }

    private double getAverageDPS(List<Rank> characterRanks) {
        double result = 0;

        for (Rank characterRank : characterRanks) {
            result = result + characterRank.getAmount();
        }

        result = result / characterRanks.size();

        return result;
    }


    private void saveLog(String log, String fileName) {
        log = "\n" + log;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(log);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<Zone> getRaids() {
        Set<Zone> result = new HashSet<>();
        List<Boss> bosses = bossRepo.findAll();
        for (Boss boss : bosses) {
            result.add(boss.getZone());
        }


        return result;
    }

    public List<Boss> getBosses() {
        List<Boss> bosses = bossRepo.findAll();
        Collections.sort(bosses, BY_ENCOUNTER_ID);

        return bosses;

    }

    public List<Character> getRankedMembers() {
        List<Character> rankedCharacters = new ArrayList<>();
        List<Character> charactersFromDB = characterRepos.findAll();
        for (Character character : charactersFromDB) {

            if (character.getRanks() != null && !character.getRanks().isEmpty()) {
                rankedCharacters.add(character);
            }
        }


        return rankedCharacters;
    }


    public Map<String, Character> updateCharacterData(Character character) {
        Map<String, Character> result = new HashMap<>();
        character = updateBlizzardData(character);
        result = updateWOWLogsData(character);


        return result;
    }

    private Map<String, Character> updateWOWLogsData(Character character) {
        Map<String, Character> result = new HashMap<>();
        List<Boss> bosses = bossRepo.findAll();
        boolean is429Error = false;
        boolean isThereNoErrors = true;
        if (bosses != null && bosses.size() > 0) {
            List<CharacterRank> ranks = character.getRanks();

            ranks = new ArrayList<>();
            if (ranks == null) {
            }
            String response = "";
            for (int i = 0; i < 2; i++) {
                if (is429Error) {
                    break;
                }
                String metric;
                if (i == 0) {
                    metric = "dps";
                } else {
                    metric = "hps";
                }
                for (Boss boss : bosses) {

                    response = "";
                    response = getCharacterDataFromWOWLogsApi(boss, character, metric);
                    if (response == null) {
                        isThereNoErrors = false;
                        continue;
                    }
                    if (response.contains("429 Too Many Requests")) {
                        is429Error = true;
                        isThereNoErrors = false;
                        break;
                    }

                    ranks = parseCharacterWOWLogsData(response, character, boss, ranks);

                }
            }
            character.setRanks(ranks);
        }
        if (isThereNoErrors) {
            result.put("Successful", character);
        } else {
            result.put("there were errors during character data updating", character);
        }

        return result;
    }

    private Character updateBlizzardData(Character character) {
        String response = getGuildDataFromBlizzardDB();

        if (response != null && !response.isEmpty()) {
            String[] charactersStrings = response.split("\"character\"");
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
                    character = getCharacterDataFromBlizzardAPI(characterLink, Integer.parseInt(characterRank), characterName, character);

                }

            }

        }

        return character;
    }
}
