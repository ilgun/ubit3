/*
 * $Id: Login.java 739661 2009-02-01 00:06:00Z davenewton $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * ", "", "LOGIN_NAME", "LOGIN_PASS
 */

package com.ilgun.ubit3.sound;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.opensymphony.xwork2.ActionSupport;
import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;
import de.voidplus.soundcloud.User;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Login extends ActionSupport implements SessionAware {
    private Long trackId;
    private String username;
    private String s3username;
    private String password;
    private String s3password;
    private String bucketName;
    private User user;
    private SoundCloud soundcloud;
    private Map<String, Object> session;
    private Track track;
    private ArrayList<Track> allTracks;
    private static final String DEFAULT_BUCKETNAME = "com.ilgun.soundcloud";
    private static final String APP_CLIENT_ID = "8270d55f791726de7b790c1e6082d079";
    private static final String APP_CLIENT_SECRET = "0d1c00648685b4023e151a1d68175455";

    public Login() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    private boolean isInvalid(String value) {
        return (value == null || value.length() == 0);
    }

    public SoundCloud getSoundcloud() {
        return soundcloud;
    }


    public void setSoundcloud(SoundCloud soundcloud) {
        this.soundcloud = soundcloud;
    }

    public ArrayList<Track> getAllTracks() {
        return allTracks;
    }

    public void setAllTracks(ArrayList<Track> allTracks) {
        this.allTracks = allTracks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getS3username() {
        return s3username;
    }

    public void setS3username(String s3username) {
        this.s3username = s3username;
    }

    public String getS3password() {
        return s3password;
    }

    public void setS3password(String s3password) {
        this.s3password = s3password;
    }

    public String login() throws Exception {
        soundcloud = new SoundCloud(APP_CLIENT_ID, APP_CLIENT_SECRET, username, password);
        user = soundcloud.getMe();
        session.put("soundcloud", soundcloud);
        session.put("usr", user);
        return SUCCESS;
    }

    public String songMenu() throws Exception {
        allTracks = ((SoundCloud) session.get("soundcloud")).getMeTracks();
        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map<String, Object> getSession() {
        return session;
    }


    public String sthreeLoginPage() throws Exception {
        track = ((SoundCloud) session.get("soundcloud")).getTrack(trackId.intValue());
        return "success";
    }

    public String logins3() throws Exception {
        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(s3username, s3password));
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        session.put("s3", s3);

        String realBucketName = (bucketName.isEmpty() ? DEFAULT_BUCKETNAME : bucketName);

        if (!s3.doesBucketExist(realBucketName)) {
            s3.createBucket(realBucketName);
        }

        track = ((SoundCloud) session.get("soundcloud")).getTrack(trackId.intValue());
        //s3.putObject(new PutObjectRequest(realBucketName, track.getTitle()+track.getStreamUrl(), track.getStreamUrl()));
        URL url = new URL(track.getStreamUrl());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        ObjectMetadata metadata;
        metadata = new ObjectMetadata();
        InputStream streamlink = conn.getInputStream();
        s3.putObject(new PutObjectRequest(realBucketName, track.getTitle(), streamlink, metadata));
        
        return SUCCESS;
    }
}