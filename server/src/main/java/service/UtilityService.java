package service;

public class UtilityService extends Service{
    public static boolean authenticate(String authToken) {
        if (authDAO.getAuth(authToken) != null) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean clear() {

        if (authDAO.clear() &&
            gameDAO.clear() &&
            userDAO.clear()) {
            return true;
        }
        else {
            return false;
        }
    }
}
