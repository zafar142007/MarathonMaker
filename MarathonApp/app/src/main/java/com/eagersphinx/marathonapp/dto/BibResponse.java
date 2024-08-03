package com.eagersphinx.marathonapp.dto;

import java.util.List;
import java.util.Map;

public class BibResponse extends Response {
    private Map<Integer, LoopResp> loops;

    public void setLoops(Map<Integer, LoopResp> loops) {
        this.loops = loops;
    }

    public Map<Integer, LoopResp> getLoops() {
        return loops;
    }

    public static class LoopResp {
        Integer bib;
        String eventName;
        String name;
        Boolean awarded;
        List<Loop> loops;

        public LoopResp() {
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

        public String getEventName() {
            return eventName;
        }

        public void setBib(Integer bib) {
            this.bib = bib;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public Boolean getAwarded() {
            return awarded;
        }

        public void setAwarded(Boolean awarded) {
            this.awarded = awarded;
        }

        @Override
        public String toString() {
            return "LoopResp{" +
                    "bib=" + bib +
                    ", eventName='" + eventName + '\'' +
                    ", name='" + name + '\'' +
                    ", loops=" + loops +
                    '}';
        }
    }
}

