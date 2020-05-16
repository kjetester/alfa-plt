package ru.alfabank.platform.users;

import java.util.List;
import ru.alfabank.platform.businessobjects.AccessToken;
import ru.alfabank.platform.businessobjects.enums.Team;

public interface AccessibleUser {

  AccessToken jwt = null;

  String getLogin();

  String getPassword();

  AccessToken getJwt();

  void setJwt(AccessToken jwt);

  List<Team> getTeams();
}
