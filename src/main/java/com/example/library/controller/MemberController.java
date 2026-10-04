package com.example.library.controller;

import com.example.library.dao.MemberDao;
import com.example.library.model.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberDao memberDao;

    public MemberController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @GetMapping
    public List<Member> listMembers() {
        return memberDao.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        return memberDao.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        Member saved = memberDao.addMember(member);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        return memberDao.getMemberById(id)
                .map(existing -> {
                    existing.setName(member.getName());
                    existing.setEmail(member.getEmail());
                    return ResponseEntity.ok(memberDao.updateMember(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberDao.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
