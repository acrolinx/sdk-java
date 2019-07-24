package com.acrolinx.client.sdk;

public class AccessToken {
    private String token;

    public AccessToken(String token) {
        this.token = token;
    }

    public String getAccessToken() {
        return token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccessToken other = (AccessToken) obj;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Access token is hidden for security reason";
    }

    public boolean isEmpty() {
        return this.token == null || this.token.isEmpty();
    }
}
