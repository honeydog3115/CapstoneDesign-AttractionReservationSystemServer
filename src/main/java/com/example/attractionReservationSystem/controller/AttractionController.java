package com.example.attractionReservationSystem.controller;

import com.example.attractionReservationSystem.dao.Attraction;
import com.example.attractionReservationSystem.repository.AttractionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/attraction")
public class AttractionController {

    @Autowired
    private AttractionRepository attractionRepository;

    // 모든 놀이기구 목록 조회
    @GetMapping("/lists")
    public ResponseEntity<?> getAllAttractions() {
        List<Attraction> attractions = attractionRepository.findAll();

        if(attractions == null)
            return ResponseEntity.badRequest().body(new String("저장된 놀이기구가 없습니다."));

        return ResponseEntity.ok(attractions);
    }

    // 특정 놀이기구 조회
    @GetMapping("/{attractionId}")
    public ResponseEntity<Attraction> getAttractionById(@PathVariable int attractionId) {
        return attractionRepository.findAllAttractionById(attractionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 놀이기구 추가
    @PostMapping
    public ResponseEntity<?> addAttraction(@RequestBody Attraction newAttraction) {
        attractionRepository.save(newAttraction);
        return ResponseEntity.ok(new String("놀이기구가 추가되었습니다."));
    }

    // 놀이기구 정보 수정
    @PutMapping("/{attractionId}")
    public ResponseEntity<?> updateAttraction(@PathVariable int attractionId, @RequestBody Attraction updatedAttraction) {
        return attractionRepository.findAllAttractionById(attractionId)
                .map(existingAttraction -> {
                    existingAttraction.setName(updatedAttraction.getName());
                    existingAttraction.setWaitingCount(updatedAttraction.getWaitingCount());
                    attractionRepository.save(existingAttraction);
                    return ResponseEntity.ok(new String("놀이기구 정보가 수정되었습니다."));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 놀이기구 삭제
    @DeleteMapping("/{attractionId}")
    public ResponseEntity<?> deleteAttraction(@PathVariable int attractionId) {
        return attractionRepository.findAllAttractionById(attractionId)
                .map(existingAttraction -> {
                    attractionRepository.delete(existingAttraction);
                    return ResponseEntity.ok(new String("놀이기구가 삭제되었습니다."));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
