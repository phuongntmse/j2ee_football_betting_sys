package services;

import entity.*;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
@Named
public class DBService {
    @PersistenceContext(unitName = "defaultUnit")
    EntityManager entityManager;

    public void addMatch(Match n_match) {
        entityManager.persist(n_match);
    }

    public void addOdd(Odd n_odd) {
        entityManager.persist(n_odd);
    }

    public void addBet(Bet n_bet) {
        entityManager.persist(n_bet);
        User u = getUserInformation(n_bet.getP_bettor());
        u.setU_limcoins(u.getU_limcoins() - n_bet.getP_limcoins());
        updateUser(u);
    }

    public void register(User n_user) {
        entityManager.persist(n_user);
    }

    public void updateUser(User user) {
        entityManager.merge(user);
    }

    public void updateMatch(Match match) {
        entityManager.merge(match);
    }

    public void updateBet(Bet bet) {
        entityManager.merge(bet);
    }

    public User getUserInformation(String username) {
        Query emQuery = entityManager.createQuery("select object(u) from User as u where u.username = :usn");
        emQuery.setParameter("usn", username);
        List<User> list = (List<User>) emQuery.getResultList();
        if (list.isEmpty()) return null;
        else return list.get(0);
    }

    public boolean checkAuth(String username, String password) {
        Query emQuery = entityManager.createQuery("select object(u) from User as u where u.username = :usn and u.password = :pwd");
        emQuery.setParameter("usn", username);
        emQuery.setParameter("pwd", password);
        List<User> list = (List<User>) emQuery.getResultList();
        return list.size() != 0;
    }

    public List<User> getUsersRankingfromDB() {
        Query emQuery = entityManager.createQuery("select object (u) from User as u where u.u_role <> :role order by u.u_limcoins DESC");
        emQuery.setParameter("role", 0); //remove Admin from list
        List<User> list = (List<User>) emQuery.getResultList();
        return list;
    }

    public List<User> getUsers() {
        Query emQuery = entityManager.createQuery("select object (u) from User as u where u.u_role <> :role");
        emQuery.setParameter("role", 0); //remove Admin from list
        List<User> list = (List<User>) emQuery.getResultList();
        return list;
    }

    public Match getMatchInformation(String matchID) {
        Query emQuery = entityManager.createQuery("select object(m) from Match as m where m.m_id = :mid");
        emQuery.setParameter("mid", matchID);
        List<Match> list = (List<Match>) emQuery.getResultList();
        if (list.isEmpty()) return null;
        else return list.get(0);
    }

    public List<Match> getMatchesbyStatus(int status) {
        Query emQuery = entityManager.createQuery("select object (m) from Match as m where m.m_status = :mstt order by m.m_sdate ASC");
        emQuery.setParameter("mstt", status);
        List<Match> list = (List<Match>) emQuery.getResultList();
        return list;
    }

    public List<Match> getUpcomingMatches() {
        Query emQuery = entityManager.createQuery("select object (m) from Match as m where m.m_status = :mstt order by m.m_sdate ASC");
        emQuery.setParameter("mstt", 0);
        emQuery.setMaxResults(20);
        List<Match> list = (List<Match>) emQuery.getResultList();
        return list;
    }

