package interfaces;

import entity.Match;

import javax.ejb.Local;
import java.util.List;

@Local
public interface IUpcomingMatchesService {
    List<Match> getNearMatches();

    void betThisMatch(Match match) throws Exception;

    void createOdd(Match match) throws Exception;
}
