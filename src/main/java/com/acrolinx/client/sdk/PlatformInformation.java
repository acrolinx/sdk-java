package com.acrolinx.client.sdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


// This is how its done?
public class PlatformInformation {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("data")
    @Expose
    private Data data;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("server")
        @Expose
        private Server server;
        @SerializedName("locales")
        @Expose
        private List<String> locales = null;

        public Server getServer() {
            return server;
        }

        public void setServer(Server server) {
            this.server = server;
        }

        public List<String> getLocales() {
            return locales;
        }

        public void setLocales(List<String> locales) {
            this.locales = locales;
        }

    }

    public class Links {

        @SerializedName("signIn")
        @Expose
        private String signIn;

        public String getSignIn() {
            return signIn;
        }

        public void setSignIn(String signIn) {
            this.signIn = signIn;
        }

    }

    public class Server {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("version")
        @Expose
        private String version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

    }

}
