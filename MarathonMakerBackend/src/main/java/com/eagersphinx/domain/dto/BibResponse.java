package com.eagersphinx.domain.dto;

import com.eagersphinx.domain.entity.Loop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BibResponse extends Response {
    private Map<Integer, LoopResp> loops;

    public static class LoopResp {
        Integer bib;
        String eventName;
        String name;

        Boolean isAwarded;
        List<Loop> loops;


        public LoopResp(String uname, String ename, Integer bib, Boolean isAwarded, List<Loop> list) {
            name =uname;
            this.isAwarded = isAwarded;
            this.bib = bib;
            loops = list;
            eventName = ename;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Loop> getLoops() {
            return loops;
        }

        public void setLoops(List<Loop> loops) {
            this.loops = loops;
        }

        public Integer getBib() {
            return bib;
        }

        public void setBib(Integer bib) {
            this.bib = bib;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public Boolean getAwarded() {
            return isAwarded;
        }

        public void setAwarded(Boolean awarded) {
            isAwarded = awarded;
        }
    }
}


