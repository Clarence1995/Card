package com.tecsun.card.thread;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class ThreadStateInfo implements Serializable {

    private static final long serialVersionUID = 5759858852685030129L;

    int newCount;

    int runnableCount;

    int blockedCount;

    int waitingCount;

    int timedWaitingCount;

    int terminatedCount;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + blockedCount;
        result = prime * result + newCount;
        result = prime * result + runnableCount;
        result = prime * result + terminatedCount;
        result = prime * result + timedWaitingCount;
        result = prime * result + waitingCount;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadStateInfo)) {
            return false;
        }
        ThreadStateInfo other = (ThreadStateInfo) obj;
        if (blockedCount != other.blockedCount) {
            return false;
        }
        if (newCount != other.newCount) {
            return false;
        }
        if (runnableCount != other.runnableCount) {
            return false;
        }
        if (terminatedCount != other.terminatedCount) {
            return false;
        }
        if (timedWaitingCount != other.timedWaitingCount) {
            return false;
        }
        if (waitingCount != other.waitingCount) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(256)
            .append("ThreadStateInfo [newCount=").append(newCount)
            .append(", runnableCount=").append(runnableCount)
            .append(", blockedCount=").append(blockedCount)
            .append(", waitingCount=").append(waitingCount)
            .append(", timedWaitingCount=").append(timedWaitingCount)
            .append(", terminatedCount=").append(terminatedCount)
            .append("]");
        return buffer.toString();
    }

}
