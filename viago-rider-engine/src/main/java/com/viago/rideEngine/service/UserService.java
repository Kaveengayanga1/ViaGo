package com.viago.rideEngine.service;

import java.util.Map;

public interface UserService {
    Object getUserProfile(String userId);
    Object updateUserProfile(String userId, Map<String, Object> profileData);
    Object getUserInfo(String userId);
    Object getUserStatistics(String userId);
    Object updateUserPreferences(String userId, Map<String, Object> preferences);
    Object getUserPreferences(String userId);
    Object getSavedAddresses(String userId);
    Object addSavedAddress(String userId, Map<String, Object> address);
    void deleteSavedAddress(String userId, String addressId);
    Object updateProfilePicture(String userId, String pictureUrl);
}

