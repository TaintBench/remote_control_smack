package org.jivesoftware.smackx.workgroup.agent;

public class TransferRequest extends OfferContent {
    private String inviter;
    private String reason;
    private String room;

    public TransferRequest(String inviter, String room, String reason) {
        this.inviter = inviter;
        this.room = room;
        this.reason = reason;
    }

    public String getInviter() {
        return this.inviter;
    }

    public String getRoom() {
        return this.room;
    }

    public String getReason() {
        return this.reason;
    }

    /* access modifiers changed from: 0000 */
    public boolean isUserRequest() {
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean isInvitation() {
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean isTransfer() {
        return true;
    }
}