    public boolean checkMatchExist(String matchID) {
        Query emQuery = entityManager.createQuery("select object (m) from Match as m where m.m_id = :matchID");
        emQuery.setParameter("matchID", matchID);
        if (emQuery.getResultList().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkOddAvailable(int type, String username, String match_id) {
        Query emQuery = entityManager.createQuery("select object (o) from Odd as o where o.o_type = :otype and o.o_owner = :osn and o.o_matchID = :omatchid");
        emQuery.setParameter("otype", type);
        emQuery.setParameter("osn", username);
        emQuery.setParameter("omatchid", match_id);
        if (emQuery.getResultList().size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkOddMatchAvailable(String username, String match_id) {
        Query emQuery = entityManager.createQuery("select object (o) from Odd as o where o.o_owner = :osn and o.o_matchID = :omatchid");
        emQuery.setParameter("osn", username);
        emQuery.setParameter("omatchid", match_id);
        if (emQuery.getResultList().size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkBetAvailabel(String username, String odd_id) {
        Query emQuery = entityManager.createQuery("select object (b) from Bet as b where b.p_oddID = :oid and b.p_bettor = :usn");
        emQuery.setParameter("oid", odd_id);
        emQuery.setParameter("usn", username);
        if (emQuery.getResultList().size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public List<Competition> getCompetitionList() {
        Query emQuery = entityManager.createQuery("select object (c) from Competition as c ");
        List<Competition> list = (List<Competition>) emQuery.getResultList();
        return list;
    }

    public List<String> getCompCodeList() {
        Query emQuery = entityManager.createQuery("select c.c_id from Competition as c ");
        List<String> list = (List<String>) emQuery.getResultList();
        return list;
    }

    public List<Odd> getOddsOfMatch(String matchID) {
        Query emQuery = entityManager.createQuery("select object (o) from Odd as o where o.o_matchID = :mid ");
        emQuery.setParameter("mid", matchID);
        List<Odd> list = (List<Odd>) emQuery.getResultList();
        return list;
    }

    public List<String> getBookmakerOfMatch(String matchID) {
        Query emQuery = entityManager.createQuery("select distinct o.o_owner from Odd as o where o.o_matchID = :mid");
        emQuery.setParameter("mid", matchID);
        List<String> list = (List<String>) emQuery.getResultList();
        return list;
    }

    public List<Odd> getOddsOfBookmaker(String bookmakerid) {
        Query emQuery = entityManager.createQuery("select object (o) from Odd as o where o.o_owner = :uid ");
        emQuery.setParameter("uid", bookmakerid);
        List<Odd> list = (List<Odd>) emQuery.getResultList();
        return list;
    }

    public List<Odd> getRelatedOdd(String matchID, String ownerid) {
        Query emQuery = entityManager.createQuery("select object (o) from Odd as o where o.o_matchID = :mid and o.o_owner = :uid");
        emQuery.setParameter("mid", matchID);
        emQuery.setParameter("uid", ownerid);
        List<Odd> list = (List<Odd>) emQuery.getResultList();
        return list;
    }

    public List<Match> getMatchesHasOdds() {
        Query emQuery = entityManager.createQuery("select distinct object (m) from Match as m join Odd as o on m.m_id = o.o_matchID where m.m_status = :mstt");
        emQuery.setParameter("mstt", 0);
        List<Match> list = (List<Match>) emQuery.getResultList();
        return list;
    }

    public List<Bet> getBetsofOdd(String oddID) {
        Query emQuery = entityManager.createQuery("select object (b) from Bet as b where b.p_oddID = :oddid");
        emQuery.setParameter("oddid", oddID);
        List<Bet> list = (List<Bet>) emQuery.getResultList();
        return list;
    }

    public List<Bet> getBetsofUser(String UID) {
        Query emQuery = entityManager.createQuery("select object (b) from Bet as b where b.p_bettor= :uid");
        emQuery.setParameter("uid", UID);
        List<Bet> list = (List<Bet>) emQuery.getResultList();
        return list;
    }

    public Odd getOddInformation(String oddID) {
        Query emQuery = entityManager.createQuery("select object (o) from Odd as o where o.o_id = :oid");
        emQuery.setParameter("oid", oddID);
        List<Odd> list = (List<Odd>) emQuery.getResultList();
        if (list.isEmpty()) return null;
        return list.get(0);
    }

    public int getTotalBetsofOdd(String oddID) {
        return getBetsofOdd(oddID).size();
    }

    public long getBenifitofOdd(String oddID, int rs) {
        if (rs == -1) return 0;
        List<Bet> betList = getBetsofOdd(oddID);
        long result = 0;
        Odd odd = getOddInformation(oddID);
        for (Bet b : betList) {
            if (odd.getO_type() == rs) {
                result -= (long) b.getP_limcoins() * odd.getO_winodd();
            } else {
                result += b.getP_limcoins();
            }
        }
        return result;
    }
}
