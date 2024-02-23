package model;

import java.util.Objects;

public record AuthData(String authToken, String userName) {
    @Override
    public String toString() {
        return "AuthData{" +
                "authToken='" + authToken + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthData authData)) return false;
        return Objects.equals(authToken, authData.authToken) && Objects.equals(userName, authData.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, userName);
    }
}
