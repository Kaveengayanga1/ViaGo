package com.viago.matchingservice.cointroller;

import com.viago.matchingservice.dto.MatchingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matching")
@Slf4j
public class MatchingController {
    @PostMapping("/find-driver")
    public ResponseEntity<String> findDriver(@RequestBody MatchingRequest request) {

        log.info("🔔 Matching Request Received for Trip: {}", request.getTripId());
        log.info("📍 Location: {}, {}", request.getPickupLat(), request.getPickupLng());

        // --- Logic එක (Algorithm) ---
        // ඇත්තටම මෙතන වෙන්න ඕනේ:
        // 1. Location Service එකෙන් ළඟ ඉන්න Drivers ලා ගන්න එක.
        // 2. ඒ අයගෙන් හොඳම කෙනා තෝරන එක.

        // දැනට අපි බොරුවට (Dummy) Driver ID එකක් යවමු:
        try {
            // තත්පර 2ක් හොයනවා වගේ පෙන්වමු (Simulate delay)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String foundDriverId = "DRIVER_999";
        log.info("✅ Driver Found: {}", foundDriverId);

        return ResponseEntity.ok(foundDriverId);
    }
}
