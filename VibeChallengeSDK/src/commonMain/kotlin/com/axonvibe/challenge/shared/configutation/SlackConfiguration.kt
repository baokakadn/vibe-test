package com.axonvibe.challenge.shared.configutation

const val CLIENT_ID = "2924833350.1575425842754"
const val SCOPE = "users.profile:read"
const val SECRET = "6010868c03cd656478c15af7bb22f48f"
const val LOGIN_URL = "https://axonvibe.slack.com/oauth/authorize?client_id=$CLIENT_ID&scope=$SCOPE"
const val ACCESS_TOKEN_URL =
    "https://slack.com/api/oauth.access?client_id=$CLIENT_ID&client_secret=$SECRET&"
const val USER_INFO_URL = "https://slack.com/api/users.profile.get?token="
const val AXON_ID = "2924833350"
const val TOKEN_KEY = "token"
const val NAME_KEY = "name"
const val AVATAR_KEY = "avatar"
const val USER_KEY = "user"