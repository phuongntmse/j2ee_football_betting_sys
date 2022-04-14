package services;

import entity.Bet;
import entity.Match;
import entity.Odd;
import entity.User;
import interfaces.IAdminstratorService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Local(IAdminstratorService.class)
@Stateless
public class AdminstratorService implements IAdminstratorService {
    @EJB
    private DBService dbService;

    @Override
    public void updateUser(User n_user) throws Exception {
        try {
            dbService.updateUser(n_user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void syncMatcheswithAPI() throws Exception {
        try {
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.DATE, 10);
            Date endDate = c.getTime();
            String modifiedtoday = new SimpleDateFormat("yyyy-MM-dd").format(today);
            String modifiedenđate = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            String duration = "dateFrom=" + modifiedtoday + "&dateTo=" + modifiedenđate;
            URL url = new URL("http://api.football-data.org/v2/matches?status=SCHEDULED&" + duration);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("X-Auth-Token", "ee4be6019e6d49a3a3e472b8626b32c5");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer rp = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    rp.append(line);
                    rp.append('\n');
                }
                br.close();
                String response = rp.toString();
                JsonReader jsonReader = Json.createReader(new StringReader(response));
                JsonObject json = jsonReader.readObject();
                JsonArray data = json.getJsonArray("matches");
                if (data != null) {
                    List<String> comCodeList = dbService.getCompCodeList();
                    for (int j = 0; j < data.size(); j++) {
                        JsonObject jsonmatch = data.getJsonObject(j);
                        String code_id = jsonmatch.getJsonObject("competition").getJsonObject("area").getString("code");
                        if (comCodeList.contains(code_id)) {
                            Match n_match = new Match();
                            int mid = jsonmatch.getInt("id");
                            boolean exist = dbService.checkMatchExist(code_id + "-" + mid);
                            System.out.println(code_id + "-" + mid);
                            if (!exist) {
                                n_match.setM_id(code_id + "-" + mid);
                                n_match.setM_competition(code_id);
                                n_match.setM_team1(jsonmatch.getJsonObject("homeTeam").getString("name"));
                                n_match.setM_team2(jsonmatch.getJsonObject("awayTeam").getString("name"));
                                n_match.setM_sdate(jsonmatch.getString("utcDate"));
                                n_match.setM_status(0);
                                n_match.setM_winner(-1);
                                dbService.addMatch(n_match);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void syncMatchesRSwithAPI() throws Exception {
        try {
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.DATE, -5);
            Date fromDate = c.getTime();
            String dateTo = new SimpleDateFormat("yyyy-MM-dd").format(today);
            String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
            String duration = "dateFrom=" + dateFrom + "&dateTo=" + dateTo;
            URL url = new URL("http://api.football-data.org/v2/matches?status=FINISHED&" + duration);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("X-Auth-Token", "ee4be6019e6d49a3a3e472b8626b32c5");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer rp = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    rp.append(line);
                    rp.append('\n');
                }
                br.close();
                String response = rp.toString();
                System.out.println(response);
                JsonReader jsonReader = Json.createReader(new StringReader(response));
                JsonObject json = jsonReader.readObject();
                JsonArray data = json.getJsonArray("matches");
                if (data != null) {
                    List<String> comCodeList = dbService.getCompCodeList();
                    for (int j = 0; j < data.size(); j++) {
                        JsonObject jsonmatch = data.getJsonObject(j);
                        String code_id = jsonmatch.getJsonObject("competition").getJsonObject("area").getString("code");
                        if (comCodeList.contains(code_id)) {
                            int mid = jsonmatch.getInt("id");
                            boolean exist = dbService.checkMatchExist(code_id + "-" + mid);
                            if (exist) {
                                Match n_match = dbService.getMatchInformation(code_id + "-" + mid);
                                n_match.setM_status(1);
                                String rs = jsonmatch.getJsonObject("score").getString("winner");
                                System.out.println(n_match.getM_id() + " - " + rs);
                                if (rs.equals("AWAY_TEAM")) {
                                    n_match.setM_winner(1);
                                } else if (rs.equals("HOME_TEAM")) {
                                    n_match.setM_winner(0);
                                } else n_match.setM_winner(2);
                                dbService.updateMatch(n_match);
                                //process all odds related to this matches
                                List<Odd> relatedOdds = dbService.getOddsOfMatch(n_match.getM_id());
                                for (Odd odd : relatedOdds) {
                                    System.out.print(odd.getO_id());
                                    List<Bet> relatedBets = dbService.getBetsofOdd(odd.getO_id());
                                    if (odd.getO_type() == n_match.getM_winner()) {
                                        for (Bet bet : relatedBets) {
                                            System.out.print(bet.getP_id());
                                            int win_coin = (int) odd.getO_winodd() * bet.getP_limcoins();
                                            User u = dbService.getUserInformation(bet.getP_bettor());
                                            u.setU_limcoins(u.getU_limcoins() + win_coin);
                                            dbService.updateUser(u);
                                            bet.setP_status(1);//win
                                            dbService.updateBet(bet);
                                            //bookmaker must send limcoins for bettors
                                            User bookmaker = dbService.getUserInformation(odd.getO_owner());
                                            bookmaker.setU_limcoins(bookmaker.getU_limcoins() - win_coin);
                                            dbService.updateUser(bookmaker);
                                        }
                                    } else {
                                        for (Bet bet : relatedBets) {
                                            bet.setP_status(0);//lose
                                            dbService.updateBet(bet);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<User> getListUser() {
        return dbService.getUsers();
    }
}
